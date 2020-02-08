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

	private int bLastCount;
	private int aLastCount;
	private double aLastTime;
	private double bLastTime;
	private double aInterval;
	private double bInterval;
	private double aSpeed = 0;
	private double bSpeed = 0;

	private double lastVelocity = 0;

	public double getVelocity() {
		return (aVelocity() + bVelocity()) / 2d;
	}

	public double aVelocity(){
		double current = Timer.getFPGATimestamp();
		int counter = AChannel.get();
		double dt = 0;
		double interval = 0;

		interval = current - aInterval;
		aInterval = current;

		if (counter - aLastCount >= 1000) {
			dt = current - aLastTime;
			aSpeed = ((counter - aLastCount) * 2 * Math.PI) / (2048 * dt);
			aLastTime = current;
			aLastCount = counter;
		}
		return aSpeed;
	}

	public double bVelocity(){
		double current = Timer.getFPGATimestamp();
		int counter = BChannel.get();
		double dt = 0;
		double interval = 0;

		interval = current - bInterval;
		bInterval = current;

		if (counter - bLastCount >= 1000) {
			dt = current - bLastTime;
			bSpeed = ((counter - bLastCount) * 2 * Math.PI) / (2048 * dt);
			bLastTime = current;
			bLastCount = counter;
		}
		return bSpeed;
	}




	public void reset(){
		aSpeed = 0;
		bSpeed = 0;
		aLastCount = 0;
		bLastCount = 0;
		aInterval = 0;
		bInterval = 0;
		aLastTime = 0;
		bLastTime = 0;
	}

}
