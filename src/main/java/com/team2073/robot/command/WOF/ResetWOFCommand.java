package com.team2073.robot.command.WOF;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;

public class ResetWOFCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    @Override
    protected void initializeDelegate() {
        appCtx.getWofManipulatorSubsystem().setState(WOFManipulatorSubsystem.WofState.WAIT);
        appCtx.getWofManipulatorSubsystem().setMotionSetpoint(null);
        appCtx.getWofManipulatorSubsystem().resetEncoder();
//        System.out.println("\n \n \n " + "RESET SETPOINT" + "\n \n \n");
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
