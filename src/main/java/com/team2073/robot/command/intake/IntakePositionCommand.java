package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.drive.Constants;

public class IntakePositionCommand extends AbstractLoggingCommand {
    private IntakeSubsystem intake = ApplicationContext.getInstance().getIntakeSubsystem();
    private final IntakeSubsystem.IntakePositionState position;

    public IntakePositionCommand(IntakeSubsystem.IntakePositionState position) {
        this.position = position;
    }

    @Override
    protected void initializeDelegate() {
        intake.setPosition(position);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
