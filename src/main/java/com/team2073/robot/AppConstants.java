package com.team2073.robot;

public abstract class AppConstants {
    public abstract static class Ports {

        //DS
        public static final int WHEEL_PORT = 0;
        public static final int JOYSTICK_PORT = 1;
        public static final int CONTROLLER_PORT = 2;

        //Elevator
        public static final int ELEVATOR_BOTTOM_DIO_PORT = 2;
        public static final int ELEVATOR_MOTOR_PORT = 54;

        //Intake
        public static final int INTAKE_MOTOR_PORT = 7;
        public static final int INTAKE_SOLENOID_TOP_PORT = 0; // Practice bot needs plumbing change
        public static final int INTAKE_SOLENOID_BOTTOM_PORT = 1;

        //Hopper
        public static final int HOPPER_MOTOR_ID = 4;
        public static final int HOPPER_SENSOR_DIO_PORT = 3; // check

        //Drive
        public static final int DRIVE_LEFT_MASTER = 2;
        public static final int DRIVE_LEFT_SLAVE_ONE = 1;
        public static final int DRIVE_LEFT_SLAVE_TWO = 16;
        public static final int DRIVE_RIGHT_MASTER = 13;
        public static final int DRIVE_RIGHT_SLAVE_ONE = 14;
        public static final int DRIVE_RIGHT_SLAVE_TWO = 15;

        //WOF
        public static final int WOF_ENCODER_A_DIO_PORT = 0;
        public static final int WOF_ENCODER_B_DIO_PORT = 1;

        //Shooter - Current settings are for the test board

        //turret
        public static final int TURRET_MOTOR_PORT = 8;
        public static final int TURRET_POT_ANALOG_PORT = 3;

        //Shooter
        public static final int SHOOTER_MASTER = 3;
        public static final int SHOOTER_SLAVE_ONE = 11;
        public static final int SHOOTER_SLAVE_TWO = 5;
        public static final int SHOOTER_ENCODER_A_DIO = 4;
        public static final int HOOD_SERVO_PWM_PORT = 9;

        //Intermediate
        public static final int INTERMEDIATE_MASTER = 10; //change number
        public static final int INTERMEDIATE_SLAVE = 12; //change number;

        // Control System
        public static final int PDB_PORT = 0;
    }

    public abstract static class Mediator {
        public static final double MAX_DRIVE_PERCENT_IN_CLIMB = .3;
    }

    public abstract static class Shooter {
        public static final double NO_TARGET_RPM = 3250;
        public static final double DEFAULT_LONG_RPM = 6250;
        public static final double ZOOM_RANGE_INCHES = 12*20;
        public static final double test = 3;


        public static final double LIMELIGHT_HIGH_HEIGHT = 39d;
        public static final double LIMELIGHT_LOW_HEIGHT = 24.875d;
        public static final double LIMELIGHT_LENS_ANGLE = 15d;
        public static final double TARGET_HEIGHT = 81.25 - 3;
    }
}
