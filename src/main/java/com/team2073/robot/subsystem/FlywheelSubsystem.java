package com.team2073.robot.subsystem;

import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.GraphCSVUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.RobotDelegate;
import com.team2073.robot.statespace.ShooterVelocityCounter;
import com.team2073.robot.statespace.statespaceflywheel.subsystems.Flywheel;
import edu.wpi.first.wpilibj.RobotController;

import java.io.IOException;

public class FlywheelSubsystem implements AsyncPeriodicRunnable {

	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private ShooterVelocityCounter counter = appCtx.getVelocityCounter();
	private Flywheel shooter = new Flywheel();
	private GraphCSVUtil csv = new GraphCSVUtil("shooter", "iterations", "velocity (rpm)",
			"output (voltage)", "Talon Output (V)", "Battery Voltage (V)", "Current Draw (A)");
	private double reference = 4490 * 2 * Math.PI / 60; // 130"
	private boolean endFile = false;
	private double iteration = 0;

	@Override
	public void onPeriodicAsync() {
		// Converts RPM to Rad/Sec
		shooter.setReference(reference);
		shooter.enable();

		System.out.println("Speed: " +  counter.getVelocity() * 60 / (2*Math.PI));
//		System.out.println("hey");
		shooter.iterate(counter.getVelocity());

		csv.updateMainFile(iteration, counter.getVelocity() * 60 / (2 * Math.PI), shooter.getControllerVoltage(),
				shooter.getTalonVoltage(), RobotController.getBatteryVoltage(), shooter.getAmerageDraw());
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
}
