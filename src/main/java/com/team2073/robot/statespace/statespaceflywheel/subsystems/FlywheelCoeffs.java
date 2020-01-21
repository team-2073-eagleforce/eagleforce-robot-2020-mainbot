package com.team2073.robot.statespace.statespaceflywheel.subsystems;


import com.team2073.robot.statespace.controller.StateSpaceControllerCoeffs;
import com.team2073.robot.statespace.controller.StateSpaceLoop;
import com.team2073.robot.statespace.controller.StateSpaceObserverCoeffs;
import com.team2073.robot.statespace.controller.StateSpacePlantCoeffs;
import com.team2073.robot.statespace.math.Matrix;
import com.team2073.robot.statespace.math.MatrixUtils;
import com.team2073.robot.statespace.math.Nat;
import com.team2073.robot.statespace.math.numbers.N1;

public class FlywheelCoeffs {
  public static StateSpacePlantCoeffs<N1, N1, N1> makeFlywheelPlantCoeffs() {
    Matrix<N1, N1> A = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.9841271199191969);
    Matrix<N1, N1> B = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(1.3040238597692997);
    Matrix<N1, N1> C = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(1.0);
    Matrix<N1, N1> D = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.0);
    return new StateSpacePlantCoeffs<N1, N1, N1>(Nat.N1(), Nat.N1(), Nat.N1(), A, B, C, D);
  }


  public static StateSpaceControllerCoeffs<N1, N1, N1>
    makeFlywheelControllerCoeffs() {
    Matrix<N1, N1> K = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.5971095281145458);
    Matrix<N1, N1> Kff = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.5762423360059603);
    Matrix<N1, N1> Umin = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(-12.0);
    Matrix<N1, N1> Umax = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(12.0);
    return new StateSpaceControllerCoeffs<N1, N1, N1>(K, Kff, Umin, Umax);
  }


  public static StateSpaceObserverCoeffs<N1, N1, N1>
    makeFlywheelObserverCoeffs() {
    Matrix<N1, N1> K = MatrixUtils.mat(Nat.N1(), Nat.N1()).fill(0.9999000196802197);
    return new StateSpaceObserverCoeffs<N1, N1, N1>(K);
  }


  public static StateSpaceLoop<N1, N1, N1> makeFlywheelLoop() {
    return new StateSpaceLoop<N1, N1, N1>(makeFlywheelPlantCoeffs(),
                                          makeFlywheelControllerCoeffs(),
                                          makeFlywheelObserverCoeffs());
  }
}
