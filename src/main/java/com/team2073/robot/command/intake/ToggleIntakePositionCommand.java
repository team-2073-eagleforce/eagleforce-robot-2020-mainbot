package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class ToggleIntakePositionCommand extends AbstractLoggingCommand {
    private IntakeSubsystem intake = ApplicationContext.getInstance().getIntakeSubsystem();

    @Override
    protected void initializeDelegate() {
        intake.setPosition(IntakeSubsystem.IntakePositionState.INTAKE_OUT);
    }

    @Override
    protected void endDelegate() {
        intake.setPosition(IntakeSubsystem.IntakePositionState.STOW);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
