package com.team2073.robot.command;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class InverseTrigger extends Trigger {
    private Trigger trigger;

    public InverseTrigger(Trigger trigger){
        this.trigger = trigger;
    }
    @Override
    public boolean get() {
        return !trigger.get();
    }
}
