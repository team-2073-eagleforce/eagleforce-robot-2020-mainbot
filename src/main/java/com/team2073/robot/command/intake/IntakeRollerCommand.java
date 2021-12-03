package com.team2073.robot.command.intake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Mediator;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.IntermediateSubsystem;

public class IntakeRollerCommand extends AbstractLoggingCommand {
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private IntakeSubsystem intakeSubsystem = appCtx.getIntakeSubsystem();
    private HopperSubsystem hopperSubsystem = appCtx.getHopperSubsystem();
    private IntermediateSubsystem intermediateSubsystem = appCtx.getIntermediateSubsystem();

    @Override
    protected void executeDelegate() {
        intakeSubsystem.setRollerState(IntakeSubsystem.IntakeRollerState.INTAKE);
        intermediateSubsystem.set(IntermediateSubsystem.IntermediateState.STOP);
        hopperSubsystem.setState(HopperSubsystem.HopperState.STOP);
    }

    @Override
    protected void endDelegate() {
        intakeSubsystem.setRollerState(IntakeSubsystem.IntakeRollerState.STOP);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
