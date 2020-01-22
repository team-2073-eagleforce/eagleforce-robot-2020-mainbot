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
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.util.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

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
    private WOFPositionConverter converter = new WOFPositionConverter();

    Timer timer = new Timer();
    private boolean timerStarted = false;
//    private Counter Achannel = new Counter(8);
//    private Counter Bchannel = new Counter(9);

    private Encoder encoder = new Encoder(8, 9);

    private Map<String, String> wofColorsMap = new HashMap<>();
    private Map<String, WOFColor> colorMap = new HashMap<>();

    private MotionProfileControlloop controlloop = new MotionProfileControlloop(.0044, 0d, .5 / 970, 0d, 0.5d);
    private ProfileConfiguration configuration = new ProfileConfiguration(970, 970 / .5, .01);
    private TrapezoidalProfileManager manager = new TrapezoidalProfileManager(controlloop, configuration, this::position);

    public WOFManipulatorSubsystem() {
        wofColorsMap.put("Blue", "Red");
        wofColorsMap.put("Green", "Yellow");
        wofColorsMap.put("Red", "Blue");
        wofColorsMap.put("Yellow", "Green");
        colorMap.put("Red", WOFColor.RED);
        colorMap.put("Green", WOFColor.GREEN);
        colorMap.put("Blue", WOFColor.BLUE);
        colorMap.put("Yellow", WOFColor.YELLOW);

        encoder.reset();
    }

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
            stop(-.1);
            rotations = 0;
        } else {
            setMotor(0.1);
        }
    }

    private String getOffsetColor(String color) {
        return wofColorsMap.get(color);
    }

    public double getDegreeMovement(WOFColor current, WOFColor target) {
        double least = 360;
        double sign = 1;
        if(Math.abs(current.getPosition() - target.getPosition()) < least){
            least = Math.abs(current.getPosition() - target.getPosition());
            sign = Math.signum(current.getPosition() - target.getPosition());
        }else if(Math.abs(current.getPosition() - target.getPosition2()) < least){
            least = Math.abs(current.getPosition() - target.getPosition2());
            sign = Math.signum(current.getPosition() - target.getPosition2());
        }else if(Math.abs(current.getPosition2() - target.getPosition()) < least){
            least = Math.abs(current.getPosition2() - target.getPosition());
            sign = Math.signum(current.getPosition2() - target.getPosition());
        }else if(Math.abs(current.getPosition2() - target.getPosition2()) < least){
            least = Math.abs(current.getPosition2() - target.getPosition2());
            sign = Math.signum(current.getPosition2() - target.getPosition2());
        }else{
            System.out.println("JASON CANT CODE");
        }
        System.out.println(least);
        return position() + least * sign;
//        if (Math.abs(target.position - current.position) < Math.abs(current.position - target.position)) {
//            return (target.position - current.position) * 45;
//        } else {
//            return (current.position - target.position) * 45;
//        }
//        if(target.position - current.position > 0) {
//            return -(position() + target.position - current.position) * 45;
//        } else {
//            return (position() + target.position - current.position) * 45;
//        }
    }
    Double setpoint = null;
    public void stopOnColor() {
        if(setpoint == null){
            setpoint = getDegreeMovement(colorMap.get("Red"), colorMap.get(readColor()));
            System.out.println();
            System.out.println();
        }else {
            manager.setPoint(setpoint);
            manager.newOutput();
            if (readColor().equals("Red")) {
                setMotor(0d);
                setpoint = null;
            } else {
                setMotor(manager.getOutput());
            }
        }

        System.out.println("setpoint: " + setpoint);
    }

    private double position() {
        return (32.34375 * encoder.get()) / 2048;
    }

    public void stop(double stoppingSpeed) {
        if (!timerStarted) {
            timer.start();
            timerStarted = true;
        }
        if (timer.getElapsedTime() < .05) {
            setMotor(stoppingSpeed);
        } else {
            setMotor(0d);
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


    public enum WOFColor {
        RED(0, 180),
        GREEN(45, 225),
        BLUE(90, 270),
        YELLOW(135, 315);

        private double position;
        private double position2;

        WOFColor(double position, double position2) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }

        public double getPosition2(){
            return position2;
        }

        public WOFColor getColor(int position) {
            WOFColor color = null;
            for (WOFColor wofColor : values()) {
                if (wofColor.position == position) {
                    color = wofColor;
                }
            }
            return color;
        }
    }

    private static class WOFPositionConverter implements PositionConverter {

        @Override
        public double asPosition(int tics) {
            return 360 / ((32 / 2.875) * (1 / 2048) * tics);
        }

        @Override
        public int asTics(double position) {
            return (int) ((360 / position) * (2.875 / 32) * 2048);
        }

        @Override
        public String positionalUnit() {
            return Units.DEGREES;
        }
    }
}

