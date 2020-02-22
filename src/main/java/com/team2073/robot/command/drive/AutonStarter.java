package com.team2073.robot.command.drive;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.drive.Constants.AutoConstants;
import com.team2073.robot.subsystem.drive.Constants.DriveConstants;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

public class AutonStarter {

    // The robot's subsystems
    private final DriveSubsystem robotDrive = ApplicationContext.getInstance().getDriveSubsystem();


    public Command getAutonomousCommand() {

        DifferentialDriveVoltageConstraint autoVoltageConstraint =
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                DriveConstants.kvVoltSecondsPerMeter,
                                DriveConstants.kaVoltSecondsSquaredPerMeter),
                        DriveConstants.DRIVE_KINEMATICS,
                        12);

        // Create config for trajectory
        TrajectoryConfig config =
                new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
                        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(DriveConstants.DRIVE_KINEMATICS)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint);
        // An example trajectory to follow.  All units in meters.
//        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
//                // Start at the origin facing the +X direction
//                // Pass through these two interior waypoints, making an 's' curve path
//
//                        new Pose2d(0, 0, new Rotation2d(0)),
//                        List.of(
//                                new Translation2d(1, 0)
//                        ),
//                        new Pose2d(3, 0, new Rotation2d(0)),
//                // End 3 meters straight ahead of where we started, facing forward
//                // Pass config
//                config
//        );
        Trajectory shoot_first_fiveTraj = DriveSubsystem.AutoPaths.SHOOT_FIRST_FIVE.getTraj();
        RamseteCommand shoot5Command = new RamseteCommand(
                shoot_first_fiveTraj,
                robotDrive::getPose,
                new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(DriveConstants.ksVolts,
                        DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                DriveConstants.DRIVE_KINEMATICS,
                robotDrive::getWheelSpeeds,
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                robotDrive::tankDriveVolts);
        Trajectory trenchRun = DriveSubsystem.AutoPaths.TRENCH_RUN.getTraj();
        RamseteCommand trenchRunCommand = new RamseteCommand(
                trenchRun,
                robotDrive::getPose,
                new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(DriveConstants.ksVolts,
                        DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                DriveConstants.DRIVE_KINEMATICS,
                robotDrive::getWheelSpeeds,
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                robotDrive::tankDriveVolts);
        Trajectory pick2 = DriveSubsystem.AutoPaths.PICK_FIRST_2.getTraj();
        RamseteCommand pick2Command = new RamseteCommand(
                pick2,
                robotDrive::getPose,
                new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(DriveConstants.ksVolts,
                        DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                DriveConstants.DRIVE_KINEMATICS,
                robotDrive::getWheelSpeeds,
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                robotDrive::tankDriveVolts);

        // Run path following command, then stop at the end.
        return pick2Command.andThen(shoot5Command).andThen(trenchRunCommand).andThen(() -> robotDrive.tankDriveVolts(0, 0));
    }
}
