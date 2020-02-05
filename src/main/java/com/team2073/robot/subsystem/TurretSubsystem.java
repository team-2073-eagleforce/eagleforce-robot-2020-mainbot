package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.subsys.ExampleAppConstants;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();

//    private CANSparkMax turretMotor = appCtx.getTurretMotor();
    private Limelight limelight = appCtx.getLimelight();
    private AnalogPotentiometer potentiometer = appCtx.getPotentiometer(); // WARNING: Port path on this is randomly chosen!
    private Servo servo = appCtx.getServo(); // WARNING: Channel is randomly chosen!

    private CANSparkMax turretMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final double POT_MAX_VALUE = .51; // This value needs to be tuned
    private static final double POT_MIN_VALUE = .57; // This value needs to be tuned
    private static final double KP = 0.015;
    private static final double acceptableError = 0.05;
    private static final double MIN_POSITION = 0;
    private static final double MAX_POSITION = 60;
    private static final double MIN_OUTPUT = 0.058;

    private boolean rotatingClockwise = true;
    private PidfControlLoop pid = new PidfControlLoop(KP, 1e-5, 0.0008, 0, 0.25);

    /*
    Use limelight to turn to center the image
    Zoom in to stabilize image
    Calculate distance using limelight and lidar
    Compensate for elevator height
        - Calc RPM goal for Shooter based on distance
    Zero relative to robot
    Always face shot wall by using gyro
    Determine when to move hood
    Prevent wires from doing the wrap

     */

    // Positive Output = Turret turns clockwise

    public TurretSubsystem(){
        turretMotor.getEncoder().setPosition(0);
        autoRegisterWithPeriodicRunner(10);
        turretMotor.setInverted(true);
        turretMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        pid.setPositionSupplier(()->limelight.getTx()+1.7d);
//        SmartDashboard.putNumber("KP", 0.01);
        SmartDashboard.putNumber("Angle", 18d);
        SmartDashboard.putNumber("Height", 90);
    }

    @Override
    public void onPeriodicAsync() {
//        centerToTarget();
//        setMotor(0.1);
//        pid.updatePID();
//        double output =-pid.getOutput() + MIN_OUTPUT * Math.signum(limelight.getTx());
//        setMotor(output);
//
//        System.out.println("Output:" + output + "\t Tx: " + limelight.getTx());*/
        seekTarget();

        servo.set(96);
//        System.out.println(getPosition());
//        double height = SmartDashboard.getNumber("Height", 90d);
//        double angle = SmartDashboard.getNumber("Angle", 18d);
//        System.out.println(limelight.getHighDistance(height, angle));*/
        //seekTarget();
    }

    private void set(double position) {

    }

    public void set(HoodState state) {
        servo.setAngle(state.percent);
    }

    private void setMotor(double output) {
        turretMotor.set(output);
    }

    public void centerToTarget() {
        double turretAdjust = 0;
        double tx = limelight.getTx();

        if (tx > acceptableError) {
//            turretAdjust = KP * (tx - acceptableError);
            turretAdjust = KP * tx;
            turretAdjust += MIN_OUTPUT;
        } else if (tx < -acceptableError) {
//            turretAdjust = KP * (tx + acceptableError);
            turretAdjust = KP * tx;
            turretAdjust -= MIN_OUTPUT;
        } else {
            turretAdjust = 0;
        }

        setMotor(turretAdjust);
        System.out.println("Turret Adj: " + turretAdjust);
    }

    public double calcRPMGoal() {
        return 0;
    }

    public enum HoodState {
        RETRACTED(0.0),
        EXTENDED(180.0);

        private Double percent;
        HoodState(Double degree) {
            this.percent = degree;
        }
        public Double getPercent(){
            return percent;
        }
    }
    private double potPosition() {
        return (potentiometer.get() - POT_MIN_VALUE) * (MAX_POSITION - MIN_POSITION) / (POT_MAX_VALUE - POT_MIN_VALUE) + MIN_POSITION;
    }

    private void zeroEncoder(){
        turretMotor.getEncoder().setPosition(0);
    }

    private double getPosition() {
        return (turretMotor.getEncoder().getPosition() * 360)/30;
    }


    private void seekTarget() {
        if (limelight.getTv() == 0d) {
//            if(potPosition() > MIN_POSITION && potPosition() < MAX_POSITION && !rotatingClockwise){
            if(getPosition() >= MIN_POSITION && getPosition() <= MAX_POSITION && rotatingClockwise){
                setMotor(.1);
            }else if(getPosition() > MAX_POSITION){
                setMotor(-.1);
                rotatingClockwise = false;
            }else if(getPosition() < MIN_POSITION){
                setMotor(.1);
                rotatingClockwise = true;
            }
        }else{
            pid.updatePID();
            double output =-pid.getOutput() + MIN_OUTPUT * Math.signum(limelight.getTx() + 1.7d);
            setMotor(output);
        }
    }

}
