package com.team2073.robot.command.hopper;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.HopperSubsystem;

public class HopperToggleCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void executeDelegate() {
        appCtx.getHopperSubsystem().setState(HopperSubsystem.HopperState.IDLE);
    }

    @Override
    protected void initializeDelegate() {
//        appCtx.getHopperSubsystem().setState(HopperSubsystem.HopperState.IDLE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }

    @Override
    protected void endDelegate() {
//        appCtx.getHopperSubsystem().setState(HopperSubsystem.HopperState.REVERSE);
        appCtx.getHopperSubsystem().setState(HopperSubsystem.HopperState.STOP);
    }
}
