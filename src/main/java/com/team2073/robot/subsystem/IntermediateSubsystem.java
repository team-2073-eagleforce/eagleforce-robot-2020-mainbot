package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;

public class IntermediateSubsystem implements AsyncPeriodicRunnable {
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private CANSparkMax intermediateMotor1 = applicationContext.getIntermediateMotor();
//    private VictorSPX intermediateMotor2 = applicationContext.getBagMotor();
    private IntermediateState state = IntermediateState.STOP;

    public IntermediateSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getBottomPercent(), state.getTopPercent());
    }

    private void setPower(Double bottomPercent, Double topPercent) {
        intermediateMotor1.set(bottomPercent);
//        intermediateMotor2.set(ControlMode.PercentOutput, topPercent);
    }

    public void set(IntermediateState goalState) {
        state = goalState;
    }

    public enum IntermediateState {
        SHOOT(1d, .9),
        IDLE(-.05d, 0),
        STOP(0d, 0),
        DISABLED(0d, 0);

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



