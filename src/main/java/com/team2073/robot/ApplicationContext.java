package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2073.robot.subsystem.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2073.robot.AppConstants;
import com.team2073.robot.subsystem.*;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.CANSparkMax;
import com.team2073.robot.statespace.ShooterVelocityCounter;
import com.team2073.robot.statespace.statespaceflywheel.subsystems.Flywheel;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;

import static com.revrobotics.CANSparkMaxLowLevel.*;
import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private static ApplicationContext instance;
    private Joystick controller;
    private Joystick driveWheel;
    private Joystick driveStick;


    //Subsystem
    private CANSparkMax intakeMotor;
    private CANSparkMax leftMaster;
    private CANSparkMax rightMaster;
    private CANSparkMax leftSlave1;
    private CANSparkMax leftSlave2;
    private CANSparkMax rightSlave1;
    private CANSparkMax rightSlave2;
    private CANSparkMax turretMotor;
    private CANSparkMax intermediateMotor;
    private Solenoid intakeSolenoidLeft;
    private Solenoid intakeSolenoidRight;
    private AnalogPotentiometer potentiometer; // WARNING: Change port
    private Servo servo; // WARNING: Change channel
    private TalonSRX intermediateBagMotor;

    // Neo550
    private CANSparkMax hopperMotor;

    // Sensors
    private DigitalInput hopperSensor;
    private Encoder wofEncoder;

    //Subsystem
    private TurretSubsystem turretSubsystem;
    private HopperSubsystem hopperSubsystem;
    private DriveSubsystem driveSubsystem;
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private IntermediateSubsystem intermediateSubsystem;
    private HoodSubsystem hoodSubsystem;
    private Limelight limelight;

    private TalonSRX shooterMotorOne;
    private TalonSRX shooterMotorTwo;
    private TalonSRX shooterMotorThree;
    private Counter AChannel;
    private Counter BChannel;

    private ShooterVelocityCounter velocityCounter;
    private Flywheel flywheel;
    private FlywheelSubsystem flywheelSubsystem;



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

    public CANSparkMax getHopperMotor() {
        if(hopperMotor == null) {
            hopperMotor = new CANSparkMax(HOPPER_MOTOR_ID, MotorType.kBrushless);
        }
        return hopperMotor;
    }

    public DigitalInput getHopperSensor() {
        if(hopperSensor == null) {
            hopperSensor = new DigitalInput(HOPPER_SENSOR_DIO_PORT);
        }
        return hopperSensor;
    }

    public HopperSubsystem getHopperSubsystem() {
        if(hopperSubsystem == null) {
            hopperSubsystem = new HopperSubsystem();
        }
        return hopperSubsystem;
    }

    public CANSparkMax getIntakeMotor() {
        if (intakeMotor == null) {
            intakeMotor = new CANSparkMax(INTAKE_MOTOR_PORT, MotorType.kBrushless);
        }
        return intakeMotor;
    }

    public Solenoid getIntakeSolenoidLeft() {
        if (intakeSolenoidLeft == null) {
            intakeSolenoidLeft = new Solenoid(INTAKE_SOLENOID_TOP_PORT);
        }
        return intakeSolenoidLeft;
    }

    public Solenoid getIntakeSolenoidRight() {
        if (intakeSolenoidRight == null) {
            intakeSolenoidRight = new Solenoid(INTAKE_SOLENOID_BOTTOM_PORT);
        }
        return intakeSolenoidRight;
    }

    public IntakeSubsystem getIntakeSubsystem() {
        if (intakeSubsystem == null) {
            intakeSubsystem = new IntakeSubsystem();
        }
        return intakeSubsystem;
    }



    public TurretSubsystem getTurretSubsystem() {
        if(turretSubsystem == null) {
            turretSubsystem = new TurretSubsystem();
        }
        return turretSubsystem;
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

    public DriveSubsystem getDriveSubsystem() {
        if (driveSubsystem == null) {
            driveSubsystem = new DriveSubsystem();
        }
        return driveSubsystem;
    }


    public WOFManipulatorSubsystem getWofManipulatorSubsystem (){
        if(wofManipulatorSubsystem == null){
            wofManipulatorSubsystem = new WOFManipulatorSubsystem();
        }
        return wofManipulatorSubsystem;
    }

    public Encoder getWofEncoder() {
        if (wofEncoder == null){
            wofEncoder = new Encoder(8, 9);
        }
        return wofEncoder;
    }

    public TalonSRX getShooterMotorOne() {
        if(shooterMotorOne == null){
            shooterMotorOne = new TalonSRX(SHOOTER_ONE);
        }
        return shooterMotorOne;
    }

    public TalonSRX getShooterMotorTwo() {
        if(shooterMotorTwo == null){
            shooterMotorTwo = new TalonSRX(SHOOTER_TWO);
        }
        return shooterMotorOne;
    }

    public TalonSRX getShooterMotorThree() {
        if(shooterMotorThree == null){
            shooterMotorThree = new TalonSRX(SHOOTER_THREE);
        }
        return shooterMotorOne;
    }

    public Counter getAChannel() {
        if(AChannel == null){
            AChannel = new Counter(SHOOTER_COUNTER_A);
        }
        return AChannel;
    }

    public Counter getBChannel() {
        if(BChannel == null){
            BChannel = new Counter(SHOOTER_COUNTER_B);
        }
        return BChannel;
    }

    public ShooterVelocityCounter getVelocityCounter() {
        if(velocityCounter == null){
            velocityCounter = new ShooterVelocityCounter();
        }
        return velocityCounter;
    }

    public Flywheel getFlywheel() {
        if(flywheel == null){
            flywheel = new Flywheel();
        }
        return flywheel;
    }

    public FlywheelSubsystem getFlywheelSubsystem() {
        if(flywheelSubsystem == null){
            flywheelSubsystem = new FlywheelSubsystem();
        }
        return flywheelSubsystem;
    }


    public AnalogPotentiometer getPotentiometer() {
        if(potentiometer == null){
            potentiometer = new AnalogPotentiometer(4);
        }
        return potentiometer;
    }

    public Limelight getLimelight() {
        if(limelight == null){
            limelight = new Limelight();
        }
        return limelight;
    }

    public Servo getServo() {
        if(servo == null){
            servo = new Servo(9);
        }
        return servo;
    }
    public CANSparkMax getTurretMotor(){
        if (turretMotor == null){
            turretMotor = new CANSparkMax(TURRET_MOTOR_PORT, MotorType.kBrushless);
        }
        return turretMotor;
    }
    public IntermediateSubsystem getIntermediateSubsystem() {
        if (intermediateSubsystem == null){
            intermediateSubsystem = new IntermediateSubsystem();
        }
        return intermediateSubsystem;
    }
    public CANSparkMax getIntermediateMotor() {
        if (intermediateMotor == null) {
            intermediateMotor = new CANSparkMax(INTERMEDIATE_MASTER, MotorType.kBrushless);
        }
        return intermediateMotor;
    }
    public TalonSRX getBagMotor() {
        if (intermediateBagMotor == null) {
            intermediateBagMotor = new TalonSRX(INTERMEDIATE_SLAVE);
        }
        return intermediateBagMotor;
    }

    public HoodSubsystem getHoodSubsystem() {
        if(hoodSubsystem == null){
            hoodSubsystem = new HoodSubsystem();
        }
        return hoodSubsystem;
    }
}
