package com.team2073.robot.command.turret;

import com.team2073.common.command.AbstractLoggingCommand;

public class HoodRetractCommand extends AbstractLoggingCommand {
	@Override
	protected boolean isFinishedDelegate() {
		return false;
	}
}
