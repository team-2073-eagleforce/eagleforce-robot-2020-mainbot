package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private boolean pistonsExtended = false;

    private CANSparkMax intakeMotor = appCtx.getIntakeMotor();
    private Solenoid pistonLeft = appCtx.getIntakeSolenoidLeft();
    private Solenoid pistonRight = appCtx.getIntakeSolenoidRight();
    private IntakeState state = IntakeState.DISABLED;

    public IntakeSubsystem(){
        intakeMotor.setOpenLoopRampRate(1);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getPercent());
        switch(state){
            case DISABLED:
                togglePistons(false);
                break;

            case STOWED:
                togglePistons(false);
                break;
            case INTAKE_OUT:
                togglePistons(true);
                break;
        }

    }

    public void setPower(Double percent) {
        intakeMotor.set(percent);
    }
    public void set(IntakeState goalState) {
        state = goalState;
    }
    private void togglePistons(boolean value) {
        pistonLeft.set(value);
        pistonRight.set(value);
        pistonsExtended = value;
    }

    public enum IntakeState {
        STOWED(0d),
        INTAKE(1d),
        OUTTAKE(-.9),
        STOP(0d),
        INTAKE_OUT(0d),
        DISABLED(0d);

        private Double percent;
        IntakeState(Double percent) {
            this.percent = percent;
        }
        public Double getPercent(){
            return percent;
        }
    }
}

