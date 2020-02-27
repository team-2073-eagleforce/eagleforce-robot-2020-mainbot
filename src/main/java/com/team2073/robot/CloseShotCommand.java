package com.team2073.robot;

import com.team2073.common.command.AbstractLoggingCommand;

public class CloseShotCommand extends AbstractLoggingCommand {
    private boolean isClose;
    public CloseShotCommand(boolean isCLose){
        this.isClose = isCLose;
    }
    @Override
    protected void initializeDelegate() {
        Mediator.getInstance().setCloseShot(isClose);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
