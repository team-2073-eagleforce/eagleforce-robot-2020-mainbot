package com.team2073.robot.command.shooter;

import com.team2073.robot.ApplicationContext;
import com.team2073.robot.subsystem.FlywheelSubsystem;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class RPMTrigger extends Trigger {
    ApplicationContext appCtx = ApplicationContext.getInstance();
    FlywheelSubsystem flywheel = appCtx.getFlywheelSubsystem();

    @Override
    public boolean get() {
        return flywheel.atReference();
    }
}
