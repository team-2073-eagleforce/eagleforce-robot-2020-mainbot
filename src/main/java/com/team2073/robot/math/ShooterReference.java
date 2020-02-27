package com.team2073.robot.math;

import com.team2073.common.util.ConversionUtil;
import com.team2073.common.util.MathUtil;

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

    public double generateBallVelocity(double distanceFromTarget, double elevatorHeight, ShooterAngle angle) {
        return Math.sqrt((GRAVITY * Math.pow(distanceFromTarget, 2)) /
                (Math.pow(MathUtil.degreeCosine(angle.getAngle()), 2) * -2 * ((TARGET_HEIGHT -
                        ConversionUtil.inchesToMeters(elevatorHeight)) - distanceFromTarget *
                        MathUtil.degreeTangent(angle.getAngle()))));
    }

    public double getShooterReference() {
        return shooterReference;
    }

    public enum ShooterAngle {
        IN(25.0),
        EXTENDED(53.0);

        private Double angle;

        ShooterAngle(Double angle) {
            this.angle = angle;
        }

        public Double getAngle() {
            return angle;
        }
    }

}
