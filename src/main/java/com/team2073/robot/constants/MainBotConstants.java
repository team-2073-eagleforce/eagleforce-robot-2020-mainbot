package com.team2073.robot.constants;

import java.lang.reflect.Field;

public class MainBotConstants {

    public int WHEEL_PORT = 0;
    public int JOYSTICK_PORT = 1;
    public int CONTROLLER_PORT = 2;
    //Elevator
    public int ELEVATOR_BOTTOM_DIO_PORT = 5;
    public int ELEVATOR_MOTOR_PORT = 54;
    //Intake
    public int INTAKE_MOTOR_PORT = 7;
    public int INTAKE_SOLENOID_TOP_PORT = 3;
    public int INTAKE_SOLENOID_BOTTOM_PORT = 2;
    //Hopper
    public int HOPPER_MOTOR_ID = 4;
    public int HOPPER_SENSOR_DIO_PORT = 6;
    //Drive
    public int DRIVE_LEFT_MASTER = 2;
    public int DRIVE_LEFT_SLAVE_ONE = 1;
    public int DRIVE_LEFT_SLAVE_TWO = 16;
    public int DRIVE_RIGHT_MASTER = 13;
    public int DRIVE_RIGHT_SLAVE_ONE = 14;
    public int DRIVE_RIGHT_SLAVE_TWO = 15;
    public int GYRO_PORT = 17;
    //WOF
    public int WOF_ENCODER_A_DIO_PORT = 8;
    public int WOF_ENCODER_B_DIO_PORT = 7;
    //turret
    public int TURRET_MOTOR_PORT = 8;
    public int TURRET_POT_ANALOG_PORT = 3;
    //Shooter
    public int SHOOTER_MASTER = 3;
    public int SHOOTER_SLAVE_ONE = 11;
    public int SHOOTER_SLAVE_TWO = 5;
    public int SHOOTER_ENCODER_A_DIO = 9;
    public int HOOD_SERVO_PWM_PORT = 9;
    //Intermediate
    public int INTERMEDIATE_NEO = 10; //change number
    public int INTERMEDIATE_BAG = 12; //change number;
    //Climb
    public int CLIMB_PISTON_SOLENOID = 4;
    public int CLIMB_PTO_SOLENOID = 5;
    // Control System
    public int PDB_PORT = 0;
    public double MAX_DRIVE_PERCENT_IN_CLIMB = .3;
    public double NO_TARGET_RPM = 7000d;
    public double DEFAULT_LONG_RPM = 6250;
    public double ZOOM_RANGE_INCHES = 12 * 20;
    public double LIMELIGHT_LOW_HEIGHT = 25d;
    public double LIMELIGHT_HIGH_HEIGHT = LIMELIGHT_LOW_HEIGHT + 11d;
//    public double LIMELIGHT_LENS_ANGLE = 15d;
    public double LIMELIGHT_LENS_ANGLE = 16d;

    public double TARGET_HEIGHT = 81;

    public void applyMainbotChanges() throws NoSuchFieldException, IllegalAccessException {
        AppConstants.getInstance();
        for (Field f : AppConstants.class.getFields()) {
            System.out.println(f);
            if (!f.getName().equals("instance")) {
                if (f.getType() == double.class) {
                    f.setDouble(AppConstants.getInstance(), this.getClass().getField(f.getName()).getDouble(this));
                } else if (f.getType() == int.class) {
                    f.setInt(AppConstants.getInstance(), this.getClass().getField(f.getName()).getInt(this));
                }
            }
        }
    }
}
