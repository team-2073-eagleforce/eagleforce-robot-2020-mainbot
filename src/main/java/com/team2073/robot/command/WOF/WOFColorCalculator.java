package com.team2073.robot.command.WOF;

import com.team2073.robot.subsystem.WOFManipulatorSubsystem.WOFColor;

import java.util.HashMap;
import java.util.Map;

public class WOFColorCalculator {
    private Map<String, Double> decisionMap = new HashMap<>();

    public WOFColorCalculator() {
        decisionMap.put(new WOFColorCombo(WOFColor.RED, WOFColor.GREEN).toString(), -45d);
        decisionMap.put(new WOFColorCombo(WOFColor.RED, WOFColor.BLUE).toString(), 90d);
        decisionMap.put(new WOFColorCombo(WOFColor.RED, WOFColor.YELLOW).toString(), 45d);
        decisionMap.put(new WOFColorCombo(WOFColor.RED, WOFColor.RED).toString(), 0d);

        decisionMap.put(new WOFColorCombo(WOFColor.GREEN, WOFColor.RED).toString(), 45d);
        decisionMap.put(new WOFColorCombo(WOFColor.GREEN, WOFColor.YELLOW).toString(), 90d);
        decisionMap.put(new WOFColorCombo(WOFColor.GREEN, WOFColor.BLUE).toString(), -45d);
        decisionMap.put(new WOFColorCombo(WOFColor.GREEN, WOFColor.GREEN).toString(), 0d);

        decisionMap.put(new WOFColorCombo(WOFColor.BLUE, WOFColor.RED).toString(), 90d);
        decisionMap.put(new WOFColorCombo(WOFColor.BLUE, WOFColor.GREEN).toString(), 45d);
        decisionMap.put(new WOFColorCombo(WOFColor.BLUE, WOFColor.YELLOW).toString(), -45d);
        decisionMap.put(new WOFColorCombo(WOFColor.BLUE, WOFColor.BLUE).toString(), 0d);

        decisionMap.put(new WOFColorCombo(WOFColor.YELLOW, WOFColor.RED).toString(), -45d);
        decisionMap.put(new WOFColorCombo(WOFColor.YELLOW, WOFColor.BLUE).toString(), 45d);
        decisionMap.put(new WOFColorCombo(WOFColor.YELLOW, WOFColor.GREEN).toString(), 90d);
        decisionMap.put(new WOFColorCombo(WOFColor.YELLOW, WOFColor.YELLOW).toString(), 0d);
    }

    public double getSetpoint(String colorCombo) {
        System.out.println(decisionMap.get(colorCombo));
        return decisionMap.get(colorCombo);
    }
}
