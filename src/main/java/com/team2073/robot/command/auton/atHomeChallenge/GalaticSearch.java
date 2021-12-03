package com.team2073.robot.command.auton.atHomeChallenge;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.command.hopper.HopperStateCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GalaticSearch extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();

    public GalaticSearch(){
        addParallel(new IntakeRollerCommand());
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
        addParallel(new HopperStateCommand(HopperSubsystem.HopperState.STOP), .25);
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.GALACTIC_SEARCH_lEG_1.getTraj(), drive));
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.GALACTIC_SEARCH_LEG_2.getTraj(), drive));
        addSequential(new RamseteCommand(DriveSubsystem.atHomePaths.GALACTIC_SEARCH_LEG_3.getTraj(), drive));
        addSequential(new StopDriveCommand());
    }
}
