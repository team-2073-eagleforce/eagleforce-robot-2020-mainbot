package com.team2073.robot.command.auton;

import com.team2073.common.util.CommandUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.AutonTurretCommand;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.TurretCommand;
import com.team2073.robot.command.drive.AutonSelector;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.command.hopper.HopperIdleCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.command.shooter.ShooterCommand;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.TurretSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class TopSide10Ball extends CommandGroup {
	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private DriveSubsystem drive = appCtx.getDriveSubsystem();

	public TopSide10Ball(){
		addParallel(new HopperIdleCommand());
		addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
		addParallel(new IntakeRollerCommand(), 15);
		addParallel(new AutonTurretCommand(215));
		addParallel(new TurretCommand(TurretSubsystem.TurretState.AUTO));
		addParallel(CommandUtil.waitBefore(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.STOW), 3.5));
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.PICK_FIRST_2.getTraj(), drive));
		addParallel(CommandUtil.waitBefore(new MediatorCommand(Mediator.RobotState.PREP_SHOT), 1));
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.SHOOT_FIRST_FIVE.getTraj(), drive));
		addSequential(new StopDriveCommand());
		addSequential(new WaitCommand(1));
		addSequential(new ShooterCommand(), 3);
		addSequential(new MediatorCommand(Mediator.RobotState.STOW));
		addSequential(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
		addParallel(new HopperIdleCommand());
		addParallel(new AutonTurretCommand(210));
		addParallel(new TurretCommand(TurretSubsystem.TurretState.AUTO));
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.TRENCH_RUN.getTraj(), drive));
		addParallel(CommandUtil.waitBefore(new MediatorCommand(Mediator.RobotState.PREP_SHOT), 1.5));
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.TRENCH_RUN_RETURN.getTraj(), drive));
		addSequential(new StopDriveCommand());
		addSequential(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.STOW));
		addSequential(new ShooterCommand(), 3);
		addSequential(new MediatorCommand(Mediator.RobotState.STOW));

	}

}
