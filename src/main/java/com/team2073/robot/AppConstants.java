package com.team2073.robot;

public abstract class AppConstants {
    public abstract class Ports {

        //DS
        public static final int WHEEL_PORT = 0;
        public static final int JOYSTICK_PORT = 1;
        public static final int CONTROLLER_PORT = 2;

        //Intake
        public static final int INTAKE_MOTOR_PORT = 7;
        public static final int INTAKE_SOLENOID_TOP_PORT = 0; // Practice bot needs plumbing change
        public static final int INTAKE_SOLENOID_BOTTOM_PORT = 1;

        //Hopper
        public static final int HOPPER_MOTOR_ID = 4;
        public static final int HOPPER_SENSOR_DIO_PORT = 7; // check

        //Drive
        public static final int DRIVE_LEFT_MASTER = 2;
        public static final int DRIVE_LEFT_SLAVE_ONE = 1;
        public static final int DRIVE_LEFT_SLAVE_TWO = 16;
        public static final int DRIVE_RIGHT_MASTER = 13;
        public static final int DRIVE_RIGHT_SLAVE_ONE = 14;
        public static final int DRIVE_RIGHT_SLAVE_TWO = 15;

        //WOF
        public static final int WOF_ENCODER_A_DIO_PORT = 0; // has not been wired yet
        public static final int WOF_ENCODER_B_DIO_PORT = 1; // ^

        //Shooter - Current settings are for the test board
        public static final int SHOOTER_ONE = 1;
        public static final int SHOOTER_TWO = 4;
        public static final int SHOOTER_THREE = 9;
        public static final int SHOOTER_COUNTER_A = 8;
        public static final int SHOOTER_COUNTER_B = 9;

        //turret
        public static final int TURRET_MOTOR_PORT = 1;
        public static final int TURRET_POT_ANALOG_PORT = 1; // has not been wired yet
        //Shooter
        public static final int SHOOTER_MASTER = 3;
        public static final int SHOOTER_SLAVE_ONE = 12;
        public static final int SHOOTER_SLAVE_TWO = 5;
        public static final int SHOOTER_ENCODER_A_DIO = 9; // Has not been wired yet
        public static final int SHOOTER_ENCODER_B_DIO = 8; // ^

        //Intermediate
        public static final int INTERMEDIATE_MASTER = 10; //change number
        public static final int INTERMEDIATE_SLAVE = 8; //change number;

        // Control System
        public static final int PDB_PORT = 0;
    }

    public abstract class Mediator {
        public static final double MAX_DRIVE_PERCENT_IN_CLIMB = .3;
    }
}
