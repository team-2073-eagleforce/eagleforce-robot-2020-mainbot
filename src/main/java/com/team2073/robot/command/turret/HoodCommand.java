package com.team2073.robot.command.turret;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ApplicationContext;

public class HoodCommand extends AbstractLoggingCommand {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	@Override
	protected void initializeDelegate() {

	}

	@Override
	protected boolean isFinishedDelegate() {
		return false;
	}
}
