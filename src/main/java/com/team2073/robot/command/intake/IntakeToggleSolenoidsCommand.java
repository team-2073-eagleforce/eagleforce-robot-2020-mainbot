package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class IntakeToggleSolenoidsCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    @Override
    protected void initializeDelegate() {
        appCtx.getIntakeSubsystem().set(IntakeSubsystem.IntakeState.INTAKE_OUT);
    }

    @Override
    protected void executeDelegate() {
    }

    @Override
    protected void endDelegate() {
        appCtx.getIntakeSubsystem().set(IntakeSubsystem.IntakeState.STOWED);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
