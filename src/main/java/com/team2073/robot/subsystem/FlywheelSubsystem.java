package com.team2073.robot.subsystem;

import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.GraphCSVUtil;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.RobotDelegate;
import com.team2073.robot.statespace.ShooterVelocityCounter;
import com.team2073.robot.statespace.statespaceflywheel.subsystems.Flywheel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Servo;
import org.opencv.core.Mat;

import java.io.IOException;
import java.lang.reflect.Array;

public class FlywheelSubsystem implements AsyncPeriodicRunnable {

	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private ShooterVelocityCounter counter = appCtx.getVelocityCounter();
	private Servo newServo = appCtx.getServo();
	private Flywheel shooter = new Flywheel();
	private GraphCSVUtil csv = new GraphCSVUtil("shooter", "iterations", "velocity (rpm)",
			"Estimated Velocity", "Talon Output (V)", "Battery Voltage (V)", "Reference");
	private double rpm_reference = 5090;
	private double reference = rpm_reference * 2 * Math.PI / 60; // 130"
	private boolean endFile = false;
	private double iteration = 0;
//	private double[] lastVelocity = new double[5];
//	private double[] timeIntervals = new double[5];
	private double lastVelocity = 0;

	@Override
	public void onPeriodicAsync() {
		double currentVelocity = counter.getVelocity();
		shooter.setReference(reference);
		shooter.enable();

//		System.out.println("Speed: " +  currentVelocity * 60 / (2*Math.PI) + "\t Kalman Estimated Velocity: " + shooter.getEstimatedVelocity());


//			shooter.iterate(lastVelocity);
//
//		} else {
//			shooter.iterate(currentVelocity);
//			lastVelocity = currentVelocity;
//		}

		shooter.iterate(currentVelocity);

		csv.updateMainFile(iteration, currentVelocity * 60 / (2 * Math.PI), shooter.getEstimatedVelocity() * 60 / (2*Math.PI),
				shooter.getTalonVoltage(), RobotController.getBatteryVoltage(), rpm_reference);
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
		shooter.disable();
		shooter.reset();
		counter.reset();
		if(!endFile) {
			csv.writeToFile();
			System.out.println(System.getProperty("user.home"));
			endFile = true;
		}
	}

//	public void addInterval(double time){
//		for(double shift: timeIntervals){
//
//		}
//	}
}
