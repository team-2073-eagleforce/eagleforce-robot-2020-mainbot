package com.team2073.robot.command.auton.atHomeChallenge;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Bounce extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();

    public Bounce(){
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BOUNCE_TO_FIRST_CONE.getTraj(), drive));
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BOUNCE_TO_SECOND_CONE.getTraj(), drive));
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BOUNCE_TO_THIRD_CONE.getTraj(), drive));
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BOUNCE_TO_END.getTraj(), drive));
        addSequential(new StopDriveCommand());
    }
}