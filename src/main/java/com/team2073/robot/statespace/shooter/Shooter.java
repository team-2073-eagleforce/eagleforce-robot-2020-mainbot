package com.team2073.robot.statespace.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;

public class Shooter {

    // Check whether motors are slaves or motors are masters
    // Check the deviceNumbers, currently I put fillers
    private final TalonSRX motorOne = new TalonSRX(1);
    private final TalonSRX motorTwo = new TalonSRX(2);
    private final TalonSRX motorThree = new TalonSRX(3);

    private final Encoder encoder = new Encoder(1, 2);

    private final ShooterController shooterController = new ShooterController();

    public void enable() {
        shooterController.enable();
    }

    public void disable() {
        shooterController.disable();
    }

    /**
     * Sets the references.
     *
     * @param angularVelocity Velocity of arm in radians per second.
     */
    public void setReferences(double angularVelocity) {
        shooterController.setReferences(angularVelocity);
    }

    public boolean atReferences() {
        return shooterController.atReferences();
    }

    /**
     * Iterates the elevator control loop one cycle.
     */
    public void iterate() {
        shooterController.setMeasuredVelocity(encoder.getDistance());
        shooterController.update();

        double batteryVoltage = RobotController.getBatteryVoltage();
        motorOne.set(ControlMode.PercentOutput, shooterController.getControllerVoltage() / batteryVoltage);
    }

    public double getControllerVoltage() {
        return shooterController.getControllerVoltage();
    }

    public void reset() {
        shooterController.reset();
    }


}
