package com.team2073.robot.ctx;

import edu.wpi.first.wpilibj.Joystick;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private static ApplicationContext instance;
    private Joystick controller;
    private Joystick driveWheel;
    private Joystick driveStick;

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }
    public Joystick getController() {
        if(controller == null){
            controller = new Joystick(CONTROLLER_PORT);
        }
        return controller;
    }
    public Joystick getDriveWheel() {
        if (driveWheel == null){
            driveWheel = new Joystick(WHEEL_PORT);
        }
        return driveWheel;
    }
    public Joystick getDriveStick() {
        if (driveStick == null){
            driveStick = new Joystick(JOYSTICK_PORT);
        }
        return driveStick;
    }
}
