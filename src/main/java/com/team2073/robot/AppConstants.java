package com.team2073.robot;

public abstract class AppConstants {
    public abstract class Ports {

        //DS
        public static final int WHEEL_PORT = 2;
        public static final int JOYSTICK_PORT = 1;
        public static final int CONTROLLER_PORT = 0;

        //Intake
        public static final int INTAKE_MOTOR_PORT = 0;
        public static final int INTAKE_SOLENOID_LEFT_PORT = 0;
        public static final int INTAKE_SOLENOID_RIGHT_PORT = 1;

        //Hopper
        public static final int HOPPER_MOTOR_ID = 0;
        public static final int HOPPER_SENSOR_DIO_PORT = 0;

        //Drive
        public static final int DRIVE_LEFT_MASTER = 40;
        public static final int DRIVE_LEFT_SLAVE_ONE = 1;
        public static final int DRIVE_LEFT_SLAVE_TWO = 2;
        public static final int DRIVE_RIGHT_MASTER = 15;
        public static final int DRIVE_RIGHT_SLAVE_ONE = 14;
        public static final int DRIVE_RIGHT_SLAVE_TWO = 13;

        //WOF
        public static final int WOF_ENCODER_PORT = 0;

    }
}
