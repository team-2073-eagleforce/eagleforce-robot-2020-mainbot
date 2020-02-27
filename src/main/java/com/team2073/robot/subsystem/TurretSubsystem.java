package com.team2073.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.pathfollowing.math.InterpolatingDouble;
import com.team2073.common.pathfollowing.math.InterpolatingTreeMap;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import com.team2073.robot.Limelight.Pipeline;
import com.team2073.robot.Mediator;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.RobotState;

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private static final double POT_MAX_VALUE = 0.9315;
    private static final double POT_MIN_VALUE = 0.5485;
    //    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    //private Mediator mediator = Mediator.getInstance();
    private static final double KP_LIMELIGHT = 0.0125; // 0.015
    private static final double KP_ENCODER = 0.0033;
    private static final double acceptableError = 0.05;
    private static final double MIN_POSITION = 0;
    private static final double MAX_POSITION = 238;
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
    private boolean deadZone = false;

    private Double setpoint = null;
    private Mediator mediator;
    private TurretState state = TurretState.WAIT;
    private double autoSetpoint = 0;
    /*
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
        if (!hasZeroed && encoder.getVelocity() <= 0.005) {
            encoder.setPosition(potPosition());
            hasZeroed = true;
        }
//        System.out.println("Turret: " + getPosition());
        if (limelight.getTv() == 0d) {
            setpoint = null;
            pidEncoder.resetAccumulatedError();
        }

        switch (state) {
            case SEEK:
                if (limelight.getTv() == 0) {
                    seekTarget();
                } else {
                    shootTargeting();
                }
                break;
            case AUTO:
                    if(getPosition() < autoSetpoint - 3){
                        setMotor(.25);
                    }else if(getPosition() > autoSetpoint + 3){
                        setMotor(-.25);
                    }else{
                        setMotor(0);
                    }
                break;
            case WAIT:
//                NO-OP
                break;
            case COUNTERSPIN:
//                TODO: backdrive whatever gyro does to remain pointing in the same direction
                break;
            case WOF:
                setpoint = WOF_POSITION;
                pidEncoder.updateSetPoint(setpoint);
                pidEncoder.updatePID();
                double output = pidEncoder.getOutput() + MIN_OUTPUT * Math.signum(-(getPosition() - setpoint));
                setMotor(output);
                break;
        }
        if(mediator == null){
            mediator = Mediator.getInstance();
        }
        double distance = mediator.targetDistance();
        limelight.setCurrentPipeline(calculatePipeline(distance));
    }

    private void setMotor(double output) {
        turretMotor.set(output);
    }

    public void updateTargetSighting(double turretAngle, double limelightTx) {
        lastTurretAngle = turretAngle + limelightTx;
    }

    public void setAutoSetpoint(double autoSetpoint) {
        this.autoSetpoint = autoSetpoint;
    }

    public double predictTurretAngle() {
        return lastTurretAngle;
    }

    public void centerToTarget() {
        double turretAdjust = 0;
        double tx = limelight.getTx();

        if (tx > acceptableError) {
//            turretAdjust = KP * (tx - acceptableError);
            turretAdjust = KP_LIMELIGHT * tx;
            turretAdjust += MIN_OUTPUT;
        } else if (tx < -acceptableError) {
//            turretAdjust = KP * (tx + acceptableError);
            turretAdjust = KP_LIMELIGHT * tx;
            turretAdjust -= MIN_OUTPUT;
        } else {
            turretAdjust = 0;
        }

        setMotor(turretAdjust);
//        System.out.println("Turret Adj: " + turretAdjust);
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
    private void setAutoSeekPosition(double position){
        this.autoSetpoint = position;
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
                setMotor(.25);
            } else if (getPosition() > MAX_POSITION - 15) {
                setMotor(-.25);
                rotatingClockwise = false;
            } else if (getPosition() < MIN_POSITION + 15) {
                setMotor(.25);
                rotatingClockwise = true;
            }
        }
    }

    private void shootTargeting() {
        if (Math.abs(limelight.getAdjustedTx()) > 0.5 && setpoint == null) {
            pidLimelight.updatePID();
            double output = -pidLimelight.getOutput() + MIN_OUTPUT * Math.signum(limelight.getAdjustedTx());
//                System.out.println("Limelight Output:" + output);
            setMotor(output);
        } else {
            double position = getPosition();
            if (limelight.getAdjustedTx() > 2) {
                setpoint = null;
            }
            if (setpoint == null) {
                setpoint = position + limelight.getAdjustedTx();
            }
            pidEncoder.updateSetPoint(setpoint);
            pidEncoder.updatePID();
            double output = pidEncoder.getOutput() + MIN_OUTPUT * Math.signum(-(position - setpoint));
//                System.out.println("Output: " + output + "\t Setpoint: " + setpoint + "\t pos: " + position + "\t tx" + limelight.getAdjustedTx());
            setMotor(output);
        }
    }

    public enum TurretState {
        SEEK,
        WAIT,
        COUNTERSPIN,
        WOF,
        AUTO;
    }
}


