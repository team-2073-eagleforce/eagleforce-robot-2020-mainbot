package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;

public class TurretSubsystem implements AsyncPeriodicRunnable {

    private RobotContext robotContext = RobotContext.getInstance();
    private ApplicationContext appCtx = ApplicationContext.getInstance();

    private TalonFX turretMotor = appCtx.getTurretMotor();


    @Override
    public void onPeriodicAsync() {

    }

    private void set(double position) {

    }

    private void setMotor(double output) {
        turretMotor.set(ControlMode.PercentOutput, output);
    }

}
