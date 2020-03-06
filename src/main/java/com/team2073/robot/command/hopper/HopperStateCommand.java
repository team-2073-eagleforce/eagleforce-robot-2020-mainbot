package com.team2073.robot.command.hopper;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;

public class HopperStateCommand extends AbstractLoggingCommand {
    private HopperSubsystem hopper = ApplicationContext.getInstance().getHopperSubsystem();
    private final HopperSubsystem.HopperState state;

    public HopperStateCommand(HopperSubsystem.HopperState state) {
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        hopper.setState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
