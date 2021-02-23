package com.team2073.robot.subsystem.drive;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.ConversionUtil;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import java.util.List;

import static com.team2073.robot.subsystem.drive.Constants.DriveConstants.*;


public class DriveSubsystem implements AsyncPeriodicRunnable {
    //    private GraphCSVUtil graph = new GraphCSVUtil("driveCalibration", "time", "leftVelocity");

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

    //  a starting heading of zero means that our intake starts facing directly away from the target
    private double startingHeading = 0;


    /**
     * Creates a new DriveSubsystem.
     */
    public DriveSubsystem() {
        autoRegisterWithPeriodicRunner(10);
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


//        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(115d), ConversionUtil.inchesToMeters(115d), Rotation2d.fromDegrees(-50d))
//                , Rotation2d.fromDegrees(getHeading()));
//        startingHeading = Rotation2d.fromDegrees(getHeading()).getDegrees() - 50;

//
//        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(130d), ConversionUtil.inchesToMeters(112d -28), Rotation2d.fromDegrees(0d))
//                , Rotation2d.fromDegrees(getHeading()));
        startingHeading = Rotation2d.fromDegrees(getHeading()).getDegrees();

//        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(228d), ConversionUtil.inchesToMeters(40d), Rotation2d.fromDegrees(115d-180d))
//                , Rotation2d.fromDegrees(getHeading()));

    }

    public void getOutput() {
        System.out.println(rightMaster.getAppliedOutput());
    }

    public double getStartingHeading() {
        return startingHeading;
    }

    public void setStartingHeading(double startingHeading) {
        this.startingHeading = startingHeading;
    }

    public void updateOdometry() {
        // Update the odometry in the periodic block
//        TODO: Make sure positions are in meters
        m_odometry.update(Rotation2d.fromDegrees(getHeading()), leftEncoder.getPosition(),
                rightEncoder.getPosition());
    }

    public void resetPosition(double x, double y, double angle){
        m_odometry.resetPosition(new Pose2d(ConversionUtil.inchesToMeters(x), ConversionUtil.inchesToMeters(y), Rotation2d.fromDegrees(angle))
                , Rotation2d.fromDegrees(getHeading()));

        startingHeading = Rotation2d.fromDegrees(getHeading()).getDegrees();
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
        DriveSignal driveSignal = cheesyDriveHelper.cheesyDrive(-joystick.getRawAxis(1), adjustWheel(wheel.getRawAxis(0)), wheel.getRawButton(5));
        double left = driveSignal.getLeft();
        double right = driveSignal.getRight();
        left = Math.abs(left) > maxPercent ? maxPercent * Math.signum(left) : left;
        right = Math.abs(right) > maxPercent ? maxPercent * Math.signum(right) : right;
        leftMaster.set(left);
        rightMaster.set(right);
    }

    private double adjustWheel(double rawJoystick) {
        if (rawJoystick < .04 && rawJoystick > -.04) {
            rawJoystick = 0;
        }
        if (rawJoystick < 0) {
            return Math.max(-1d, (rawJoystick * 120d) / 90d);
        } else {
            return Math.min(1d, (rawJoystick * 120d) / 90d);
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
//        double x = ConversionUtil.metersToFeet(m_odometry.getPoseMeters().getTranslation().getX());
//
//        double y = ConversionUtil.metersToFeet(m_odometry.getPoseMeters().getTranslation().getY());
//        double theta = m_odometry.getPoseMeters().getRotation().getDegrees();
//        System.out.println("X: " + x + "\t y: " + y + "\t theta: " + theta);
        if (RobotState.isOperatorControl() && RobotState.isEnabled()) {
            if (leftMaster.getIdleMode() == CANSparkMax.IdleMode.kCoast) {

                leftSlave1.setIdleMode(CANSparkMax.IdleMode.kBrake);
                leftSlave2.setIdleMode(CANSparkMax.IdleMode.kBrake);
                leftMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightSlave1.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightSlave2.setIdleMode(CANSparkMax.IdleMode.kBrake);
                rightMaster.setIdleMode(CANSparkMax.IdleMode.kBrake);
            }
//            graph.updateMainFile(Timer.getFPGATimestamp(), ConversionUtil.metersToFeet(getWheelSpeeds().leftMetersPerSecond));
//            wrote = false;
            teleopDrive();
//            System.out.println(gyro.getFusedHeading());
        }
//        if(RobotState.isDisabled() && !wrote){
//            graph.writeToFile();
//            wrote = true;
//        }

//        System.out.println("Velocity: " + ConversionUtil.metersToFeet(getWheelSpeeds().leftMetersPerSecond));
    }

    public enum atHomePaths {
        Test(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(20d), ConversionUtil.inchesToMeters(-95d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(70d), ConversionUtil.inchesToMeters(-95d), Rotation2d.fromDegrees(0d))
                ),
                config.setReversed(false)));
        private Trajectory traj;
        atHomePaths(Trajectory traj) {
            this.traj = traj;
        }

