/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2073.robot.statespace.statespaceflywheel.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.*;
import org.graalvm.compiler.lir.amd64.AMD64MathIntrinsicBinaryOp;

public class Flywheel {
//  private ApplicationContext appCtx = ApplicationContext.getInstance();
//  private TalonSRX m_motor = appCtx.getShooterMotorOne();
//  private VictorSPX m_motor2 = appCtx.getShooterMotorTwo();
//  private VictorSPX m_motor3 = appCtx.getShooterMotorThree();
////  private final Encoder m_encoder = new Encoder(1, 2);
//
//  private final FlywheelController m_wheel = new FlywheelController();
////  private final Notifier m_thread = new Notifier(this::iterate);
//
//  public Flywheel() {
//    m_motor.setInverted(false);
//    m_motor2.setInverted(false);
//    m_motor3.setInverted(false);
//    m_motor.configOpenloopRamp(0.25);
//    m_motor2.configOpenloopRamp(0.25);
//    m_motor3.configOpenloopRamp(0.25);
//
//    m_motor2.follow(m_motor);
//    m_motor3.follow(m_motor);
//
//  }
//
//  public void enable() {
//    m_wheel.enable();
//  }
//
//  public void disable() {
//    m_wheel.disable();
//  }
//
//  public void setReference(double angularVelocity) {
//    m_wheel.setVelocityReference(angularVelocity);
//  }
//
//  public boolean atReference() {
//    return m_wheel.atReference();
//  }
//
//  /**
//   * Iterates the shooter control loop one cycle.
//   */
//  public void iterate(double velocity) {
//    m_wheel.setMeasuredVelocity(velocity);
//    m_wheel.update();
//
//
//
//    double batteryVoltage = RobotController.getBatteryVoltage();
//    double controllerVoltage = m_wheel.getControllerVoltage();
//    m_motor.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
////    m_motor2.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
////    m_motor3.set(ControlMode.PercentOutput, -controllerVoltage / batteryVoltage);
////      m_motor3.set(ControlMode.PercentOutput, 0.3);
////    m_motor3.set(ControlMode.PercentOutput, 0.4);
////    System.out.println("Output: " + m_motor.getMo
////    torOutputVoltage() + "\tBattery Voltage: " + batteryVoltage);
////    System.out.println("going");
//
//  }
//
//  public void reset() {
//    m_wheel.reset();
//  }
//
//  public double getControllerVoltage(){
//    return m_wheel.getControllerVoltage();
//  }
//
//  public double getTalonVoltage() {
//    return m_motor.getMotorOutputVoltage();
//  }
//
//  //change this
//  public boolean atShootingRPM(double angularVelocity) {
//    return false;
//  }
//
//  public double getAmerageDraw(){
//    return m_motor.getStatorCurrent();
//  }
//
//  public double getEstimatedVelocity(){
//    return m_wheel.getEstimatedVelocity();
//  }
}
