package com.team2073.robot.command.auton;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.TurretCommand;
import com.team2073.robot.command.drive.RamseteCommand;
import com.team2073.robot.command.drive.StopDriveCommand;
import com.team2073.robot.command.hopper.HopperStopCommand;
import com.team2073.robot.command.intake.IntakeCommand;
import com.team2073.robot.command.intake.IntakePositionCommand;
import com.team2073.robot.command.shooter.ShooterCommand;
import com.team2073.robot.subsystem.FlywheelSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.TurretSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Shoot3Drive extends CommandGroup {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private DriveSubsystem drive = appCtx.getDriveSubsystem();
    private FlywheelSubsystem shooter = appCtx.getFlywheelSubsystem();

    public Shoot3Drive(){
        addSequential(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
        addSequential(new WaitCommand(4.5));
        addSequential(new ShooterCommand(), 3);
        addParallel(new MediatorCommand(Mediator.RobotState.STOW));
        addParallel(new HopperStopCommand());
        addSequential(new RamseteCommand(DriveSubsystem.AutoPaths.DRIVE_OFF_LINE.getTraj(),drive));
        addSequential(new StopDriveCommand());
    }
}
