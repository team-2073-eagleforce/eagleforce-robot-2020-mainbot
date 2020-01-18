package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class IntakeCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getIntakeSubsystem().set(IntakeSubsystem.IntakeState.Intake);

    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
