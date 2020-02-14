package com.team2073.robot.statespace.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Shooter {

    // Check whether motors are slaves or motors are masters
    // Check the deviceNumbers, currently I put fillers
    private final TalonSRX motorOne = new TalonSRX(1);
    private final TalonSRX motorTwo = new TalonSRX(4);


//    private final Encoder encoder = new Encoder(8, 9);

    private final ShooterController shooterController = new ShooterController();

    public void enable() {
        shooterController.enable();
    }

    public void disable() {
        shooterController.disable();
        motorOne.set(ControlMode.PercentOutput, 0);
        motorTwo.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Sets the references.
     *
     * @param angularVelocity Velocity of arm in radians per second.
     */
    public void setReference(double angularVelocity) {
        shooterController.setReferences(angularVelocity);
    }

    public boolean atReferences() {
        return shooterController.atReferences();
    }

    /**
     * Iterates the shooter control loop one cycle.
     */
    public void iterate(double velocity) {
        shooterController.setMeasuredVelocity(velocity);
        shooterController.update();

//        double batteryVoltage = RobotController.getBatteryVoltage();
        System.out.println(getControllerVoltage());
        motorOne.set(ControlMode.PercentOutput, shooterController.getControllerVoltage()/12);
        //motorTwo.set(ControlMode.PercentOutput, -shooterController.getControllerVoltage() / batteryVoltage);

    }

    public double getControllerVoltage() {
        return shooterController.getControllerVoltage();
    }

    public void reset() {
        shooterController.reset();
    }

    public double getTalonVoltage() {
        return motorOne.getMotorOutputVoltage();
    }


}
