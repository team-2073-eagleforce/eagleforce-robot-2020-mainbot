package com.team2073.robot.subsystem.Elevator;

import com.team2073.common.periodic.AsyncPeriodicRunnable;

public class ElevatorSubsytem implements AsyncPeriodicRunnable {


    @Override
    public void onPeriodicAsync() {

    }

    public enum ElevatorStates {
        BOTTOM(0d),
        TOP(0d);

        private Double height;

        ElevatorStates(Double height) {
            this.height = height;
        }

        public double getValue() {
            return height;
        }
    }
}
