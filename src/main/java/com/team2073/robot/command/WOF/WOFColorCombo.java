package com.team2073.robot.command.WOF;

import static com.team2073.robot.subsystem.WOFManipulatorSubsystem.WOFColor;

public class WOFColorCombo {

    private WOFColor target;
    private WOFColor current;


    public WOFColorCombo(WOFColor target, WOFColor current) {
        this.target = target;
        this.current = current;
    }

    public WOFColor getTarget() {
        return target;
    }

    public WOFColor getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "Target: " + target.toString() + " Current: " + current.toString();
    }
}
