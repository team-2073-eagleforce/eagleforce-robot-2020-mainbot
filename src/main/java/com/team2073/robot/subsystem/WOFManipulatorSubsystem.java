package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.util.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WOFManipulatorSubsystem implements PeriodicRunnable {
    private static ApplicationContext appCtx = ApplicationContext.getInstance();
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    TalonSRX talonSRX = new TalonSRX(1);
    String gameData = DriverStation.getInstance().getGameSpecificMessage();
    // private Color kGreenTarget = getTarget("Green");
    // private Color kRedTarget = getTarget("Red");
    // private Color kYellowTarget = getTarget("Yellow");
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
    // private Color kBlueTarget = getTarget("Blue");
    private int rotations = 0;
    private static File file = new File(System.getProperty("user.home"), '/' + "RGB.csv");
    private String currentColor;
    private String goalColor;
    private boolean setGoalColor = false;
    private String previousColor = "Grey";

    @Override
    public void onPeriodic() {
    }

/*    private void calibrateColors() {
        if (appCtx.getController().getRawButton(1)) {
            setColor(kRedTarget, "Red");
        }
        if (appCtx.getController().getRawButton(2)) {
            setColor(kBlueTarget, "Blue");
        }
        if (appCtx.getController().getRawButton(3)) {
            setColor(kGreenTarget, "Green");
        }
        if (appCtx.getController().getRawButton(4)) {
            setColor(kYellowTarget, "Yellow");
        }
    }*/

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

    private Double getRed() {
        return (m_colorSensor.getRawColor().red) / 255d;
    }

    private Double getGreen() {
        return (m_colorSensor.getRawColor().green) / 255d;
    }

    private Double getBlue() {
        return (m_colorSensor.getRawColor().blue) / 255d;
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

    public void setMotor(double output) {
        talonSRX.set(ControlMode.PercentOutput, output);
    }

    public void rotationCounter() {
        currentColor = readColor();
        if (!setGoalColor) {
            setGoalColor = true;
            goalColor = currentColor;
        }
        if (readColor().equals(currentColor) && !previousColor.equals(currentColor)) {
            rotations += 1;
        }
        previousColor = currentColor;
    }

    public void stopOnRotation() {
        if (rotations >= 7) {
            setMotor(0d);
        } else {
            setMotor(0.1);
        }
    }

    public void stopOnColor() {
        if (readColor().equals("Red")) {
            setMotor(0d);
        } else {
            setMotor(0.2);
        }
    }

    public void calibrate() {
        Joystick controller = ApplicationContext.getInstance().getController();
        if (controller.getRawButtonPressed(1)) {
            updateFile("Green", getRed().toString(), getGreen().toString(), getBlue().toString());
        } else if (controller.getRawButtonPressed(2)) {
            updateFile("Red", getRed().toString(), getGreen().toString(), getBlue().toString());
        } else if (controller.getRawButtonPressed(3)) {
            updateFile("Blue", getRed().toString(), getGreen().toString(), getBlue().toString());
        } else if (controller.getRawButtonPressed(4)) {
            updateFile("Yellow", getRed().toString(), getGreen().toString(), getBlue().toString());
        }

    }

    public static void createCSV() {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateFile("Color", "R", "G", "B");
    }

    private static void updateFile(String... data) {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true));
            csvWriter.writeNext(data);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getColorValues(String color) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            List<String[]> records = csvReader.readAll();
            for (String[] record : records) {
                if (record[0].equals(color)) {
                    return record;
                }
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        System.err.println("File not Found");
        return new String[]{"Null Pointer Generated"};
    }

    private double getR(String color) {
        return Double.parseDouble(getColorValues(color)[1]);
    }

    private double getG(String color) {
        return Double.parseDouble(getColorValues(color)[2]);
    }

    private double getB(String color) {
        return Double.parseDouble(getColorValues(color)[3]);
    }

    private Color getTarget(String color) {
        return ColorMatch.makeColor(getR(color), getG(color), getB(color));
    }


}

