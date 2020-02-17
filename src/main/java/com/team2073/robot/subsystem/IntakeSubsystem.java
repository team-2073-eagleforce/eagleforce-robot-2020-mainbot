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
    private Solenoid pistonTop = appCtx.getIntakeSolenoidLeft();
    private Solenoid pistonBottom = appCtx.getIntakeSolenoidRight();
    private IntakePositionState positionState = IntakePositionState.STARTING_CONFIG;
    private IntakeRollerState rollerState = IntakeRollerState.STOP;

    public IntakeSubsystem() {
        autoRegisterWithPeriodicRunner(10);
        intakeMotor.setOpenLoopRampRate(.25);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(rollerState.getPercent());
        switch (positionState) {
            case STARTING_CONFIG:
                togglePistons(false, false);
                break;
            case STOW:
                togglePistons(true, false);
                break;
            case INTAKE_OUT:
                togglePistons(true, true);
                break;
        }


    }

    private void setPower(Double percent) {
        intakeMotor.set(percent);
    }

    public void setRollerState(IntakeRollerState state) {
        this.rollerState = state;
    }

    public void setPosition(IntakePositionState goalState) {
        positionState = goalState;
    }

    private void togglePistons(boolean topOut, boolean bottomOut) {
        pistonTop.set(topOut);
        pistonBottom.set(bottomOut);
    }

    public enum IntakePositionState {
        STARTING_CONFIG,
        INTAKE_OUT,
        STOW,

    }

    public enum IntakeRollerState {
        INTAKE(1d),
        OUTTAKE(-.9),
        STOP(0d),
        DISABLED(0d);

        private Double percent;

        IntakeRollerState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }
}

