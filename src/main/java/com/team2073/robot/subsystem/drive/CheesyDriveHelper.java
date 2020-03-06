package com.team2073.robot.subsystem.drive;

import static com.team2073.robot.subsystem.drive.Constants.CheesyDriveConstants.*;

/**
 * Helper class to implement "Cheesy Drive". "Cheesy Drive" simply means that the "turning" stick controls the curvature
 * * of the robot's path rather than its rate of heading change. This helps make the robot more controllable at high
 * * speeds. Also handles the robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
 * turn-in-place maneuvers.
 */
public class CheesyDriveHelper {


    private double mOldWheel = 0.0;
    private double mQuickStopAccumlator = 0.0;
    private double mNegInertiaAccumlator = 0.0;

    public static double limit(double v, double min, double max) {
        return Math.min(max, Math.max(min, v));
    }

    public static double limit(double v, double maxMagnitude) {
        return limit(v, -maxMagnitude, maxMagnitude);
    }

    public DriveSignal cheesyDrive(double throttle, double wheel, boolean isQuickTurn) {

        wheel = handleDeadband(wheel, kWheelDeadband);
        throttle = handleDeadband(throttle, kThrottleDeadband);

        double negInertia = wheel - mOldWheel;
        mOldWheel = wheel;

        double wheelNonLinearity;
        wheelNonLinearity = kHighWheelNonLinearity;
        final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        // Apply a sin function that's scaled to make it feel better.
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
        wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaScalar;
        negInertiaScalar = kHighNegInertiaScalar;
        sensitivity = kHighSensitivity;
        double negInertiaPower = negInertia * negInertiaScalar;
        mNegInertiaAccumlator += negInertiaPower;

        wheel = wheel + mNegInertiaAccumlator;
        if (mNegInertiaAccumlator > 1) {
            mNegInertiaAccumlator -= 1;
        } else if (mNegInertiaAccumlator < -1) {
            mNegInertiaAccumlator += 1;
        } else {
            mNegInertiaAccumlator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < kQuickStopDeadband) {
                double alpha = kQuickStopWeight;
                mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator
                        + alpha * limit(wheel, 1.0) * kQuickStopScalar;
            }
            overPower = 1.0;
            angularPower = wheel * kPointTurnSensitivity;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity - mQuickStopAccumlator;
//            if(throttle > 0){
//                angularPower = throttle * wheel * sensitivity - mQuickStopAccumlator;
//            }else{
//                angularPower = throttle * wheel * sensitivity + mQuickStopAccumlator;
//            }
            if (mQuickStopAccumlator > 1) {
                mQuickStopAccumlator -= 1;
            } else if (mQuickStopAccumlator < -1) {
                mQuickStopAccumlator += 1;
            } else {
                mQuickStopAccumlator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
        return new DriveSignal(leftPwm, rightPwm);
    }

    public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
}
