/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2073.robot.statespace.statespaceflywheel.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.*;

public class Flywheel {
  private ApplicationContext appCtx = ApplicationContext.getInstance();
//  private TalonSRX m_motor = appCtx.getShooterMotorOne();
//  private TalonSRX m_motor2 = appCtx.getShooterMotorTwo();
//  private TalonSRX m_motor3 = appCtx.getShooterMotorThree();
  private TalonSRX m_motor = new TalonSRX(1);
  private TalonSRX m_motor2 = new TalonSRX(4);
  private TalonSRX m_motor3 = new TalonSRX(9);
//  private final Encoder m_encoder = new Encoder(1, 2);

  private final FlywheelController m_wheel = new FlywheelController();
//  private final Notifier m_thread = new Notifier(this::iterate);

  public Flywheel() {
    m_motor.configOpenloopRamp(2);
    m_motor2.configOpenloopRamp(2);
    m_motor3.configOpenloopRamp(2);
  }

  public void enable() {
    m_wheel.enable();
  }

  public void disable() {
    m_wheel.disable();
  }

  public void setReference(double angularVelocity) {
    m_wheel.setVelocityReference(angularVelocity);
  }

  public boolean atReference() {
    return m_wheel.atReference();
  }

  /**
   * Iterates the shooter control loop one cycle.
   */
  public void iterate(double velocity) {
    m_wheel.setMeasuredVelocity(velocity);
    m_wheel.update();



    double batteryVoltage = RobotController.getBatteryVoltage();
    double controllerVoltage = m_wheel.getControllerVoltage();
//    m_motor.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
//    m_motor2.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
    m_motor3.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
//    m_motor.set(ControlMode.PercentOutput, 0.3);
//    m_motor3.set(ControlMode.PercentOutput, 0.4);
//    System.out.println("Output: " + m_motor.getMo
//    torOutputVoltage() + "\tBattery Voltage: " + batteryVoltage);
    double current1 = m_motor.getStatorCurrent();
    double current2 = m_motor2.getStatorCurrent();
    double current3 = m_motor3.getStatorCurrent();
    double max = 0;
    double min = 0;
    if(current1 < current2 && current1 < current3){
      System.out.println("entering 1");
      min = current1;
    }else if(current2 < current1 && current2 < current3){
      System.out.println("entering 2");
      min = current2;
    }else if(current3 < current1 && current3> current2){
      System.out.println("entering 3");
      min = current3;
    }

    if(current1 > current2 && current1 > current3){
      System.out.println("entering max 1");
      max = current1;
    }else if(current2 > current1 && current2 > current3){
      System.out.println("entering max 2");
      max = current2;
    }else if(current3 > current1 && current3 > current2){
      System.out.println("entering max 3");
      max = current3;
    }

    System.out.println("Current Diff " + current1 + "\t" + current2 + "\t " + current3 );
  }

  public void reset() {
    m_wheel.reset();
  }

  public double getControllerVoltage(){
    return m_wheel.getControllerVoltage();
  }

  public double getTalonVoltage() {
    return m_motor.getMotorOutputVoltage();
  }

  //change this
  public boolean atShootingRPM(double angularVelocity) {
    return false;
  }

  public double getAmerageDraw(){
    return m_motor.getStatorCurrent();
  }
}
