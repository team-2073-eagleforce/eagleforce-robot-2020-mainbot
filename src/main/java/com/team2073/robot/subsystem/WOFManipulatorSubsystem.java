package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.team2073.common.periodic.PeriodicRunnable;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import java.util.ArrayList;

public class WOFManipulatorSubsystem implements PeriodicRunnable {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private TalonSRX talonSRX = new TalonSRX(1);
    private Color kBlueTarget = ColorMatch.makeColor(60 / 255d, 136 / 255d, 107 / 255d);
    private Color kGreenTarget = ColorMatch.makeColor(88 / 255d, 170 / 255d, 67 / 255d);
    private Color kRedTarget = ColorMatch.makeColor(113 / 255d, 65 / 255d, 27 / 255d);
    private Color kYellowTarget = ColorMatch.makeColor(150 / 255d, 150 / 255d, 45 / 255d);

    @Override
    public void onPeriodic() {
//        if (controller.getRawButton(1)) {
//            stopOnColor("Red");
//        }
    }

    private void calibrateColors() {
       /* if (controller.getRawButton(1)) {
            setColor(kRedTarget, "Red");
        }
        if (controller.getRawButton(2)) {
            setColor(kBlueTarget, "Blue");
        }
        if (controller.getRawButton(3)) {
            setColor(kGreenTarget, "Green");
        }
        if (controller.getRawButton(4)) {
            setColor(kYellowTarget, "Yellow");
        }*/
    }

    public void addColorMatch() {
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);
    }

    public String readColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        String colorString;

        if (match.color == kBlueTarget) {
            colorString = "Blue";
        } else if (match.color == kRedTarget) {
            colorString = "Red";
        } else if (match.color == kGreenTarget) {
            colorString = "Green";
        } else if (match.color == kYellowTarget) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }
        return colorString;
    }

    private double getRed() {
        return (m_colorSensor.getRawColor().red) / 255d;
    }

    private double getGreen() {
        return (m_colorSensor.getRawColor().green) / 255d;
    }

    private double getBlue() {
        return (m_colorSensor.getRawColor().blue) / 255d;
    }

    private void getRGBValues() {
        System.out.println("Red: " + getRed() + " \t Green: " + getGreen() + " \t Blue: " + getBlue());
    }

    public ArrayList<Double> getColors(Color color) {
        ArrayList<Double> colorArray = new ArrayList<>();
        colorArray.add(color.red);
        colorArray.add(color.blue);
        colorArray.add(color.green);

        return colorArray;
    }

    private void setColor(Color color, String colorName) {
        color = ColorMatch.makeColor(getRed() / 255d, getGreen() / 255d, getBlue() / 255d);
        System.out.println(colorName + " set: " + getColors(color).toString());
    }

    private void motorOn() {
        talonSRX.set(ControlMode.PercentOutput, .20);
    }

    private void motorOff() {
        talonSRX.set(ControlMode.PercentOutput, 0);
    }

    private void stopOnColor(String colorName) {
        if (readColor().equals(colorName)) {
            motorOff();
        } else {
            motorOn();
        }
    }

}

