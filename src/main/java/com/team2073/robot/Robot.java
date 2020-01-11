package com.team2073.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";

    /**
     * A Rev Color Sensor V3 object is constructed with an I2C port as a
     * parameter. The device will be automatically initialized with default
     * parameters.
     */

    /**
     * This function is called periodically during test mode.
     */


    /**
     * Note: Any example colors should be calibrated as the user needs, these
     * are here as a basic example.
     */
/*    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);*/
//    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);


    private String m_autoSelected;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

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

    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        switch (m_autoSelected) {
            case kCustomAuto:
                // Put custom auto code here
                break;
            case kDefaultAuto:
            default:
                // Put default auto code here
                break;
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {


    }


    @Override
    public void testPeriodic() {

    }


}
