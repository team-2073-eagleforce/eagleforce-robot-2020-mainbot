package com.team2073.robot.command.WOF;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;

public class WOFPositionCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private String goal;

    public WOFPositionCommand(String goal){
        this.goal = goal;
    }
    @Override
    protected void executeDelegate() {
        appCtx.getWofManipulatorSubsystem().positionControl(goal);
    }

    @Override
    protected void endDelegate() {
        super.endDelegate();
        appCtx.getWofManipulatorSubsystem().setMotor(0d);
        appCtx.getWofManipulatorSubsystem().setMotionSetpoint(null);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
