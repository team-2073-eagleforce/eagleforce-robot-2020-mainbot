package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class IntakeCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private IntakeSubsystem intakeSubsystem = appCtx.getIntakeSubsystem();

    @Override
    protected void initializeDelegate() {
        intakeSubsystem.setRollerState(IntakeSubsystem.IntakeRollerState.INTAKE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
