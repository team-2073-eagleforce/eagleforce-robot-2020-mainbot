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
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private static final double POT_MAX_VALUE = 0.9315;
    private static final double POT_MIN_VALUE = 0.5485;
    //    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    //private Mediator mediator = Mediator.getInstance();
    private static final double KP_LIMELIGHT = 0.0125; // 0.015
    private static final double KP_ENCODER = 0.0025;
    private static final double acceptableError = 0.05;
    private static final double MIN_POSITION = 0;
    private static final double MAX_POSITION = 238;
    private static final double MIN_OUTPUT = 0.04;
    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Limelight limelight = appCtx.getLimelight();
    private AnalogPotentiometer potentiometer = appCtx.getPotentiometer(); // WARNING: Port path on this is randomly chosen!
    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    private CANEncoder encoder = turretMotor.getEncoder();
    private boolean rotatingClockwise = true;
    private boolean hasZeroed = false;
    private PidfControlLoop pidLimelight = new PidfControlLoop(KP_LIMELIGHT, 1e-5, 0.0008, 0, 0.25);
    private PidfControlLoop pidEncoder = new PidfControlLoop(KP_ENCODER, 0, 0.0, 0.0, 0.15);
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> lowDistanceToRPM = new InterpolatingTreeMap<>();
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> highDistanceToRPM = new InterpolatingTreeMap<>();

    private Double setpoint = null;
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
        limelight.setxOffset(1);
        pidLimelight.setPositionSupplier(() -> limelight.getAdjustedTx());
        pidEncoder.setPositionSupplier(this::getPosition);
////        SmartDashboard.putNumber("KP", 0.01);
//        SmartDashboard.putNumber("Angle", 18d);
//        SmartDashboard.putNumber("Height", 90);
        encoder.setPositionConversionFactor(360 / 30d);
        encoder.setVelocityConversionFactor(6 / 30d);
        initializeMap();
    }

    private void initializeMap() {
//       TEMP DATA! Upon measuring real data, record
        lowDistanceToRPM.put(new InterpolatingDouble(10 * 12d), new InterpolatingDouble(4300d));
        lowDistanceToRPM.put(new InterpolatingDouble(16 * 12d), new InterpolatingDouble(5000d));

        highDistanceToRPM.put(new InterpolatingDouble(10 * 12d), new InterpolatingDouble(4300d));
        highDistanceToRPM.put(new InterpolatingDouble(22 * 12d), new InterpolatingDouble(5800d));
    }

    @Override
    public void onPeriodicAsync() {
        if (!hasZeroed && encoder.getVelocity() <= 0.005) {
            encoder.setPosition(potPosition());
            hasZeroed = true;
        }
//        System.out.println(getPosition());
        seekTarget();
    }

    private void set(double position) {

    }

    private void setMotor(double output) {
        turretMotor.set(output);
    }

    public void updateTargetSighting(double turretAngle, double limelightTx) {
        lastTurretAngle = turretAngle + limelightTx;
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

    public double calcRPMGoal(boolean isLowHeight) {
        if (isLowHeight) {
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


    private void seekTarget() {
        if (limelight.getTv() == 0d) {
//            if(potPosition() > MIN_POSITION && potPosition() < MAX_POSITION && !rotatingClockwise){
            if (getPosition() >= MIN_POSITION && getPosition() <= MAX_POSITION && rotatingClockwise) {
                setMotor(.1);
            } else if (getPosition() > MAX_POSITION) {
                setMotor(-.1);
                rotatingClockwise = false;
            } else if (getPosition() < MIN_POSITION) {
                setMotor(.1);
                rotatingClockwise = true;
            }
        } else {
            if (limelight.getTv() == 0d) {
                setpoint = null;
                pidEncoder.resetAccumulatedError();
            }
            if (Math.abs(limelight.getAdjustedTx()) > 0.5 && setpoint == null) {
                pidLimelight.updatePID();
                double output = -pidLimelight.getOutput() + MIN_OUTPUT * Math.signum(limelight.getAdjustedTx());
//                System.out.println("Limelight Output:" + output);
                setMotor(output);
            } else {
                double position = getPosition();
                if (setpoint == null) {
                    setpoint = position + limelight.getAdjustedTx();
                }
                if (limelight.getAdjustedTx() > 3) {
                    setpoint = null;
                }
                pidEncoder.updateSetPoint(setpoint);
                pidEncoder.updatePID();
                double output = pidEncoder.getOutput() + MIN_OUTPUT * Math.signum(-(position - setpoint));
//                System.out.println("Output: " + output + "\t Setpoint: " + setpoint + "\t pos: " + position + "\t tx" + limelight.getAdjustedTx());
                setMotor(output);
            }
        }
    }
}


