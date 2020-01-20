package com.team2073.robot.command.drive;

import com.team2073.robot.Robot;
import com.team2073.robot.subsystem.drive.Constants.AutoConstants;
import com.team2073.robot.subsystem.drive.Constants.DriveConstants;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

import java.util.List;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class AutonStarter {
    // The robot's subsystems
    private final DriveSubsystem robotDrive = new DriveSubsystem();

    // The driver's controller
    XboxController m_driverController = new XboxController(1);

    public AutonStarter() {

    }

    public Command getAutonomousCommand() {

        // Create a voltage constraint to ensure we don't accelerate too fast
        DifferentialDriveVoltageConstraint autoVoltageConstraint =
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                DriveConstants.kvVoltSecondsPerMeter,
                                DriveConstants.kaVoltSecondsSquaredPerMeter),
                        DriveConstants.kDriveKinematics,
                        10);

        // Create config for trajectory
        TrajectoryConfig config =
                new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
                        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(DriveConstants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint);

        // An example trajectory to follow.  All units in meters.
        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(

                        new Pose2d(0, 0, new Rotation2d(0)),
                        new Pose2d(1, 1, new Rotation2d(0)),
                        new Pose2d(2, -1, new Rotation2d(0)),
                        new Pose2d(3, 0, new Rotation2d(0))
                ),
                // End 3 meters straight ahead of where we started, facing forward
                // Pass config
                config
        );

        RamseteCommand ramseteCommand = new RamseteCommand(
                exampleTrajectory,
                robotDrive::getPose,
                new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(DriveConstants.ksVolts,
                        DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                DriveConstants.kDriveKinematics,
                robotDrive::getWheelSpeeds,
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                new PIDController(DriveConstants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                robotDrive::tankDriveVolts);

        // Run path following command, then stop at the end.
        return ramseteCommand.andThen(() -> robotDrive.tankDriveVolts(0, 0));
    }
}
