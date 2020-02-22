package com.team2073.robot.command.auton;

import com.team2073.common.util.CommandUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.drive.AutonSelector;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.hopper.HopperIdleCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.subsystem.IntakeSubsystem;
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
		addParallel(CommandUtil.waitBefore(new IntakeRollerCommand(), 2), 4);
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.PICK_FIRST_2.getTraj(), drive));
		addParallel(CommandUtil.waitBefore(new MediatorCommand(Mediator.RobotState.PREP_SHOT), .75));
		addSequential(new WaitCommand(.1));
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.SHOOT_FIRST_FIVE.getTraj(), drive));
		addSequential(new WaitCommand(.5));
		addParallel(new MediatorCommand(Mediator.RobotState.SHOOTING));
		addSequential(new WaitCommand(2));
		addParallel(new HopperIdleCommand());
		addParallel(CommandUtil.waitBefore(new IntakeRollerCommand(), 2), 4);
		addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.TRENCH_RUN.getTraj(), drive));
	}

}
