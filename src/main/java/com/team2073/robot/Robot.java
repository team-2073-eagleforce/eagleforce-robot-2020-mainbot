package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.ctremotionprofile.MotionProfileGenerator;
import com.team2073.common.ctremotionprofile.MotionProfileHelper;
import com.team2073.common.motionprofiling.MotionProfileConfiguration;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.ProfileTrajectoryPoint;
import com.team2073.common.motionprofiling.SCurveShooterProfile;
import com.team2073.common.util.GraphCSVUtil;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.statespace.shooter.Shooter;
import com.team2073.robot.statespace.statespaceflywheel.subsystems.Flywheel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import org.opencv.core.Mat;

import java.io.IOException;

public class Robot extends TimedRobot {

    private Counter AChannel = new Counter(8);
    private Counter BChannel = new Counter(9);
    Flywheel shooter = new Flywheel();

//    private TalonFX tf = new TalonFX(3);
    private GraphCSVUtil csv = new GraphCSVUtil("shooter", "iterations", "velocity (rpm)", "output (voltage)", "Talon Output (V)", "Battery Voltage (V)");

    private int lastCount;
    private double lastTime;
    double lastInterval;

//    private Encoder encoder = new Encoder(8,9);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        try {
            csv.initFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    double speed = 0;
    boolean endFile = false;
    double iteration = 0;
    double reference = 7000.0 * 2 * Math.PI / 60;

    private double maxAcceleration = (6900 * 2 * Math.PI / 60) / 1.03;
    private double maxJerk = maxAcceleration / 0.5;
    private double goalVelocity = reference; // in rad/s - 2000 RPM
    private double startingVelocity = 0;

    ProfileConfiguration pc = new ProfileConfiguration(goalVelocity, maxAcceleration, maxJerk,0.01);
    SCurveShooterProfile shooterProfile = new SCurveShooterProfile(startingVelocity, goalVelocity, pc);

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {

        double current = Timer.getFPGATimestamp();
        int counter = AChannel.get();
        double dt = 0;
        double interval = 0;

//        tf.getSensorCollection()
        if(isEnabled()){
            endFile = false;
            interval = current - lastInterval;
            lastInterval = current;
            ProfileTrajectoryPoint newPoint = shooterProfile.nextPoint(interval);

            shooter.setReference(newPoint.getVelocity());
            shooter.enable();

            if(counter - lastCount >= 10) {
                dt = current - lastTime;
                speed = ((counter - lastCount) * 2 * Math.PI ) / (2048 * dt);
                lastTime = current;
                lastCount = counter;
            }
            System.out.println("Speed: " + speed * 60 / (2*Math.PI));
            iteration ++;
            csv.updateMainFile(iteration, speed * 60 / (2 * Math.PI), shooter.getControllerVoltage(), shooter.getTalonVoltage(), RobotController.getBatteryVoltage());
            shooter.iterate(speed);
        } else {
            speed = 0;
            shooter.disable();
            shooter.reset();

            if(!endFile) {
                csv.writeToFile();
                System.out.println(System.getProperty("user.home"));
                endFile = true;
            }

        }

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {


    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
