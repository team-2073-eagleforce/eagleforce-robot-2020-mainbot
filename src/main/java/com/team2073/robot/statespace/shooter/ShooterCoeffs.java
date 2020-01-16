package com.team2073.robot.statespace.shooter;

import com.team2073.robot.statespace.controller.StateSpaceControllerCoeffs;
import com.team2073.robot.statespace.controller.StateSpaceLoop;
import com.team2073.robot.statespace.controller.StateSpaceObserverCoeffs;
import com.team2073.robot.statespace.controller.StateSpacePlantCoeffs;
import com.team2073.robot.statespace.math.*;
import com.team2073.robot.statespace.math.numbers.*;

public class ShooterCoeffs {
  public static StateSpacePlantCoeffs<N2, N1, N1> makeShooterPlantCoeffs() {
    Matrix<N2, N2> A = MatrixUtils.mat(Nat.N2(), Nat.N2()).fill(0.98412712, 1.30402386, 0, 0);
    Matrix<N2, N1> B = MatrixUtils.mat(Nat.N2(), Nat.N1()).fill(1.30402386, 0);
    Matrix<N1, N2> C = MatrixUtils.mat(Nat.N1(), Nat.N2()).fill(1.0, 0);
    Matrix<N1, N1> D = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.0);
    return new StateSpacePlantCoeffs<N2, N1, N1>(Nat.N2(), Nat.N1(), Nat.N1(), A, B, C, D);
  }

  public static StateSpaceControllerCoeffs<N2, N1, N1>
    makeShooterControllerCoeffs() {
    Matrix<N1, N2> K = MatrixUtils.mat(Nat.N1(), Nat.N2()).fill(0.59710953, 1);
    Matrix<N1, N2> Kff = MatrixUtils.mat(Nat.N1(), Nat.N2()).fill(0.57624234, 1);
    Matrix<N1, N1> Umin = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(-12.0);
    Matrix<N1, N1> Umax = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(12.0);
    return new StateSpaceControllerCoeffs<N2, N1, N1>(K, Kff, Umin, Umax);
  }

  public static StateSpaceObserverCoeffs<N2, N1, N1>
    makeShooterObserverCoeffs() {
    Matrix<N2, N1> K = MatrixUtils.mat(Nat.N2(), Nat.N1()).fill(0.9999000196802197);
    return new StateSpaceObserverCoeffs<N2, N1, N1>(K);
  }

  public static StateSpaceLoop<N2, N1, N1> makeShooterLoop() {
    return new StateSpaceLoop<N2, N1, N1>(makeShooterPlantCoeffs(),
                                          makeShooterControllerCoeffs(),
                                          makeShooterObserverCoeffs());
  }
}
