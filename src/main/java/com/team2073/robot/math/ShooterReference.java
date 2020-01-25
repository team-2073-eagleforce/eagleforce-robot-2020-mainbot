package com.team2073.robot.math;

import com.team2073.common.subsys.ExampleAppConstants;
import com.team2073.common.util.ConversionUtil;
import com.team2073.common.util.MathUtil;
import org.opencv.core.Mat;

public class ShooterReference {

	private static final double GRAVITY = 9.81;
	private static final double TARGET_HEIGHT = ConversionUtil.feetToMeters(8); // Check this number

	private static final double ELEVATOR_HIGH = ConversionUtil.inchesToMeters(45);
	private static final double ELEVATOR_LOW = ConversionUtil.inchesToMeters(24);

	private double ballVelocity;
	private double shooterReference;

	public double createShooterReference() {
		return 0;
	}

	public double generateBallVelocity(double distanceFromTarget, ElevatorHeight elevatorHeight, ShooterAngle angle) {
		return Math.sqrt((GRAVITY * Math.pow(distanceFromTarget, 2)) / (Math.pow(MathUtil.degreeCosine(angle.getAngle()), 2) * -2 * ((TARGET_HEIGHT - elevatorHeight.getHeight()) - distanceFromTarget * MathUtil.degreeTangent(angle.getAngle()))));
	}

	public double getShooterReference() {
		return shooterReference;
	}

	public enum ElevatorHeight {
		HIGH(ConversionUtil.inchesToMeters(45)),
		LOW(ConversionUtil.inchesToMeters(24));

		private Double height;
		ElevatorHeight(Double height) {
			this.height = height;
		}
		public Double getHeight(){
			return height;
		}
	}

	public enum ShooterAngle {
		IN(25.0),
		EXTENDED(53.0);

		private Double angle;
		ShooterAngle(Double angle) { this.angle = angle; }
		public Double getAngle() { return angle; }
	}

}
