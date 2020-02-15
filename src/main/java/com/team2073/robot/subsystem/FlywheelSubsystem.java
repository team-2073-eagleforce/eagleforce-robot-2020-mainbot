package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.GraphCSVUtil;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.statespace.ShooterVelocityCounter;
import com.team2073.robot.statespace.statespaceflywheel.subsystems.FlywheelController;
import edu.wpi.first.wpilibj.RobotController;

import java.io.IOException;

public class FlywheelSubsystem implements AsyncPeriodicRunnable {

    private final FlywheelController m_wheel = new FlywheelController();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private ShooterVelocityCounter counter = appCtx.getVelocityCounter();
    private TalonSRX m_motor = appCtx.getShooterMotorOne();
    private VictorSPX m_motor2 = appCtx.getShooterMotorTwo();
    private VictorSPX m_motor3 = appCtx.getShooterMotorThree();
    private GraphCSVUtil csv = new GraphCSVUtil("shooter", "iterations", "velocity (rpm)",
            "Estimated Velocity", "Talon Output (V)", "Battery Voltage (V)", "Reference");
//    private double rpm_reference = 4710; good for a dist of 133.5 in from front bumper rail
    private double rpm_reference = 5975;
    private double reference = rpm_reference * 2 * Math.PI / 60; // 130"
    private boolean endFile = false;
    private double iteration = 0;

    public FlywheelSubsystem() {
        autoRegisterWithPeriodicRunner(10);
        init();
		TalonUtil.resetTalon(m_motor, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(m_motor2, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(m_motor3, TalonUtil.ConfigurationType.SENSOR);
        m_motor.setInverted(true);
        m_motor2.setInverted(false);
        m_motor3.setInverted(true);
        m_motor.configOpenloopRamp(0.25);
        m_motor2.configOpenloopRamp(0.25);
        m_motor3.configOpenloopRamp(0.25);

//        m_motor2.follow(m_motor);
//        m_motor3.follow(m_motor);

    }

    /**
     * Iterates the shooter control loop one cycle.
     */
    public void iterate(double velocity) {
        m_wheel.setMeasuredVelocity(velocity);
        m_wheel.update();

        double batteryVoltage = RobotController.getBatteryVoltage();
        double controllerVoltage = m_wheel.getControllerVoltage();
        m_motor.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
    	m_motor2.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
    	m_motor3.set(ControlMode.PercentOutput, controllerVoltage / batteryVoltage);
    }

    @Override
    public void onPeriodicAsync() {
        double currentVelocity = counter.getVelocity();
//        System.out.println("Current Velocity: " + currentVelocity * 60 / (2*Math.PI) + "\t Output" + m_motor.getMotorOutputVoltage());
        setReference(reference);
        enable();

//		System.out.println("Speed: " +  currentVelocity * 60 / (2*Math.PI) + "\t Kalman Estimated Velocity: " + getEstimatedVelocity());
        iterate(currentVelocity);

        csv.updateMainFile(iteration, currentVelocity * 60 / (2 * Math.PI), getEstimatedVelocity() * 60 / (2 * Math.PI),
                getTalonVoltage(), RobotController.getBatteryVoltage(), rpm_reference);
        endFile = false;
        iteration++;
    }


    public void init() {
        try {
            csv.initFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        disable();
        counter.reset();
        if (!endFile) {
            csv.writeToFile();
            System.out.println(System.getProperty("user.home"));
            endFile = true;
        }
        m_wheel.reset();
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

    public double getControllerVoltage() {
        return m_wheel.getControllerVoltage();
    }

    public double getTalonVoltage() {
        return m_motor.getMotorOutputVoltage();
    }

    public boolean atShootingRPM(double angularVelocity) {
        return false;
    }

    public double getAmerageDraw() {
        return m_motor.getStatorCurrent();
    }

    public double getEstimatedVelocity() {
        return m_wheel.getEstimatedVelocity();
    }
}
