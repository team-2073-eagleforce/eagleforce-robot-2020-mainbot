package com.team2073.robot.command.hopper;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.HopperSubsystem;

public class HopperReverseCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private HopperSubsystem hopper = appCtx.getHopperSubsystem();

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }

    @Override
    protected void executeDelegate() {
//        hopper.setState(HopperSubsystem.HopperState.REVERSE);
    }

    @Override
    protected void endDelegate() {
        hopper.setState(HopperSubsystem.HopperState.STOP);
    }
}