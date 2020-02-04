package com.team2073.robot;

public abstract class AppConstants {
    public abstract class Ports {

        //DS
        public static final int WHEEL_PORT = 2;
        public static final int JOYSTICK_PORT = 1;
        public static final int CONTROLLER_PORT = 0;

        //Intake
        public static final int INTAKE_MOTOR_PORT = 7;
        public static final int INTAKE_SOLENOID_LEFT_PORT = 0;
        public static final int INTAKE_SOLENOID_RIGHT_PORT = 1;

        //Hopper
        public static final int HOPPER_MOTOR_ID = 4;
        public static final int HOPPER_SENSOR_DIO_PORT = 0;

        //Drive
        public static final int DRIVE_LEFT_MASTER = 2;
        public static final int DRIVE_LEFT_SLAVE_ONE = 1;
        public static final int DRIVE_LEFT_SLAVE_TWO = 16;
        public static final int DRIVE_RIGHT_MASTER = 13;
        public static final int DRIVE_RIGHT_SLAVE_ONE = 14;
        public static final int DRIVE_RIGHT_SLAVE_TWO = 15;

        //WOF
        public static final int WOF_ENCODER_PORT = 0;

        //turret
        public static final int TURRET_MOTOR_PORT = 1;

        //Shooter
        public static final int SHOOTER_MASTER = 3;
        public static final int SHOOTER_SLAVE_ONE = 12;
        public static final int SHOOTER_SLAVE_TWO = 5;
        //Intermediate
        public static final int INTERMEDIATE_MASTER = 7; //change number
    }
}
