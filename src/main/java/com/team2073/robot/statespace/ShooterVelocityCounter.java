package com.team2073.robot.statespace;

import com.team2073.common.motionprofiling.ProfileTrajectoryPoint;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

import java.lang.reflect.Array;

public class ShooterVelocityCounter {

	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private Counter AChannel = appCtx.getAChannel();
	private Counter BChannel = appCtx.getBChannel();

	private int lastCount;
	private double lastTime;
	private double lastInterval;
	private double speed = 0;

	private double lastVelocity = 0;

	public double getVelocity() {
		double current = Timer.getFPGATimestamp();
		int counter = AChannel.get();
		double dt = 0;
		double interval = 0;

		interval = current - lastInterval;
		lastInterval = current;

		if (counter - lastCount >= 1000) {
			dt = current - lastTime;
			speed = ((counter - lastCount) * 2 * Math.PI) / (2048 * dt);
			lastTime = current;
			lastCount = counter;
		}
		return speed;
	}

	public void reset(){
		speed = 0;
		lastCount = 0;
		lastInterval = 0;
		lastTime = 0;
	}

	public double velocityAverage(double[] arr){
		double total = 0;
		for(double average: arr){
			total += average;
		}
		return total/(double)arr.length;
	}



}
