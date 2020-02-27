package com.team2073.robot.climb;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.ClimbSubsystem;
import com.team2073.robot.subsystem.HopperSubsystem;

public class HooksEngageCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getClimbSubsystem().setState(ClimbSubsystem.ClimbState.HOOKS);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
