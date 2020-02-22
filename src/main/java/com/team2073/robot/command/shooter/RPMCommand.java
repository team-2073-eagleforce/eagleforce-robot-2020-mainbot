package com.team2073.robot.command.shooter;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class RPMCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Joystick driveStick = appCtx.getDriveStick();
    private Joystick wheel = appCtx.getDriveWheel();
    private Timer timer = new Timer();
    @Override
    protected boolean isFinishedDelegate() {
        return timer.hasPeriodPassed(2);
    }

    @Override
    protected void initializeDelegate() {
        timer.start();
    }

    @Override
    protected void executeDelegate() {
        driveStick.setRumble(GenericHID.RumbleType.kLeftRumble, 1);
        wheel.setRumble(GenericHID.RumbleType.kLeftRumble, 1);
    }

    @Override
    protected void endDelegate() {
        driveStick.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        wheel.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        timer.reset();
    }
}
