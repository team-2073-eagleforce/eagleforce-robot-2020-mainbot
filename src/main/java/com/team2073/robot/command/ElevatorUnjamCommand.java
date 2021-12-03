package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.command.shooter.ShooterCommand;
import com.team2073.robot.subsystem.ElevatorSubsytem;
import com.team2073.robot.subsystem.FlywheelSubsystem;
import com.team2073.robot.subsystem.IntermediateSubsystem;
import com.team2073.robot.subsystem.TurretSubsystem;

public class ElevatorUnjamCommand extends AbstractLoggingCommand {
    ApplicationContext  appCtx = ApplicationContext.getInstance();
    ElevatorSubsytem elevator = appCtx.getElevatorSubsystem();
    TurretSubsystem turret = appCtx.getTurretSubsystem();
    FlywheelSubsystem flywheel = appCtx.getFlywheelSubsystem();
    IntermediateSubsystem intermediate = appCtx.getIntermediateSubsystem();

    @Override
    protected void executeDelegate() {
        elevator.setElevatorState(ElevatorSubsytem.ElevatorState.UNJAM_HEIGHT);
        turret.setState(TurretSubsystem.TurretState.FACE_FRONT);
        flywheel.setRPM(1000d);
        intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
    }

    @Override
    protected void endDelegate() {
        elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
        turret.setState(TurretSubsystem.TurretState.GYRO);
        flywheel.setRPM(null);
        intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
