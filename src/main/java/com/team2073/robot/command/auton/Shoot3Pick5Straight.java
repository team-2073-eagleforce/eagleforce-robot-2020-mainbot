package com.team2073.robot.command.auton;

import com.team2073.common.util.CommandUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.TurretCommand;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.command.hopper.HopperStateCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.command.shooter.ShooterCommand;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.TurretSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Shoot3Pick5Straight extends CommandGroup {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();

    public Shoot3Pick5Straight() {

        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
        addParallel(new HopperStateCommand(HopperSubsystem.HopperState.STOP));
        addParallel(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
        addSequential(new WaitCommand(.5));

        addSequential(new ShooterCommand(), 4);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));
        addParallel(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
        addParallel(new IntakeRollerCommand(), 6);
        addParallel(new HopperStateCommand(HopperSubsystem.HopperState.IDLE));
        addParallel(new TurretCommand(TurretSubsystem.TurretState.GYRO));
        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.DOWN_THE_TRENCH.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addParallel(new HopperStateCommand(HopperSubsystem.HopperState.IDLE));
        addParallel(CommandUtil.waitBefore(new MediatorCommand(Mediator.RobotState.PREP_SHOT), 2));
        addSequential(new WaitCommand(.25));
        addSequential(new RamseteCommand(DriveSubsystem.AutoPathTrench.LEAVE_THE_TRENCH.getTraj(), drive));
        addSequential(new StopDriveCommand());
        addSequential(new WaitCommand(1));
        addSequential(new ShooterCommand(), 4);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));

    }
}
