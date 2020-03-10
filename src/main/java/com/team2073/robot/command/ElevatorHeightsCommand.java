package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsytem;
import com.team2073.robot.subsystem.ElevatorSubsytem.ElevatorState;

public class ElevatorHeightsCommand  extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ElevatorSubsytem elevatorSubsystem = appCtx.getElevatorSubsystem();
    private ElevatorState state;

    public ElevatorHeightsCommand(ElevatorState state) {
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        elevatorSubsystem.setElevatorState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
