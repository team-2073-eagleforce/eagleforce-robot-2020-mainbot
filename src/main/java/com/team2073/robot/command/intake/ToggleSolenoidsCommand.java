package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class ToggleSolenoidsCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    @Override
    protected void initializeDelegate() {
        appCtx.getIntakeSubsystem().togglePistons(false);
    }

    @Override
    protected void executeDelegate() {
    }

    @Override
    protected void endDelegate() {
        appCtx.getIntakeSubsystem().togglePistons(true);
        appCtx.getIntakeSubsystem().set(IntakeSubsystem.IntakeState.Stowed);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
