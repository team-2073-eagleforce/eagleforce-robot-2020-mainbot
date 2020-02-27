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

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private static final double POT_MAX_VALUE = 0.9315;
    private static final double POT_MIN_VALUE = 0.5485;

    private static final double FACE_FRONT_POSITION = 35; // TODO: CHECK then REMOVE and offset MIN/MAX position

    //    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    //private Mediator mediator = Mediator.getInstance();
    private static final double KP_LIMELIGHT = 0.0125; // 0.015
    private static final double KP_ENCODER = 0.0033;
    private static final double acceptableError = 0.05;
    private static final double MIN_POSITION = 0 - FACE_FRONT_POSITION;
    private static final double MAX_POSITION = 238 - FACE_FRONT_POSITION;
    private static final double MIN_OUTPUT = 0.05;

    private static final double WOF_POSITION = 81.714;
    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Limelight limelight = appCtx.getLimelight();
    private AnalogPotentiometer potentiometer = appCtx.getPotentiometer(); // WARNING: Port path on this is randomly chosen!
    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    private CANEncoder encoder = turretMotor.getEncoder();
    private boolean rotatingClockwise = true;
    private boolean hasZeroed = false;
    private PidfControlLoop pidLimelight = new PidfControlLoop(KP_LIMELIGHT, 1e-5, 0.0008, 0, 0.25);
    private PidfControlLoop pidEncoder = new PidfControlLoop(KP_ENCODER, 0, 0.0005, 0.0, 0.15);
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> lowDistanceToRPM = new InterpolatingTreeMap<>();
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> highDistanceToRPM = new InterpolatingTreeMap<>();

    private Double setpoint = null;
    private Mediator mediator;
    private TurretState state = TurretState.WAIT;
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

    // Positive Output = Turret turns clockwise
    private double lastTurretAngle = 0;

    public TurretSubsystem() {
        autoRegisterWithPeriodicRunner(10);
        turretMotor.setInverted(true);
        turretMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        limelight.setxOffset(1.7);
        pidLimelight.setPositionSupplier(() -> limelight.getAdjustedTx());
        pidEncoder.setPositionSupplier(this::getPosition);
        encoder.setPositionConversionFactor(360 / 30d);
        encoder.setVelocityConversionFactor(6 / 30d);
        initializeMap();
    }

    public void setState(TurretState state) {
        this.state = state;
    }

    private void initializeMap() {
//       Recording on 2/17/2020
        lowDistanceToRPM.put(new InterpolatingDouble(189.5d), new InterpolatingDouble(5300d));
        lowDistanceToRPM.put(new InterpolatingDouble(200d), new InterpolatingDouble(5375d));
        lowDistanceToRPM.put(new InterpolatingDouble(214d), new InterpolatingDouble(5450d));
        lowDistanceToRPM.put(new InterpolatingDouble(225d), new InterpolatingDouble(5500d));
        lowDistanceToRPM.put(new InterpolatingDouble(235.4d), new InterpolatingDouble(5545d));
        lowDistanceToRPM.put(new InterpolatingDouble(245.5d), new InterpolatingDouble(5625d));
        lowDistanceToRPM.put(new InterpolatingDouble(263.7d), new InterpolatingDouble(5700d));
        lowDistanceToRPM.put(new InterpolatingDouble(273d), new InterpolatingDouble(5775d));
        lowDistanceToRPM.put(new InterpolatingDouble(285d), new InterpolatingDouble(5850d));
        lowDistanceToRPM.put(new InterpolatingDouble(300d), new InterpolatingDouble(5960d));
        lowDistanceToRPM.put(new InterpolatingDouble(312d), new InterpolatingDouble(6025d));
        lowDistanceToRPM.put(new InterpolatingDouble(325d), new InterpolatingDouble(6065d));
        lowDistanceToRPM.put(new InterpolatingDouble(336d), new InterpolatingDouble(6150d));
        lowDistanceToRPM.put(new InterpolatingDouble(341d), new InterpolatingDouble(6300d));
        lowDistanceToRPM.put(new InterpolatingDouble(359d), new InterpolatingDouble(6450d));
        lowDistanceToRPM.put(new InterpolatingDouble(380d), new InterpolatingDouble(6650d));
        lowDistanceToRPM.put(new InterpolatingDouble(428d), new InterpolatingDouble(7300d));
        lowDistanceToRPM.put(new InterpolatingDouble(475d), new InterpolatingDouble(8300d));

        highDistanceToRPM.put(new InterpolatingDouble(72d), new InterpolatingDouble(6475d));
        highDistanceToRPM.put(new InterpolatingDouble(82d), new InterpolatingDouble(6000d));
        highDistanceToRPM.put(new InterpolatingDouble(97.5d), new InterpolatingDouble(5300d));
        highDistanceToRPM.put(new InterpolatingDouble(110d), new InterpolatingDouble(5000d));
        highDistanceToRPM.put(new InterpolatingDouble(126d), new InterpolatingDouble(4805d));
        highDistanceToRPM.put(new InterpolatingDouble(138.7d), new InterpolatingDouble(4800d));
        highDistanceToRPM.put(new InterpolatingDouble(148.7d), new InterpolatingDouble(4800d));
        highDistanceToRPM.put(new InterpolatingDouble(159.4d), new InterpolatingDouble(4850d));
        highDistanceToRPM.put(new InterpolatingDouble(173.6d), new InterpolatingDouble(4900d));
        highDistanceToRPM.put(new InterpolatingDouble(189.5d), new InterpolatingDouble(4965d));
        highDistanceToRPM.put(new InterpolatingDouble(344d), new InterpolatingDouble(6050d));
        highDistanceToRPM.put(new InterpolatingDouble(361d), new InterpolatingDouble(6150d));
        highDistanceToRPM.put(new InterpolatingDouble(385d), new InterpolatingDouble(6350d));
        highDistanceToRPM.put(new InterpolatingDouble(447d), new InterpolatingDouble(7100d));

    }

    @Override
    public void onPeriodicAsync() {
//        System.out.println("Turret: " + getPosition());

        if (mediator == null) {
            mediator = Mediator.getInstance();
        }

        if (!hasZeroed && encoder.getVelocity() <= 0.005) {
            encoder.setPosition(potPosition());
            hasZeroed = true;
        }

        if (limelight.getTv() == 0d) {
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
                turretMotor.set(0);
                break;
            case FACE_FRONT:
                setpoint = 0d;
                pidControlloop();
                break;
            case SEEK:
                double distance = mediator.targetDistance();
                limelight.setCurrentPipeline(calculatePipeline(distance));
                if (limelight.getTv() == 0) {
                    seekTarget();
                } else {
                    shootTargeting();
                }
                break;
        }

    }

    private void pidControlloop() {
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
        if (limelight.getCurrentPipeline() == Pipeline.CLOSE) {
            if (distance > 156 && limelight.getTx() < 6d) {
                return Pipeline.FAR;
            }
        } else {
            if (distance < 140) {
                return Pipeline.CLOSE;
            }
        }
        return limelight.getCurrentPipeline();
    }

    private void seekTarget() {
        if (limelight.getTv() == 0d) {
            if (getPosition() >= MIN_POSITION + 15 && getPosition() <= MAX_POSITION - 15 && rotatingClockwise) {
                setpoint = MAX_POSITION - 10;
            } else if (getPosition() > MAX_POSITION - 15) {
                setpoint = MIN_POSITION + 10;
                rotatingClockwise = false;
            } else if (getPosition() < MIN_POSITION + 15) {
                setpoint = MAX_POSITION - 10;
                rotatingClockwise = true;
            }
        }
    }

    private void shootTargeting() {
        if (Math.abs(limelight.getAdjustedTx()) > 0.5 && setpoint == null) {
            pidLimelight.updatePID();
            double output = -pidLimelight.getOutput() + MIN_OUTPUT * Math.signum(limelight.getAdjustedTx());
            turretMotor.set(output);
        } else {
//            TODO: THIS MATH NEEDS TO BE SUPER CHECKED
            mediator.setRobotAngleToTargetOffset(mediator.robotAngleToTarget() - getPosition() + limelight.getAdjustedTx());
            double position = getPosition();
            if (limelight.getAdjustedTx() > 2) {
                setpoint = null;
            }
            if (setpoint == null) {
                setpoint = position + limelight.getAdjustedTx();
            }
            pidControlloop();
        }
    }

    public enum TurretState {
        SEEK,
        GYRO,
        WAIT,
        FACE_FRONT,
        WOF;
    }
}


