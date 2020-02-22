package com.team2073.robot.subsystem.drive;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.ConversionUtil;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import java.sql.SQLOutput;
import java.util.List;

import static com.team2073.robot.subsystem.drive.Constants.DriveConstants.DISTANCE_PER_MOTOR_REV_METERS;
import static com.team2073.robot.subsystem.drive.Constants.DriveConstants.kGyroReversed;


public class DriveSubsystem implements AsyncPeriodicRunnable {
    //    private GraphCSVUtil graph = new GraphCSVUtil("driveCalibration", "time", "leftVelocity");
    private static DifferentialDriveVoltageConstraint autoVoltageConstraint =
            new DifferentialDriveVoltageConstraint(
                    new SimpleMotorFeedforward(Constants.DriveConstants.ksVolts,
                            Constants.DriveConstants.kvVoltSecondsPerMeter,
                            Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
                    Constants.DriveConstants.DRIVE_KINEMATICS,
                    12);
    // Create config for trajectory
    private static TrajectoryConfig config =
            new TrajectoryConfig(Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                    Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                    // Add kinematics to ensure max speed is actually obeyed
                    .setKinematics(Constants.DriveConstants.DRIVE_KINEMATICS)
                    // Apply the voltage constraint
                    .addConstraint(autoVoltageConstraint);
    // The gyro sensor
    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private final PigeonIMU gyro = appCtx.getGyro();
    private CANSparkMax leftMaster = appCtx.getLeftMaster();
    private CANSparkMax rightMaster = appCtx.getRightMaster();
    private CANSparkMax leftSlave1 = appCtx.getLeftSlave1();
    private CANSparkMax leftSlave2 = appCtx.getLeftSlave2();
    private CANSparkMax rightSlave1 = appCtx.getRightSlave1();
    private CANSparkMax rightSlave2 = appCtx.getRightSlave2();
    private CANEncoder leftEncoder = leftMaster.getEncoder();
    private CANEncoder rightEncoder = rightMaster.getEncoder();
    private CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper();
    private Joystick joystick = appCtx.getDriveStick();
    private Joystick wheel = appCtx.getDriveWheel();
    private double maxPercent = 1.0;
    private double lastHeading;
    private double lastTime;
    private boolean wrote = true;

    /**
     * Creates a new DriveSubsystem.
     */
    public DriveSubsystem() {
        // Sets the distance per pulse for the encoders
//        try {
//            graph.initFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        autoRegisterWithPeriodicRunner(10);
//        TODO:  MATH TO MAKE SURE UNITS ARE PROPER
        leftSlave1.setInverted(true);
        leftSlave2.setInverted(true);
        leftMaster.setInverted(true);
        rightSlave1.setInverted(false);
        rightSlave2.setInverted(false);
        rightMaster.setInverted(false);
        leftMaster.setOpenLoopRampRate(.25);
        leftSlave1.setOpenLoopRampRate(.25);
        leftSlave2.setOpenLoopRampRate(.25);
        rightMaster.setOpenLoopRampRate(.25);
        rightSlave1.setOpenLoopRampRate(.25);
        rightSlave2.setOpenLoopRampRate(.25);
        leftSlave1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        leftSlave2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        leftMaster.setIdleMode(CANSparkMax.IdleMode.kCoast);
        rightSlave1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        rightSlave2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        rightMaster.setIdleMode(CANSparkMax.IdleMode.kCoast);
        leftEncoder.setPositionConversionFactor(DISTANCE_PER_MOTOR_REV_METERS);
        rightEncoder.setPositionConversionFactor(DISTANCE_PER_MOTOR_REV_METERS);
        leftEncoder.setVelocityConversionFactor(DISTANCE_PER_MOTOR_REV_METERS / 60);
        rightEncoder.setVelocityConversionFactor(DISTANCE_PER_MOTOR_REV_METERS / 60);
        leftMaster.setSmartCurrentLimit(60);
        leftSlave1.setSmartCurrentLimit(60);
        leftSlave2.setSmartCurrentLimit(60);
        rightMaster.setSmartCurrentLimit(60);
        rightSlave1.setSmartCurrentLimit(60);
        rightSlave2.setSmartCurrentLimit(60);
        leftEncoder.setMeasurementPeriod(10);
        rightEncoder.setMeasurementPeriod(10);
        leftSlave1.follow(leftMaster);
        leftSlave2.follow(leftMaster);
        rightSlave1.follow(rightMaster);
        rightSlave2.follow(rightMaster);
        resetEncoders();
        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(100d), ConversionUtil.inchesToMeters(120d), Rotation2d.fromDegrees(-90d))
                , Rotation2d.fromDegrees(getHeading()));
//        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(228d), ConversionUtil.inchesToMeters(40d), Rotation2d.fromDegrees(115d-180d))
//                , Rotation2d.fromDegrees(getHeading()));

    }

