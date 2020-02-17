package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class IntakeRollerCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private final IntakeSubsystem intake = appCtx.getIntakeSubsystem();

    @Override
    protected void initializeDelegate() {
        intake.setRollerState(IntakeSubsystem.IntakeRollerState.INTAKE);
    }

    @Override
    protected void endDelegate() {
        intake.setRollerState(IntakeSubsystem.IntakeRollerState.STOP);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
