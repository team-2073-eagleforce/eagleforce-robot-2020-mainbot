package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;

public class IntakeOutCommand extends AbstractLoggingCommand {
    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
