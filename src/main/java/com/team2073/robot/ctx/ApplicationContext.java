package com.team2073.robot.ctx;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.team2073.robot.subsystem.IntakeSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private static ApplicationContext instance;
    private Joystick controller;
    private Joystick driveWheel;
    private Joystick driveStick;

    private CANSparkMax intakeMotor;
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
            intakeMotor = new CANSparkMax(INTAKE_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
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
}
