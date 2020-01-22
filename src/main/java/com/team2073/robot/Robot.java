package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2073.common.util.GraphCSVUtil;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import java.io.IOException;

public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private WOFManipulatorSubsystem wofManipulatorSubsystem; //= ApplicationContext.getInstance().getWofManipulatorSubsystem();

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

    @Override
    public void teleopInit() {
        //wofManipulatorSubsystem.addColorMatch();
        wofManipulatorSubsystem = ApplicationContext.getInstance().getWofManipulatorSubsystem();
        wofManipulatorSubsystem.addColorMatch();
    }

    /**
     * This function is called periodically during operator control.
     */


    @Override
    public void teleopPeriodic() {
        wofManipulatorSubsystem.onPeriodic();
        //System.out.println(wofManipulatorSubsystem.readColor());
        Joystick controller = ApplicationContext.getInstance().getController();
        if(controller.getRawButton(1)){
            wofManipulatorSubsystem.stopOnColor();
        }else{
            wofManipulatorSubsystem.setMotor(0d);
        }

        if(controller.getRawButtonPressed(2)){
            wofManipulatorSubsystem.rotationCounter();
            wofManipulatorSubsystem.stopOnRotation();
        }

        if(isDisabled()){
            wofManipulatorSubsystem.setMotor(0d);
        }

    }

    @Override
    public void testInit() {
        WOFManipulatorSubsystem.createCSV();
        wofManipulatorSubsystem = ApplicationContext.getInstance().getWofManipulatorSubsystem();
    }

    @Override
    public void testPeriodic() {
        wofManipulatorSubsystem.calibrate();
    }

}
