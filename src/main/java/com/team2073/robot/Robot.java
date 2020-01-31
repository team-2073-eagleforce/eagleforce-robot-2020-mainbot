package com.team2073.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private Joystick controller = new Joystick(0);
    private Servo servoOne = new Servo(9);
    //  private Servo servoTwo = new Servo(6);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        SmartDashboard.putNumber("Voltage", 7);
    }

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
        if (isEnabled()) {

        } else {

        }

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        System.out.println(servoOne.getAngle());
        if (controller.getRawButtonPressed(1)) {
            servoOne.setAngle(0);
        } else if (controller.getRawButtonPressed(2)) {
            servoOne.setAngle(270);
        } else if (controller.getRawButtonPressed(3)) {
            servoOne.setAngle(servoOne.getAngle() + 5);
        } else if (controller.getRawButtonPressed(4)) {
            servoOne.setAngle(servoOne.getAngle() - 5);
        } else if (controller.getRawButtonPressed(5)) {
            servoOne.setAngle(servoOne.getAngle() + 1);
        } else if (controller.getRawButtonPressed(6)) {
            servoOne.setAngle(servoOne.getAngle() - 1);
        }
    }


    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
