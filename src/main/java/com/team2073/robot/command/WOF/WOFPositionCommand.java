package com.team2073.robot.command.WOF;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;

public class WOFPositionCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getWofManipulatorSubsystem().setState(WOFManipulatorSubsystem.WofState.POSITION);
    }

    @Override
    protected void endDelegate() {

    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
