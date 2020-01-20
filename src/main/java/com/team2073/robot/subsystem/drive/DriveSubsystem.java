package com.team2073.robot.subsystem.drive;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

import static com.team2073.robot.subsystem.drive.Constants.DriveConstants.*;


public class DriveSubsystem {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private CANSparkMax leftMaster = appCtx.getLeftMaster();
    private CANSparkMax rightMaster = appCtx.getRightMaster();
    private CANSparkMax leftSlave1 = appCtx.getLeftSlave1();
    private CANSparkMax leftSlave2 = appCtx.getLeftSlave2();
    private CANSparkMax rightSlave1 = appCtx.getRightSlave1();
    private CANSparkMax rightSlave2 = appCtx.getRightSlave2();


    private CANEncoder leftEncoder = leftMaster.getEncoder();
    private CANEncoder rightEncoder = rightMaster.getEncoder();
    // The gyro sensor
    private final PigeonIMU gyro = new PigeonIMU(1);

    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;

    /**
     * Creates a new DriveSubsystem.
     */
    public DriveSubsystem() {
        // Sets the distance per pulse for the encoders

//        TODO:  MATH TO MAKE SURE UNITS ARE PROPER
        leftEncoder.setPositionConversionFactor(distancePerRevMeters);
        rightEncoder.setPositionConversionFactor(distancePerRevMeters);
        leftEncoder.setVelocityConversionFactor(distancePerRevMeters);
        leftEncoder.setVelocityConversionFactor(distancePerRevMeters);

        leftEncoder.setMeasurementPeriod(10);
        rightEncoder.setMeasurementPeriod(10);

        resetEncoders();
        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    }

    public void periodic() {
        // Update the odometry in the periodic block
//        TODO: Make sure positions are in meters
        m_odometry.update(Rotation2d.fromDegrees(getHeading()), leftEncoder.getPosition(),
                rightEncoder.getPosition());
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
//        TODO: make sure what is passed in is in m/s
        return new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity());
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMaster.setVoltage(leftVolts);
        rightMaster.setVoltage(-rightVolts);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
    }

    /**
     * Gets the left drive encoder.
     *
     * @return the left drive encoder
     */
    public CANEncoder getLeftEncoder() {
        return leftEncoder;
    }

    /**
     * Gets the right drive encoder.
     *
     * @return the right drive encoder
     */
    public CANEncoder getRightEncoder() {
        return rightEncoder;
    }

    /**
     * Zeroes the heading of the robot.
     */
    public void zeroHeading() {
        gyro.setFusedHeading(0);
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in degrees, from 180 to 180
     */
    public double getHeading() {
        return Math.IEEEremainder(gyro.getFusedHeading(), 360) * (kGyroReversed ? -1.0 : 1.0);
    }

    private double lastHeading;
    private double lastTime;
    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        double heading = gyro.getFusedHeading();
        double time = System.currentTimeMillis()/1000d;
        double rate = (heading-lastHeading)/(time - lastTime) * (kGyroReversed ? -1.0 : 1.0);
        lastHeading = heading;
        lastTime = time;
        return rate;
    }
}
