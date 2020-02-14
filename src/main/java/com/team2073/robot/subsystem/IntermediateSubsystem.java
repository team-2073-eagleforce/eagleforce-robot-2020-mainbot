package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;

public class IntermediateSubsystem implements AsyncPeriodicRunnable {
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private CANSparkMax bottomMotor = applicationContext.getIntermediateMotor();
    private TalonSRX topMotor = applicationContext.getBagMotor();
    private IntermediateState state = IntermediateState.STOP;

    public IntermediateSubsystem() {
        autoRegisterWithPeriodicRunner();
        topMotor.setInverted(true);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getBottomPercent(), state.getTopPercent());
    }

    private void setPower(Double bottomPercent, Double topPercent) {
        if(state != IntermediateState.WAIT_FOR_WOF) {
            bottomMotor.set(bottomPercent);
            topMotor.set(ControlMode.PercentOutput, topPercent);
        }else{
            bottomMotor.set(0);
        }
    }

    public void set(IntermediateState goalState) {
        state = goalState;
    }

    public enum IntermediateState {
        SHOOT(1d, .9),
        IDLE(-.05d, 0),
        STOP(0d, 0),
        DISABLED(0d, 0),
        WAIT_FOR_WOF(0d,0d);

        private double topPercent;
        private double bottomPercent;

        IntermediateState(double bottomPercent, double topPercent) {
            this.bottomPercent = bottomPercent;
            this.topPercent = topPercent;
        }

        public double getBottomPercent() {
            return bottomPercent;
        }

        public double getTopPercent() {
            return topPercent;
        }
    }
}



