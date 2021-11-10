package com.team2073.robot.command.auton;

import com.team2073.common.util.CommandUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.TurretCommand;
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
import com.team2073.robot.subsystem.TurretSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Shoot3Pick3 extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();
    private FlywheelSubsystem shooter = appCtx.getFlywheelSubsystem();

    public Shoot3Pick3() {
        addSequential(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
        addSequential(new WaitCommand(1.5));
        addSequential(new ShooterCommand(), 2);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));
        addParallel(new HopperStopCommand());
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
        addParallel(new IntakeCommand());
        addParallel(new TurretCommand(TurretSubsystem.TurretState.GYRO));
//        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.PICK_First_3.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addParallel(new IntakeStopCommand());
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.STOW));
        addParallel(new HopperIdleCommand());
        addSequential(CommandUtil.waitBefore(new MediatorCommand(Mediator.RobotState.PREP_SHOT), .5));
//        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.EXIT_3_BALL.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addSequential(new WaitCommand(1));
        addSequential(new ShooterCommand(), 2);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));
//        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.RETURN_3_BALL.getTraj(), drive));
        addSequential(new StopDriveCommand());
    }
}