    public void updateOdometry() {
        // Update the odometry in the periodic block
//        TODO: Make sure positions are in meters
        m_odometry.update(Rotation2d.fromDegrees(getHeading()), leftEncoder.getPosition(),
                rightEncoder.getPosition());
    }

    /**
     * Returns the currently-estimated pose of the robot.
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
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
            rightMaster.setVoltage(rightVolts);
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

    public void capTeleopOutput(double maxPercent) {
        this.maxPercent = maxPercent;
    }

    private void teleopDrive() {
        DriveSignal driveSignal = cheesyDriveHelper.cheesyDrive(-joystick.getRawAxis(1), adjustWheel(wheel.getRawAxis(0)), wheel.getRawButton(1));
//        System.out.println(joystick.getRawAxis(1));
        double left = driveSignal.getLeft();
        double right = driveSignal.getRight();
        left = Math.abs(left) > maxPercent ? maxPercent * Math.signum(left) : left;
        right = Math.abs(right) > maxPercent ? maxPercent * Math.signum(right) : right;
        leftMaster.set(left);
        rightMaster.set(right);
    }

    private double adjustWheel(double rawJoystick) {
        if (rawJoystick < .02 && rawJoystick > -.02) {
            rawJoystick = 0;
        }
        if (rawJoystick < 0) {
            return Math.max(-1d, (rawJoystick * 110d) / 90d);
        } else {
            return Math.min(1d, (rawJoystick * 110d) / 90d);
        }

    }

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        double heading = gyro.getFusedHeading();
        double time = System.currentTimeMillis() / 1000d;
        double rate = (heading - lastHeading) / (time - lastTime) * (kGyroReversed ? -1.0 : 1.0);
        lastHeading = heading;
        lastTime = time;
        return rate;
    }

    @Override
    public void onPeriodicAsync() {
        updateOdometry();
        double x = ConversionUtil.metersToFeet(m_odometry.getPoseMeters().getTranslation().getX());

        double y = ConversionUtil.metersToFeet(m_odometry.getPoseMeters().getTranslation().getY());
        double theta = m_odometry.getPoseMeters().getRotation().getDegrees();
        System.out.println("X: " + x + "\t y: " + y + "\t theta: " +theta);
//        System.out.println("LeftVel: " +getWheelSpeeds().leftMetersPerSecond  + "\trightVel: " + getWheelSpeeds().rightMetersPerSecond);
        if (RobotState.isOperatorControl() && RobotState.isEnabled()) {
            if(leftMaster.getIdleMode() == CANSparkMax.IdleMode.kCoast){

                leftSlave1.setIdleMode(CANSparkMax.IdleMode.kBrake);
                leftSlave2.setIdleMode(CANSparkMax.IdleMode.kBrake);
                leftMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightSlave1.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightSlave2.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
            }
//            graph.updateMainFile(Timer.getFPGATimestamp(), ConversionUtil.metersToFeet(getWheelSpeeds().leftMetersPerSecond));
//            wrote = false;
//            teleopDrive();
        }
//        if(RobotState.isDisabled() && !wrote){
//            graph.writeToFile();
//            wrote = true;
//        }

//        System.out.println("Velocity: " + ConversionUtil.metersToFeet(getWheelSpeeds().leftMetersPerSecond));
    }

    public enum AutoPaths {
        // Poses are in feet.
//        VAL(TrajectoryGenerator.generateTrajectory(List.of(
////                new Pose2d(ConversionUtil.feetToMeters(130d), ConversionUtil.feetToMeters(120d), new Rotation2d(-90d)),
////                new Pose2d(ConversionUtil.feetToMeters(228d), ConversionUtil.feetToMeters(40d), new Rotation2d(-65d)),
////                new Pose2d(ConversionUtil.feetToMeters(228d), ConversionUtil.feetToMeters(40d), new Rotation2d(115d)),
////                new Pose2d(ConversionUtil.feetToMeters(160d), ConversionUtil.feetToMeters(112d), new Rotation2d(180d)),
////                new Pose2d(ConversionUtil.feetToMeters(160d), ConversionUtil.feetToMeters(112), new Rotation2d(0)),
//                new Pose2d(ConversionUtil.feetToMeters(390d), ConversionUtil.feetToMeters(112), new Rotation2d(0))),
//                config)),

        PICK_FIRST_2(TrajectoryGenerator.generateTrajectory(
                List.of(
                    new Pose2d(ConversionUtil.inchesToMeters(100d), ConversionUtil.inchesToMeters(120d), Rotation2d.fromDegrees(-90d)),
                    new Pose2d(ConversionUtil.inchesToMeters(150d), ConversionUtil.inchesToMeters(70d), Rotation2d.fromDegrees(0d)),
                    new Pose2d(ConversionUtil.inchesToMeters(228d), ConversionUtil.inchesToMeters(40d), Rotation2d.fromDegrees(-60d))
                ),
//                        new Pose2d(ConversionUtil.feetToMeters(0d), ConversionUtil.feetToMeters(0d), new Rotation2d(0)),
//                        List.of(new Translation2d(ConversionUtil.feetToMeters(5),ConversionUtil.feetToMeters(1.5))),
//                        new Pose2d(ConversionUtil.feetToMeters(10d), ConversionUtil.feetToMeters(3d), new Rotation2d(0)),
                config.setReversed(false))),
        SHOOT_FIRST_FIVE(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(228d), ConversionUtil.inchesToMeters(40d), Rotation2d.fromDegrees(-60)),
                        new Pose2d(ConversionUtil.inchesToMeters(160d), ConversionUtil.inchesToMeters(90d), Rotation2d.fromDegrees(0))),
//                        new Pose2d(ConversionUtil.feetToMeters(0d), ConversionUtil.feetToMeters(0d), new Rotation2d(0)),
//                        List.of(new Translation2d(ConversionUtil.feetToMeters(5),ConversionUtil.feetToMeters(1.5))),
//                        new Pose2d(ConversionUtil.feetToMeters(10d), ConversionUtil.feetToMeters(3d), new Rotation2d(0)),
                config.setReversed(true))),
        TRENCH_RUN(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(160d), ConversionUtil.inchesToMeters(90d), Rotation2d.fromDegrees(0)),
                        new Pose2d(ConversionUtil.inchesToMeters(260d), ConversionUtil.inchesToMeters(90d), Rotation2d.fromDegrees(0))),
//                        new Pose2d(ConversionUtil.feetToMeters(0d), ConversionUtil.feetToMeters(0d), new Rotation2d(0)),
//                        List.of(new Translation2d(ConversionUtil.feetToMeters(5),ConversionUtil.feetToMeters(1.5))),
//                        new Pose2d(ConversionUtil.feetToMeters(10d), ConversionUtil.feetToMeters(3d), new Rotation2d(0)),
                config.setReversed(false)));




        private Trajectory traj;

        AutoPaths(Trajectory traj) {
            this.traj = traj;
        }

        public Trajectory getTraj() {
            return traj;
        }
    }
}
