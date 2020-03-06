package com.team2073.robot.command;

import edu.wpi.first.wpilibj.buttons.Trigger;

import java.util.concurrent.Callable;

public class WOFModeTrigger extends Trigger {
    Callable<Boolean> get;

    public WOFModeTrigger(Callable<Boolean> get) {
        this.get = get;
    }

    @Override
    public boolean get() {
        try {
            return get.call();
        } catch (Exception e) {
            return false;
        }
    }
}
