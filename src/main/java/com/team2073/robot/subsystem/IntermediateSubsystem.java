package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;

public class IntermediateSubsystem implements AsyncPeriodicRunnable {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private CANSparkMax bottomMotor = appCtx.getIntermediateMotor();
    private CANEncoder encoder = bottomMotor.getEncoder();
    private CANPIDController pid = bottomMotor.getPIDController();
    private TalonSRX topMotor = appCtx.getBagMotor();
    private IntermediateState state = IntermediateState.IDLE;
    private double MAX_VELOCITY = 11000/10d;

    public IntermediateSubsystem() {
        autoRegisterWithPeriodicRunner();
        topMotor.setInverted(true);
        encoder.setVelocityConversionFactor(1/10d);
        encoder.setPositionConversionFactor(1/10d);
        pid.setFeedbackDevice(encoder);
        pid.setFF(1/ MAX_VELOCITY);
        pid.setP(0.0025);
        pid.setD(0.00001);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getBottomRPM(), state.getTopPercent());
//        System.out.println("Velocity: " + bottomMotor.getEncoder().getVelocity() + " \t Output: " + bottomMotor.getAppliedOutput());
    }

    private void setPower(Double bottomRPM, Double topPercent) {
        if(state != IntermediateState.WAIT_FOR_WOF) {
//            pid.setReference(bottomRPM, ControlType.kVelocity);
            bottomMotor.set(bottomRPM/MAX_VELOCITY);
            topMotor.set(ControlMode.PercentOutput, topPercent);
        }else{
            bottomMotor.set(0);
        }
    }

    public void set(IntermediateState goalState) {
        state = goalState;
    }

    public enum IntermediateState {
        SHOOT(4000d, 1),
        IDLE(-200d, 0),
        STOP(0d, 0),
        DISABLED(0d, 0),
        WAIT_FOR_WOF(0d,0d);

        private double topPercent;
        private double bottomRPM;

        IntermediateState(double bottomRPM, double topPercent) {
            this.bottomRPM = bottomRPM;
            this.topPercent = topPercent;
        }

        public double getBottomRPM() {
            return bottomRPM;
        }

        public double getTopPercent() {
            return topPercent;
        }
    }
}



