package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private TalonSRX motorOne = new TalonSRX(1);
    private TalonSRX motorTwo = new TalonSRX(4);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        motorOne.configOpenloopRamp(1);
        motorTwo.configOpenloopRamp(1);
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
        if(isEnabled()){
            motorOne.set(ControlMode.PercentOutput, SmartDashboard.getNumber("Voltage", 7)/12d);
            motorTwo.set(ControlMode.PercentOutput, SmartDashboard.getNumber("Voltage", 7)/12d);
            System.out.println("Voltage: " + motorOne.getMotorOutputVoltage() + "\t Amperage: " + motorOne.getStatorCurrent());
        } else {
            motorOne.set(ControlMode.PercentOutput, 0);
            motorTwo.set(ControlMode.PercentOutput, 0);
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
