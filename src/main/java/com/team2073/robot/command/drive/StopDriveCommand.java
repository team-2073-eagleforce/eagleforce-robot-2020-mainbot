package com.team2073.robot.command.drive;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.drive.DriveSubsystem;

public class StopDriveCommand extends AbstractLoggingCommand {

    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getDriveSubsystem().tankDriveVolts(0, 0);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
