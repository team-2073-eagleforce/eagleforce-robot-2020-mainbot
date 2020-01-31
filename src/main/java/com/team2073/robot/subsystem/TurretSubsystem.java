package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.subsys.ExampleAppConstants;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Servo;

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();

    private TalonFX turretMotor = appCtx.getTurretMotor();
    private Limelight limelight = appCtx.getLimelight();
    private AnalogPotentiometer potentiometer = appCtx.getPotentiometer(); // WARNING: Port path on this is randomly chosen!
    private Servo servo = appCtx.getServo(); // WARNING: Channel is randomly chosen!

    private static final double POT_MAX_VALUE = .51; // This value needs to be tuned
    private static final double POT_MIN_VALUE = .57; // This value needs to be tuned
    private static final double KP = 0.01;
    private static final double acceptableError = 0.05;

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

    @Override
    public void onPeriodicAsync() {

    }

    private void set(double position) {

    }

    public void set(HoodState state) {
        servo.setAngle(state.percent);
    }

    private void setMotor(double output) {
        turretMotor.set(ControlMode.PercentOutput, output);
    }

    public void centerToTarget() {
        double turretAdjust = 0;
        double tx = limelight.getTx();

        if (tx > 1.0) {
            turretAdjust = KP * tx - acceptableError;
        } else if (tx < 1.0) {
            turretAdjust = KP * tx + acceptableError;
        }

        setMotor(turretAdjust);
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

}
