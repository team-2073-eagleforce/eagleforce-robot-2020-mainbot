package com.team2073.robot.constants;

public class AppConstants {
    private static AppConstants instance;

    public int WHEEL_PORT = 0;
    public int JOYSTICK_PORT = 1;
    public int CONTROLLER_PORT = 2;
    //Elevator
    public int ELEVATOR_BOTTOM_DIO_PORT = 2;
    public int ELEVATOR_MOTOR_PORT = 54;
    //Intake
    public int INTAKE_MOTOR_PORT = 7;
    public int INTAKE_SOLENOID_TOP_PORT = 1;
    public int INTAKE_SOLENOID_BOTTOM_PORT = 0;
    //Hopper
    public int HOPPER_MOTOR_ID = 4;
    public int HOPPER_SENSOR_DIO_PORT = 3; // check
    //Drive
    public int DRIVE_LEFT_MASTER = 2;
    public int DRIVE_LEFT_SLAVE_ONE = 1;
    public int DRIVE_LEFT_SLAVE_TWO = 16;
    public int DRIVE_RIGHT_MASTER = 13;
    public int DRIVE_RIGHT_SLAVE_ONE = 14;
    public int DRIVE_RIGHT_SLAVE_TWO = 15;
    public int GYRO_PORT = 17;
    //WOF
    public int WOF_ENCODER_A_DIO_PORT = 0;
    public int WOF_ENCODER_B_DIO_PORT = 1;
    //turret
    public int TURRET_MOTOR_PORT = 8;
    public int TURRET_POT_ANALOG_PORT = 3;
    //Shooter
    public int SHOOTER_MASTER = 3;
    public int SHOOTER_SLAVE_ONE = 11;
    public int SHOOTER_SLAVE_TWO = 5;
    public int SHOOTER_ENCODER_A_DIO = 4;
    public int HOOD_SERVO_PWM_PORT = 9;

    //Intermediate
    public int INTERMEDIATE_MASTER = 10; //change number
    public int INTERMEDIATE_SLAVE = 12; //change number;
    //Climb
    public int CLIMB_PISTON_SOLENOID = 3;
    public int CLIMB_PTO_SOLENOID = 2;
    // Control System

    public int PDB_PORT = 0;
    public double MAX_DRIVE_PERCENT_IN_CLIMB = .3;
    public double NO_TARGET_RPM = 7000d;
    public double DEFAULT_LONG_RPM = 6250;
    public double ZOOM_RANGE_INCHES = 12 * 20;
    public double LIMELIGHT_LOW_HEIGHT = 25d;
    public double LIMELIGHT_HIGH_HEIGHT = LIMELIGHT_LOW_HEIGHT + 11d;
    public double LIMELIGHT_LENS_ANGLE = 19.5d;
    public double TARGET_HEIGHT = 80.5d;

    private AppConstants() {
    }

    public static AppConstants getInstance() {
        if (instance == null) {
            instance = new AppConstants();
        }
        return instance;
    }
}
