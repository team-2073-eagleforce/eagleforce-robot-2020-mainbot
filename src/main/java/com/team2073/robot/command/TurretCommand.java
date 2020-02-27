package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.TurretSubsystem;
import com.team2073.robot.subsystem.TurretSubsystem.TurretState;

public class TurretCommand extends AbstractLoggingCommand {
    private TurretState state;

    public TurretCommand(TurretState state) {
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getTurretSubsystem().setState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
