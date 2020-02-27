package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.Mediator;

public class MediatorCommand extends AbstractLoggingCommand {
    private Mediator mediator = Mediator.getInstance();
    private final Mediator.RobotState state;

    public MediatorCommand(Mediator.RobotState state){
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        mediator.setDesiredState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
