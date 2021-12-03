package com.team2073.robot.command.auton.atHomeChallenge;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Barrel extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();

    public Barrel(){
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BARREL_LOOP_1.getTraj(), drive));
//            addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BARREL_LOOP_2.getTraj(), drive));
//            addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.BARREL_LOOP_3.getTraj(), drive));
        addSequential(new StopDriveCommand());
    }
}