package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.command.WOF.WOFColorCalculator;
import com.team2073.robot.command.WOF.WOFColorCombo;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.util.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WOFManipulatorSubsystem implements PeriodicRunnable {
    private static ApplicationContext appCtx = ApplicationContext.getInstance();

    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private TalonSRX WOFMotor = appCtx.getBagMotor();

    private static File file = new File(System.getProperty("user.home"), '/' + "RGB.csv");
    private WOFColorCalculator wofColorCalculator = new WOFColorCalculator();
    private Map<String, WOFColor> colorMap = new HashMap<>();
    private Map<String, String> offsetColor = new HashMap<>();

    private Encoder wofEncoder = appCtx.getWofEncoder();

    private Color rioGreenTarget;
    private Color rioRedTarget;
    private Color rioYellowTarget;
    private Color rioBlueTarget;
//    private Color rioGreenTarget = getTargetFromFile("Green");
//    private Color rioRedTarget = getTargetFromFile("Red");
//    private Color rioYellowTarget = getTargetFromFile("Yellow");
//    private Color rioBlueTarget = getTargetFromFile("Blue");

    private Double setpoint = null;
    private MotionProfileControlloop positionControlLoop = new MotionProfileControlloop(0.008, 0d, .4d / 800, 0d, 0.7d);
    private ProfileConfiguration positionConfiguration = new ProfileConfiguration(800d, 800 / .5, .01);
    private TrapezoidalProfileManager positionManager = new TrapezoidalProfileManager(positionControlLoop, positionConfiguration, this::position);

    //  360 / ((32 / 2.875) * (1 / 2048) * tics) tics to degrees
    //  (int) ((360 / position) * (2.875 / 32) * 2048) degrees to tics


    public WOFManipulatorSubsystem() {
        offsetColor.put("Yellow", "Red");
        offsetColor.put("Red", "Blue");
        offsetColor.put("Green", "Yellow");
        offsetColor.put("Blue", "Red");
        colorMap.put("Red", WOFColor.RED);
        colorMap.put("Green", WOFColor.GREEN);
        colorMap.put("Blue", WOFColor.BLUE);
        colorMap.put("Yellow", WOFColor.YELLOW);
        addColorMatch();
        wofEncoder.reset();
    }

    @Override
    public void onPeriodic() {
    }

    private String getGameData() {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0) {
            switch (gameData.charAt(0)) {
                case 'B':
                    return "Blue";
                case 'G':
                    return "Green";
                case 'R':
                    return "Red";
                case 'Y':
                    return "Yellow";
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    private void addColorMatch() {
        m_colorMatcher.addColorMatch(rioBlueTarget);
        m_colorMatcher.addColorMatch(rioGreenTarget);
        m_colorMatcher.addColorMatch(rioRedTarget);
        m_colorMatcher.addColorMatch(rioYellowTarget);
    }

    private String readColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        String colorString;

        if (match.color == rioBlueTarget) {
            colorString = "Blue";
        } else if (match.color == rioRedTarget) {
            colorString = "Red";
        } else if (match.color == rioGreenTarget) {
            colorString = "Green";
        } else if (match.color == rioYellowTarget) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }
        return colorString;
    }

    private Double getSensorRed() {
        return (m_colorSensor.getRawColor().red) / 255d;
    }

    private Double getSensorGreen() {
        return (m_colorSensor.getRawColor().green) / 255d;
    }

    private Double getSensorBlue() {
        return (m_colorSensor.getRawColor().blue) / 255d;
    }

    public void setWOFMotor(double output) {
        WOFMotor.set(ControlMode.PercentOutput, output);
    }

    public void positionControl() {
        if (!getGameData().equals("")) {
            if (setpoint == null) {
                wofEncoder.reset();
                setpoint = wofColorCalculator.getSetpoint(new WOFColorCombo(colorMap.get(offsetColor.get(getGameData())), colorMap.get(offsetColor.get(readColor()))).toString());
            } else {
                positionManager.setPoint(setpoint);
                positionManager.newOutput();
                setWOFMotor(positionManager.getOutput());
            }

            //System.out.println("setpoint: " + setpoint + " output: " + positionManager.getOutput() + " position: " + position() + " color: " + readColor());
        }
    }

    public void rotationControl() {
        if (setpoint == null) {
            wofEncoder.reset();
            setpoint = 360 * 4.0;
        } else {
            positionManager.setPoint(setpoint);
            positionManager.newOutput();
            setWOFMotor(positionManager.getOutput());
        }
        //System.out.println("setpoint: " + setpoint + " output: " + positionManager.getOutput() + " position: " + position() + " color: " + readColor());
    }

    private double position() {
        return (32.34375 * wofEncoder.get()) / 2048;
    }

    public void calibrate() {
        Joystick controller = ApplicationContext.getInstance().getController();
        if (controller.getRawButtonPressed(1)) {
            updateFile("Green", getSensorRed().toString(), getSensorGreen().toString(), getSensorBlue().toString());
        } else if (controller.getRawButtonPressed(2)) {
            updateFile("Red", getSensorRed().toString(), getSensorGreen().toString(), getSensorBlue().toString());
        } else if (controller.getRawButtonPressed(3)) {
            updateFile("Blue", getSensorRed().toString(), getSensorGreen().toString(), getSensorBlue().toString());
        } else if (controller.getRawButtonPressed(4)) {
            updateFile("Yellow", getSensorRed().toString(), getSensorGreen().toString(), getSensorBlue().toString());
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

    private double getRFromFile(String color) {
        try {
            return Double.parseDouble(getColorValues(color)[1]);
        } catch (Exception e) {
            return defaultValues(color)[1];
        }
    }

    private double getGFromFile(String color) {
        try {
            return Double.parseDouble(getColorValues(color)[2]);
        } catch (Exception e) {
            return defaultValues(color)[2];
        }
    }

    private double getBFromFile(String color) {
        try {
            return Double.parseDouble(getColorValues(color)[3]);
        } catch (Exception e) {
            return defaultValues(color)[3];
        }
    }

    private Color getTargetFromFile(String color) {
        return ColorMatch.makeColor(getRFromFile(color), getGFromFile(color), getBFromFile(color));
    }

    public enum WOFColor {
        RED,
        GREEN,
        BLUE,
        YELLOW;
    }

    public void setMotionSetpoint(Double setpoint) {
        this.setpoint = setpoint;
    }

    private double[] defaultValues(String color) {
        switch (color) {
            case "Red":
                return new double[]{0.561, 0.232, 0.114};
            case "Blue":
                return new double[]{0.143, 0.427, 0.429};
            case "Green":
                return new double[]{0.197, 0.561, 0.240};
            case "Yellow":
                return new double[]{0.361, 0.524, 0.113};
            default:
                return new double[]{0, 0, 0};
        }
    }
}

