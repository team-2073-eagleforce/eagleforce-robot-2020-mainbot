package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Solenoid;

public class ClimbSubsystem implements PeriodicRunnable {
    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private Solenoid pistonSolenoid = appCtx.getClimbPistonSolenoid();
    private Solenoid ptoSolenoid = appCtx.getClimbDriveSolenoid();
    private ClimbState state = ClimbState.INACTIVE;

    // flip hooks - button
    // engage PTO - (2) button

    public ClimbSubsystem(){
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodic() {
        switch (state) {
            case PTO:
                togglePTO(true);
                break;

            case HOOKS:
                togglePistons(true);
                break;

            case INACTIVE:
                togglePistons(false);
                togglePTO(false);
                break;
        }
    }

    private void togglePistons(boolean value) {
        pistonSolenoid.set(value);
    }

    private void togglePTO(boolean value) {
        ptoSolenoid.set(value);
    }

    public void setState (ClimbState state){
        this.state = state;
    }

    public enum ClimbState {
        HOOKS(1d),
        PTO(1d),
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


