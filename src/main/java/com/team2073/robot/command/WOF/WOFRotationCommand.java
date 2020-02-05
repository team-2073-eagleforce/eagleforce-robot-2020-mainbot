package com.team2073.robot.command.WOF;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;

public class WOFRotationCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    @Override
    protected void executeDelegate() {
        appCtx.getWofManipulatorSubsystem().rotationControl();
    }

    @Override
    protected void endDelegate() {
        appCtx.getWofManipulatorSubsystem().setMotor(0d);
        appCtx.getWofManipulatorSubsystem().setMotionSetpoint(null);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
