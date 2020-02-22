package com.team2073.robot.subsystem.drive;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

import static com.team2073.common.util.ConversionUtil.*;

public final class Constants {
    public static final class CheesyDriveConstants {
        public static final double kThrottleDeadband = 0.05;
        public static final double kWheelDeadband = 0.05;

        // These factor determine how fast the wheel traverses the "non linear" sine curve.
        public static final double kHighWheelNonLinearity = 0.65;

        public static final double kHighNegInertiaScalar = 4.0;

        public static final double kHighSensitivity = 0.85;

        public static final double kQuickStopDeadband = 0.5;
        public static final double kQuickStopWeight = 0.1;
        public static final double kQuickStopScalar = 5.0;

    }
    public static final class DriveConstants {

        public static final double TRACK_WIDTH_METERS = 0.74118;
        public static final double TRACK_WIDTH_INCHES = inchesToMeters(TRACK_WIDTH_METERS);
        public static final double WHEEL_DIAMETER_INCHES = 4.025;
        public static final double WHEEL_DIAMETER_METERS = inchesToMeters(WHEEL_DIAMETER_INCHES);
        public static final double GEAR_RATIO = 56/12d;
        public static final double DISTANCE_PER_MOTOR_REV_INCHES = WHEEL_DIAMETER_INCHES * Math.PI / GEAR_RATIO;
        public static final double DISTANCE_PER_MOTOR_REV_METERS = inchesToMeters(DISTANCE_PER_MOTOR_REV_INCHES);

        public static final boolean LEFT_ENCODER_REVERSED = false;
        public static final boolean RIGHT_ENCODER_REVERSED = true;

        public static final DifferentialDriveKinematics DRIVE_KINEMATICS =
                new DifferentialDriveKinematics(TRACK_WIDTH_METERS);


        public static final boolean kGyroReversed = true;

        // These are example values only - DO NOT USE THESE FOR YOUR OWN ROBOT!
        // These characterization values MUST be determined either experimentally or theoretically
        // for *your* robot's drive.
        // The Robot Characterization Toolsuite provides a convenient tool for obtaining these
        // values for your robot.
        public static final double ksVolts = 0.222;
        public static final double kvVoltSecondsPerMeter = 1.81;
        public static final double kaVoltSecondsSquaredPerMeter = 0.433;

        // Example value only - as above, this must be tuned for your drive!
        public static final double kPDriveVel = 2.5;
    }

    public static final class AutoConstants {
        public static final double maxSpeedFeetPerSecond = 14;
        public static final double maxAccelerationMetersPerSecondSquared = 5;

        public static final double kMaxSpeedMetersPerSecond = feetToMeters(maxSpeedFeetPerSecond);
        public static final double kMaxAccelerationMetersPerSecondSquared = feetToMeters(maxAccelerationMetersPerSecondSquared);

        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }
}
