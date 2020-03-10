package com.team2073.robot.command.shooter;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.Mediator;
import edu.wpi.first.wpilibj.command.Command;

public class ShooterCommand extends AbstractLoggingCommand {
    private Mediator mediator = Mediator.getInstance();
    private final Mediator.RobotState state = Mediator.RobotState.SHOOTING;

    @Override
    protected void initializeDelegate() {
        mediator.setDesiredState(state);
    }

    @Override
    protected void executeDelegate() {
        mediator.setDesiredState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
