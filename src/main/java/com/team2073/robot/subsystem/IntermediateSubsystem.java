package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;

public class IntermediateSubsystem implements AsyncPeriodicRunnable {
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private CANSparkMax intermediateMotor1 = applicationContext.getIntermediateMotor();
    // private VictorSPX intermediateSlave = applicationContext.get
    private IntermediateState state = IntermediateState.DISABLED;

    public IntermediateSubsystem() {

    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getPercent());
    }

    public void stall(Double percent) {
        intermediateMotor1.set(percent);
    }

    public void setPower(Double percent) {
        intermediateMotor1.set(percent);
    }

    public void set(IntermediateState goalState) {
        state = goalState;
    }

    public enum IntermediateState {
        INTAKE(1d),
        STALL(-.1d),
        STOP(0d),
        DISABLED(0d);

        private double percent;

        IntermediateState(double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }
}



