package com.team2073.robot.command.auton;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.command.hopper.HopperIdleCommand;
import com.team2073.robot.command.hopper.HopperStopCommand;
import com.team2073.robot.command.intake.IntakeCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.intake.IntakeStopCommand;
import com.team2073.robot.command.shooter.ShooterCommand;
import com.team2073.robot.subsystem.FlywheelSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Trench extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();
    private FlywheelSubsystem shooter = appCtx.getFlywheelSubsystem();

    public Trench() {
        addParallel(new HopperStopCommand());
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
        addParallel(new IntakeCommand());
        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.PICK_FIRST_2.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addParallel(new IntakeStopCommand());
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.STOW));
        addParallel(new HopperIdleCommand());
        addSequential(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
//        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.EXIT_2_BALL.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addSequential(new WaitCommand(1.5));
        addSequential(new ShooterCommand(), 3);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));
//        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.RETURN_2_BALL.getTraj(), drive));
        addSequential(new StopDriveCommand());
    }

}