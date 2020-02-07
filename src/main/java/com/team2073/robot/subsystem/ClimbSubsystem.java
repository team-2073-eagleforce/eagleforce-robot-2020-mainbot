package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Solenoid;

public class ClimbSubsystem implements AsyncPeriodicRunnable {
    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private Solenoid solenoidOne = appCtx.getClimbSolenoid1();
    private ClimbState state = ClimbState.INACTIVE;

    @Override
    public void onPeriodicAsync() {
        switch(state){
            case ACTIVE:
                toggleSolenoid(true);
                break;

            case INACTIVE:
                toggleSolenoid(false);
                break;
        }

    }

    private void toggleSolenoid(boolean value) {
        solenoidOne.set(value);
    }

    public enum ClimbState {
        ACTIVE(1d),
        INACTIVE(0d);
        private Double percent;

        ClimbState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }

    }


}
