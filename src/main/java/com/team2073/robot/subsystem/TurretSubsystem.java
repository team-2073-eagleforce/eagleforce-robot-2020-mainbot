package com.team2073.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.pathfollowing.math.InterpolatingDouble;
import com.team2073.common.pathfollowing.math.InterpolatingTreeMap;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import com.team2073.robot.Limelight.Pipeline;
import com.team2073.robot.Mediator;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretSubsystem implements AsyncPeriodicRunnable {


    //    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    //private Mediator mediator = Mediator.getInstance();
//    private static final double KP_LIMELIGHT = 0.0225; // 0.015
    private static final double KP_LIMELIGHT = 0.01;
    //    private static final double KP_ENCODER = 0.0033;
//    private static final double KP_ENCODER = 0.013;
    private static final double KP_ENCODER = 0.01;

    private static final double acceptableError = 0.05;
    //    PRACTICE
//    private static final double MIN_POSITION = -35.328 - 12.6716;
//    private static final double MAX_POSITION = 221.8 - 12.6716;
//    private static final double POT_MAX_VALUE = 0.9574;
//    private static final double POT_MIN_VALUE = 0.5243;
    private static final double POT_MAX_VALUE = 0.5140;
    private static final double POT_MIN_VALUE = 7.38e-4;
    private static final double MIN_POSITION = -52.2855;
    private static final double MAX_POSITION = 258.8285;
    private static final double MIN_OUTPUT = 0.04;

    private static final double WOF_POSITION = 81.714 - 35.328 - 12.6716;
    boolean ifReachedClosest = false;
    boolean reachedMax = false;
    boolean reachedMin = false;
    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Limelight limelight = appCtx.getLimelight();
    private AnalogPotentiometer potentiometer = appCtx.getPotentiometer(); // WARNING: Port path on this is randomly chosen!
    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    private CANEncoder encoder = turretMotor.getEncoder();
    private boolean rotatingClockwise = true;
    private boolean hasZeroed = false;
    private PidfControlLoop pidLimelight = new PidfControlLoop(KP_LIMELIGHT, 1e-5, 0.0015, 0, 0.35);
    private PidfControlLoop pidEncoder = new PidfControlLoop(KP_ENCODER, 0, 0.00075, 0.0, 0.35);
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> lowDistanceToRPM = new InterpolatingTreeMap<>();
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> highDistanceToRPM = new InterpolatingTreeMap<>();
    private Pipeline lastPipeline = Pipeline.CLOSE;
    /*

    THE DIRECTION OF DRIVE GYRO AND TURRET ARE FLIPPED FROM EACHOTHER MAKE SURE MATH UNDERSTANDS THAT

    Use limelight to turn to center the image
    Zoom in to stabilize image
    Calculate distance using limelight and lidar?
    Compensate for elevator height
        - Calc RPM goal for Shooter based on distance
    Zero relative to robot
    Always face shot wall by using gyro *maybe not
    Determine when to move hood
    Prevent wires from doing the wrap

     */
    private Double setpoint = null;
    private Mediator mediator;
    private TurretState state = TurretState.WAIT;
    // Positive Output = Turret turns clockwise
    private double lastTurretAngle = 0;

    private boolean adjusted = false;
    private Pipeline pipeline;
    private Timer swapTimer = new Timer();
    private boolean swapTimerStarted = false;

    public TurretSubsystem() {
        autoRegisterWithPeriodicRunner(10);
        turretMotor.setInverted(true);
        turretMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        limelight.setxOffset(1.4);
        pidLimelight.setPositionSupplier(() -> limelight.getAdjustedTx());
        pidEncoder.setPositionSupplier(this::getPosition);
        encoder.setPositionConversionFactor(360 / 30d);
        encoder.setVelocityConversionFactor(6 / 30d);
        initializeMap();
        SmartDashboard.putBoolean("turnLeftShot", true);
        SmartDashboard.putBoolean("turnRightShot", true);
    }

    public void setState(TurretState state) {
        this.state = state;
    }

    private void initializeMap() {
//       Recording on 2/17/2020
        lowDistanceToRPM.put(new InterpolatingDouble(180d), new InterpolatingDouble(5300d));
        lowDistanceToRPM.put(new InterpolatingDouble(192d), new InterpolatingDouble(5375d));
        lowDistanceToRPM.put(new InterpolatingDouble(204d), new InterpolatingDouble(5450d));
        lowDistanceToRPM.put(new InterpolatingDouble(216d), new InterpolatingDouble(5500d));
        lowDistanceToRPM.put(new InterpolatingDouble(228d), new InterpolatingDouble(5545d));
        lowDistanceToRPM.put(new InterpolatingDouble(240d), new InterpolatingDouble(5675d));
        lowDistanceToRPM.put(new InterpolatingDouble(252d), new InterpolatingDouble(5700d));
        lowDistanceToRPM.put(new InterpolatingDouble(264d), new InterpolatingDouble(5775d));
        lowDistanceToRPM.put(new InterpolatingDouble(276d), new InterpolatingDouble(5850d));
        lowDistanceToRPM.put(new InterpolatingDouble(288d), new InterpolatingDouble(5960d));
        lowDistanceToRPM.put(new InterpolatingDouble(300d), new InterpolatingDouble(6025d));
        lowDistanceToRPM.put(new InterpolatingDouble(312d), new InterpolatingDouble(6065d));
        lowDistanceToRPM.put(new InterpolatingDouble(324d), new InterpolatingDouble(6150d));
        lowDistanceToRPM.put(new InterpolatingDouble(336d), new InterpolatingDouble(6300d));
        lowDistanceToRPM.put(new InterpolatingDouble(348d), new InterpolatingDouble(6450d));
        lowDistanceToRPM.put(new InterpolatingDouble(360d), new InterpolatingDouble(6650d));
        lowDistanceToRPM.put(new InterpolatingDouble(408d), new InterpolatingDouble(7300d));
        lowDistanceToRPM.put(new InterpolatingDouble(450d), new InterpolatingDouble(8300d));

        highDistanceToRPM.put(new InterpolatingDouble(72d), new InterpolatingDouble(6475d));
        highDistanceToRPM.put(new InterpolatingDouble(84d), new InterpolatingDouble(6000d));
        highDistanceToRPM.put(new InterpolatingDouble(92d), new InterpolatingDouble(6000d));
        highDistanceToRPM.put(new InterpolatingDouble(113d), new InterpolatingDouble(5900d));
        highDistanceToRPM.put(new InterpolatingDouble(120d), new InterpolatingDouble(4805d));
        highDistanceToRPM.put(new InterpolatingDouble(132d), new InterpolatingDouble(4800d));
        highDistanceToRPM.put(new InterpolatingDouble(144d), new InterpolatingDouble(4800d));
        highDistanceToRPM.put(new InterpolatingDouble(160d), new InterpolatingDouble(4850d));
        highDistanceToRPM.put(new InterpolatingDouble(168d), new InterpolatingDouble(4900d));
        highDistanceToRPM.put(new InterpolatingDouble(180d), new InterpolatingDouble(4870d));
        highDistanceToRPM.put(new InterpolatingDouble(200d), new InterpolatingDouble(4980d));
        highDistanceToRPM.put(new InterpolatingDouble(220d), new InterpolatingDouble(5075d));
        highDistanceToRPM.put(new InterpolatingDouble(336d), new InterpolatingDouble(6050d));
        highDistanceToRPM.put(new InterpolatingDouble(348d), new InterpolatingDouble(6150d));
        highDistanceToRPM.put(new InterpolatingDouble(360d), new InterpolatingDouble(6350d));
        highDistanceToRPM.put(new InterpolatingDouble(408d), new InterpolatingDouble(7100d));

    }

    @Override
    public void onPeriodicAsync() {

        if (mediator == null) {
            mediator = Mediator.getInstance();
        }

//        System.out.println("POT: " + potentiometer.get() + "\t position: " + getPosition());
//        System.out.println("Turret Position: " + getPosition() +  "\t OutputPer: " + turretMotor.getAppliedOutput() +
//                "\t tx" + limelight.getAdjustedTx() + "\t distance: " + mediator.targetDistance());
//                System.out.println("Pot: " + potentiometer.get() + "\t pos: " + getPosition());
//        System.out.println("Pos: " + getPosition() + "\t Output: " + turretMotor.getAppliedOutput() +
//                "\t setpoint: " + setpoint + "\t state: " + state.name() +
//                "\t mediatorSetponit: " + mediator.robotAngleToTarget() + "\t tx: " + limelight.getAdjustedTx() + "\t Reached: " + ifReachedClosest);


        if (!hasZeroed && encoder.getVelocity() <= 0.005) {
            encoder.setPosition(potPosition());
            hasZeroed = true;
        }

        if (limelight.isBlind()) {
            pidEncoder.resetAccumulatedError();
        }

        switch (state) {
            case WOF:
                setpoint = WOF_POSITION;
                pidControlloop();
                break;
            case GYRO:
                limelight.setLedOn(false);
                setpoint = mediator.robotAngleToTarget();
                pidControlloop();
                break;
            case WAIT:
                limelight.setLedOn(false);
                turretMotor.set(0);
                break;
            case FACE_FRONT:
                limelight.setLedOn(false);
                setpoint = 0d;
                pidControlloop();
                break;
            case SEEK:
                double distance = mediator.smoothTargetDistance();
                pipeline = calculatePipeline(distance);
                limelight.setCurrentPipeline(pipeline);
                limelight.setLedOn(true);
                if (pipeline != lastPipeline) {
                    if (!swapTimerStarted) {
                        swapTimer.start();
                        swapTimerStarted = true;
                    }

                } else if (swapTimerStarted) {
                    if (!swapTimer.hasPeriodPassed(.3)) {
                        return;
                    } else {
                        swapTimerStarted = false;
                        swapTimer.reset();
                    }
                }
                if (limelight.getTv() != 0 && pipeline == Pipeline.FAR) {
                    shootTargeting();
                    return;
                }

                if (limelight.isBlind()) {
                    seekTarget();
                } else {
                    shootTargeting();
                }
                break;

        }
        lastPipeline = limelight.getCurrentPipeline();

    }

    private void pidControlloop() {
        setpoint = setpoint > MAX_POSITION ? MAX_POSITION - 5 : setpoint;
        setpoint = setpoint < MIN_POSITION ? MIN_POSITION + 5 : setpoint;
        pidEncoder.updateSetPoint(setpoint);
        pidEncoder.updatePID();
        double output = pidEncoder.getOutput() + MIN_OUTPUT * Math.signum(-(getPosition() - setpoint));
        turretMotor.set(output);
    }

    public void updateTargetSighting(double turretAngle, double limelightTx) {
        lastTurretAngle = turretAngle + limelightTx;
    }

    public double predictTurretAngle() {
        return lastTurretAngle;
    }

    public double calcRPMGoal(ElevatorSubsytem.ElevatorState elevatorState) {

        if (elevatorState == ElevatorSubsytem.ElevatorState.BOTTOM) {

            Double low = lowDistanceToRPM.getInterpolated(new InterpolatingDouble(limelight.getLowDistance())).value;
            return low == null ? 3000 : low;
        } else {
            Double high = highDistanceToRPM.getInterpolated(new InterpolatingDouble(limelight.getHighDistance())).value;
            return high == null ? 6000 : high;
        }

    }

    private double potPosition() {
        return MathUtil.map(potentiometer.get(), POT_MIN_VALUE, POT_MAX_VALUE, MIN_POSITION, MAX_POSITION);
    }

    private void zeroEncoder() {
        turretMotor.getEncoder().setPosition(0);
    }

    private double getPosition() {
        return (turretMotor.getEncoder().getPosition());
    }

    private Pipeline calculatePipeline(double distance) {
        Pipeline pipeline = limelight.getCurrentPipeline();
        if (pipeline == Pipeline.CLOSE) {
            if (distance > 170 && limelight.getAdjustedTx() < 4d) {
                pipeline = Pipeline.FAR;
            }
        } else {
            if (distance < 140) {
                pipeline = Pipeline.CLOSE;
            }
        }
        return pipeline;
    }

    private void seekTarget() {
        double output = 0;

        if (Math.abs(MIN_POSITION - getPosition()) < Math.abs(MAX_POSITION - getPosition()) && !ifReachedClosest) {
            if (getPosition() > MIN_POSITION + 17 && getPosition() < MAX_POSITION - 15) {
                output = -0.45;
            }

            if (getPosition() <= MIN_POSITION + 18) {
                reachedMin = true;
                ifReachedClosest = true;
            }
        } else {
            if (getPosition() > MIN_POSITION + 17 && getPosition() < MAX_POSITION - 14 && !ifReachedClosest) {
                output = 0.45;
            }

            if (getPosition() >= MAX_POSITION - 15) {
                ifReachedClosest = true;
                reachedMax = true;
            }
        }

        if (ifReachedClosest) {
            if (reachedMin && !(getPosition() > MAX_POSITION - 20)) {
                output = 0.45;
            } else if (reachedMax && !(getPosition() < MIN_POSITION + 15)) {
                output = -.45;
            } else {
                resetReachedClosest();
            }
        }

        turretMotor.set(output);
    }

    public void resetReachedClosest() {
        this.ifReachedClosest = false;
        reachedMin = false;
        reachedMax = false;
    }

    private void shootTargeting() {
        if (Math.abs(limelight.getAdjustedTx()) > 0.5 && setpoint == null) {
            pidLimelight.updatePID();
            double output = -pidLimelight.getOutput() + MIN_OUTPUT * Math.signum(limelight.getAdjustedTx());
            if (output > 0 && getPosition() < MIN_POSITION + 10) {
                requireTurn(true);
            } else if (output > 0 && getPosition() < MAX_POSITION - 15) {
                requireTurn(false);
            } else {
                turretMotor.set(output);
                adjusted = false;
            }

        } else {
            if (!adjusted) {
                mediator.setRobotAngleToTargetOffset(-mediator.robotAngleToTarget() + getPosition() + limelight.getAdjustedTx());
                adjusted = true;
            }
            double position = getPosition();
            if (Math.abs(limelight.getAdjustedTx()) > 1d) {
                setpoint = null;
            }
            if (setpoint == null) {
                setpoint = position + limelight.getAdjustedTx();
            }
            if (setpoint < MIN_POSITION + 5) {
                requireTurn(true);
            } else if (setpoint > MAX_POSITION - 10) {
                requireTurn(false);
            } else {
                requireTurn(null);
                System.out.println("PID TX: " + limelight.getAdjustedTx() + "\t PIDError: " + pidEncoder.getError() +
                        "\t Setpoint: " + setpoint + "\t Pos: " + getPosition() + "\t dist: " + mediator.smoothTargetDistance());
                pidControlloop();
            }
        }
    }

    private void requireTurn(Boolean direction) {
        if (direction == null) {
            SmartDashboard.putBoolean("turnLeftShot", true);
            SmartDashboard.putBoolean("turnRightShot", true);
        } else if (direction) {
            SmartDashboard.putBoolean("turnLeftShot", true);
            SmartDashboard.putBoolean("turnRightShot", false);
        } else {
            SmartDashboard.putBoolean("turnLeftShot", false);
            SmartDashboard.putBoolean("turnRightShot", true);
        }
    }

    private void getLastFrame() {

    }

    public enum TurretState {
        SEEK,
        GYRO,
        WAIT,
        FACE_FRONT,
        WOF;
    }
}


