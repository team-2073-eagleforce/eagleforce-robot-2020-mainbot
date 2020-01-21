package com.team2073.robot.command.hopper;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.HopperSubsystem;

public class HopperStopCommand extends AbstractLoggingCommand {

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getHopperSubsystem().set(HopperSubsystem.HopperState.STOP);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
