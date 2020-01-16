package com.team2073.robot.statespace.shooter;

import com.team2073.robot.statespace.controller.StateSpaceLoop;
import com.team2073.robot.statespace.math.Matrix;
import com.team2073.robot.statespace.math.MatrixUtils;
import com.team2073.robot.statespace.math.Nat;
import com.team2073.robot.statespace.math.numbers.N1;
import com.team2073.robot.statespace.math.numbers.N2;

public class ShooterController {
    // State tolerances in meters and meters/sec respectively.
    public static final double kAngleTolerance = 0.05;
    public static final double kAngularVelocityTolerance = 2.0;

    // The current sensor measurement.
    private final Matrix<N1, N1> m_Y;

    // The control loop
    private final StateSpaceLoop<N2, N1, N1> ctrlLoop;

    private boolean m_atReferences;

    public ShooterController() {
        m_Y = MatrixUtils.zeros(Nat.N1());
        ctrlLoop = ShooterCoeffs.makeShooterLoop();
    }

    public void enable() {
        ctrlLoop.enable();
    }

    public void disable() {
        ctrlLoop.disable();
    }

    /**
     * Sets the references.
     *
     * @param angularVelocity   Angular velocity of flywheel in radians per second.
     */
    public void setReferences(double angularVelocity) {
        Matrix<N2, N1> nextR = MatrixUtils.mat(Nat.N2(), Nat.N1()).fill(angularVelocity, 0);
        ctrlLoop.setNextR(nextR);
    }

    public boolean atReferences() {
        return m_atReferences;
    }

    /**
     * Sets the current encoder measurement.
     *
     * @param measuredVelocity Velocity of shooter in radians/sec.
     */
    public void setMeasuredVelocity(double measuredVelocity) {
        m_Y.set(0, 0, measuredVelocity);
    }

    public double getControllerVoltage() {
        return ctrlLoop.getU(0);
    }

    public double getEstimatedVelocity() {
        return ctrlLoop.getXhat(0);
    }

    public double getVelocityError() {
        return ctrlLoop.getError().get(0, 0);
    }

    public double getVoltageError() {return ctrlLoop.getError().get(1, 0); }

    /**
     * Executes the control loop for a cycle.
     */
    public void update() {
        ctrlLoop.correct(m_Y);

        Matrix error = ctrlLoop.getError();
        m_atReferences = Math.abs(error.get(0, 0)) < kAngleTolerance
                && Math.abs(error.get(1, 0)) < kAngularVelocityTolerance;

        ctrlLoop.predict();
    }

    /**
     * Resets any internal state.
     */
    public void reset() {
        ctrlLoop.reset();
    }
}
