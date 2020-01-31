package com.team2073.robot.command.turret;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.TurretSubsystem;

public class HoodExtendCommand extends AbstractLoggingCommand {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	@Override
	protected void initializeDelegate() {
		appCtx.getTurretSubsystem().set(TurretSubsystem.HoodState.EXTENDED);

	}

	@Override
	protected boolean isFinishedDelegate() {
		return false;
	}
}
