package com.team2073.robot.command.WOF;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;

public class WOFRotationCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();


    @Override
    protected void initializeDelegate() {
        appCtx.getWofManipulatorSubsystem().setState(WOFManipulatorSubsystem.WofState.ROTATION);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