        public Trajectory getTraj() {
            return traj;
        }
    }

    public enum AutoPaths {
        PICK_FIRST_2(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(115d), ConversionUtil.inchesToMeters(115d), Rotation2d.fromDegrees(-50d)),
//                    new Pose2d(ConversionUtil.inchesToMeters(150d), ConversionUtil.inchesToMeters(70d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(220d), ConversionUtil.inchesToMeters(30d), Rotation2d.fromDegrees(-70d))
                ),
                config.setReversed(false))),
        SHOOT_FIRST_FIVE(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(220d), ConversionUtil.inchesToMeters(30d), Rotation2d.fromDegrees(-70d)),
                        new Pose2d(ConversionUtil.inchesToMeters(130d), ConversionUtil.inchesToMeters(65d), Rotation2d.fromDegrees(0d))),
                config.setReversed(true))),
        TRENCH_RUN(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(130d), ConversionUtil.inchesToMeters(65d), Rotation2d.fromDegrees(0)),
                        new Pose2d(ConversionUtil.inchesToMeters(210d), ConversionUtil.inchesToMeters(90d), Rotation2d.fromDegrees(20)),
                        new Pose2d(ConversionUtil.inchesToMeters(280d), ConversionUtil.inchesToMeters(94d), Rotation2d.fromDegrees(0))),
                config.setReversed(false))),
        TRENCH_RUN_RETURN(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(300d), ConversionUtil.inchesToMeters(94d), Rotation2d.fromDegrees(0)),
                        new Pose2d(ConversionUtil.inchesToMeters(200d), ConversionUtil.inchesToMeters(94d), Rotation2d.fromDegrees(0))),
                config.setReversed(true)));


        private Trajectory traj;

        AutoPaths(Trajectory traj) {
            this.traj = traj;
        }

        public Trajectory getTraj() {
            return traj;
        }
    }

    public enum AutoPathTrench {
        PICK_FIRST_2(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(130d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(265d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d))
                ),
                config.setReversed(false))),
        TRENCH_TO_SHOOT(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(265d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(120d), ConversionUtil.inchesToMeters(60d), Rotation2d.fromDegrees(0d))),
                config.setReversed(true))),
        THREE_BALL_RUN(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(120d), ConversionUtil.inchesToMeters(60d), Rotation2d.fromDegrees(0d)),
//                new Pose2d(ConversionUtil.inchesToMeters(265d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(300d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d))),
                config.setReversed(false))),
        DOWN_THE_TRENCH(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(130d), ConversionUtil.inchesToMeters(112d - 30), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(260d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(370d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d))
                ),
                config.setReversed(false))),
        LEAVE_THE_TRENCH(TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2d(ConversionUtil.inchesToMeters(370d), ConversionUtil.inchesToMeters(112d), Rotation2d.fromDegrees(0d)),
                        new Pose2d(ConversionUtil.inchesToMeters(150d), ConversionUtil.inchesToMeters(80d), Rotation2d.fromDegrees(0d))
                ),
                config.setReversed(true)));

        private Trajectory traj;

        AutoPathTrench(Trajectory traj) {
            this.traj = traj;
        }

        public Trajectory getTraj() {
            return traj;
        }

    }
}
