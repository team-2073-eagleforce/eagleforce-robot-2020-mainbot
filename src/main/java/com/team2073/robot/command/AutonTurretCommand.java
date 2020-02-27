package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;

public class AutonTurretCommand extends AbstractLoggingCommand {
    private double setpoint;

    public AutonTurretCommand(double setpoint) {
        this.setpoint = setpoint;
    }

    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getTurretSubsystem().setAutoSetpoint(setpoint);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
