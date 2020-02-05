package com.team2073.robot.subsystem.drive;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

import static com.team2073.common.util.ConversionUtil.*;

public final class Constants {
    public static final class CheesyDriveConstants {
        public static final double kThrottleDeadband = 0.10;
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

        public static final double trackWidthInches = 24;
        public static final double trackWidthMeters = inchesToMeters(trackWidthInches);
        public static final double wheelDiameterInches = 4;
        public static final double wheelDiameterMeters = inchesToMeters(wheelDiameterInches);

        public static final double distancePerRevInches = wheelDiameterInches * Math.PI;
        public static final double distancePerRevMeters = wheelDiameterMeters * Math.PI;

        public static final boolean kLeftEncoderReversed = false;
        public static final boolean kRightEncoderReversed = true;

        public static final DifferentialDriveKinematics kDriveKinematics =
                new DifferentialDriveKinematics(trackWidthMeters);


        public static final boolean kGyroReversed = true;

        // These are example values only - DO NOT USE THESE FOR YOUR OWN ROBOT!
        // These characterization values MUST be determined either experimentally or theoretically
        // for *your* robot's drive.
        // The Robot Characterization Toolsuite provides a convenient tool for obtaining these
        // values for your robot.
        public static final double ksVolts = 0.22;
        public static final double kvVoltSecondsPerMeter = 1.98;
        public static final double kaVoltSecondsSquaredPerMeter = 0.2;

        // Example value only - as above, this must be tuned for your drive!
        public static final double kPDriveVel = 8.5;
    }

    public static final class AutoConstants {
        public static final double maxSpeedFeetPerSecond = 16;
        public static final double maxAccelerationMetersPerSecondSquared = 16;

        public static final double kMaxSpeedMetersPerSecond = feetToMeters(maxSpeedFeetPerSecond);
        public static final double kMaxAccelerationMetersPerSecondSquared = feetToMeters(maxAccelerationMetersPerSecondSquared);

        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }
}
