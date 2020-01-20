package com.team2073.robot;

import com.revrobotics.CANSparkMax;
import com.team2073.robot.subsystem.IntakeSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

import static com.revrobotics.CANSparkMaxLowLevel.*;
import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private static ApplicationContext instance;
    private Joystick controller;
    private Joystick driveWheel;
    private Joystick driveStick;

    private CANSparkMax intakeMotor;

    private CANSparkMax leftMaster;
    private CANSparkMax rightMaster;
    private CANSparkMax leftSlave1;
    private CANSparkMax leftSlave2;
    private CANSparkMax rightSlave1;
    private CANSparkMax rightSlave2;
    private Solenoid intakeSolenoidLeft;
    private Solenoid intakeSolenoidRight;
    private IntakeSubsystem intakeSubsystem;


    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Joystick getController() {
        if (controller == null) {
            controller = new Joystick(CONTROLLER_PORT);
        }
        return controller;
    }

    public Joystick getDriveWheel() {
        if (driveWheel == null) {
            driveWheel = new Joystick(WHEEL_PORT);
        }
        return driveWheel;
    }

    public Joystick getDriveStick() {
        if (driveStick == null) {
            driveStick = new Joystick(JOYSTICK_PORT);
        }
        return driveStick;
    }

    public CANSparkMax getIntakeMotor() {
        if (intakeMotor == null) {
            intakeMotor = new CANSparkMax(INTAKE_MOTOR_PORT, MotorType.kBrushless);
        }
        return intakeMotor;
    }

    public Solenoid getIntakeSolenoidLeft() {
        if (intakeSolenoidLeft == null) {
            intakeSolenoidLeft = new Solenoid(INTAKE_SOLENOID_LEFT_PORT);
        }
        return intakeSolenoidLeft;
    }

    public Solenoid getIntakeSolenoidRight() {
        if (intakeSolenoidRight == null) {
            intakeSolenoidRight = new Solenoid(INTAKE_SOLENOID_RIGHT_PORT);
        }
        return intakeSolenoidRight;
    }

    public IntakeSubsystem getIntakeSubsystem() {
        if (intakeSubsystem == null) {
            intakeSubsystem = new IntakeSubsystem();
        }
        return intakeSubsystem;
    }

    public CANSparkMax getLeftMaster() {
        if(leftMaster == null){
            leftMaster = new CANSparkMax(DRIVE_LEFT_MASTER, MotorType.kBrushless);
        }
        return leftMaster;
    }

    public CANSparkMax getRightMaster() {
        if(rightMaster == null){
            rightMaster = new CANSparkMax(DRIVE_RIGHT_MASTER, MotorType.kBrushless);
        }
        return rightMaster;
    }

    public CANSparkMax getLeftSlave1() {
        if(leftSlave1 == null){
            leftSlave1 = new CANSparkMax(DRIVE_LEFT_SLAVE_ONE, MotorType.kBrushless);
        }
        return leftSlave1;
    }

    public CANSparkMax getLeftSlave2() {
        if(leftSlave2 == null){
            leftSlave2 = new CANSparkMax(DRIVE_LEFT_SLAVE_TWO, MotorType.kBrushless);
        }
        return leftSlave2;
    }

    public CANSparkMax getRightSlave1() {
        if(rightSlave1 == null){
            rightSlave1 = new CANSparkMax(DRIVE_RIGHT_SLAVE_ONE, MotorType.kBrushless);
        }
        return rightSlave1;
    }

    public CANSparkMax getRightSlave2() {
        if(rightSlave2 == null){
            rightSlave2 = new CANSparkMax(DRIVE_RIGHT_SLAVE_TWO, MotorType.kBrushless);
        }
        return rightSlave2;
    }
}