package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.team2073.robot.constants.AppConstants;
import com.team2073.robot.statespace.ShooterVelocityCounter;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.*;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
    private Solenoid intakeSolenoidTop;
    private Solenoid intakeSolenoidBottom;
    private Solenoid climbPistonSolenoid;
    private Solenoid climbDriveSolenoid;
    private AnalogPotentiometer potentiometer; // WARNING: Change port
    private Servo servo; // WARNING: Change channel
    private TalonSRX intermediateBagMotor;

    // Neo550
    private CANSparkMax hopperMotor;

    // Sensors
    private DigitalInput hopperSensor;
    private Encoder wofEncoder;
    private DigitalInput elevatorBottomSensor;
    private PigeonIMU gyro;

    //Subsystem
    private TurretSubsystem turretSubsystem;
    private HopperSubsystem hopperSubsystem;
    private DriveSubsystem driveSubsystem;
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private IntermediateSubsystem intermediateSubsystem;
    private HoodSubsystem hoodSubsystem;
    private ElevatorSubsytem elevatorSubsytem;
    private Limelight limelight;
    private ClimbSubsystem climbSubsystem;

    private TalonSRX shooterMotorOne;
    private VictorSPX shooterMotorTwo;
    private VictorSPX shooterMotorThree;
    private Counter AChannel;
    private TalonFX elevatorMotor;

    private ShooterVelocityCounter velocityCounter;
    private FlywheelSubsystem flywheelSubsystem;

    private AppConstants constants = AppConstants.getInstance();

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Joystick getController() {
        if (controller == null) {
            controller = new Joystick(constants.CONTROLLER_PORT);
        }
        return controller;
    }

    public Joystick getDriveWheel() {
        if (driveWheel == null) {
            driveWheel = new Joystick(constants.WHEEL_PORT);
        }
        return driveWheel;
    }

    public Joystick getDriveStick() {
        if (driveStick == null) {
            driveStick = new Joystick(constants.JOYSTICK_PORT);
        }
        return driveStick;
    }

    public CANSparkMax getHopperMotor() {
        if (hopperMotor == null) {
            hopperMotor = new CANSparkMax(constants.HOPPER_MOTOR_ID, MotorType.kBrushless);
        }
        return hopperMotor;
    }

    public DigitalInput getHopperSensor() {
        if (hopperSensor == null) {
            hopperSensor = new DigitalInput(constants.HOPPER_SENSOR_DIO_PORT);
        }
        return hopperSensor;
    }

    public HopperSubsystem getHopperSubsystem() {
        if (hopperSubsystem == null) {
            hopperSubsystem = new HopperSubsystem();
        }
        return hopperSubsystem;
    }

    public CANSparkMax getIntakeMotor() {
        if (intakeMotor == null) {
            intakeMotor = new CANSparkMax(constants.INTAKE_MOTOR_PORT, MotorType.kBrushless);
        }
        return intakeMotor;
    }

    public Solenoid getIntakeSolenoidTop() {
        if (intakeSolenoidTop == null) {
            intakeSolenoidTop = new Solenoid(constants.INTAKE_SOLENOID_TOP_PORT);
        }
        return intakeSolenoidTop;
    }

    public Solenoid getIntakeSolenoidBottom() {
        if (intakeSolenoidBottom == null) {
            intakeSolenoidBottom = new Solenoid(constants.INTAKE_SOLENOID_BOTTOM_PORT);
        }
        return intakeSolenoidBottom;
    }

    public Solenoid getClimbPistonSolenoid() {
        if (climbPistonSolenoid == null) {
            climbPistonSolenoid = new Solenoid(constants.CLIMB_PISTON_SOLENOID);
        }
        return climbPistonSolenoid;
    }

    public Solenoid getClimbDriveSolenoid() {
        if (climbDriveSolenoid == null) {
            climbDriveSolenoid = new Solenoid(constants.CLIMB_PTO_SOLENOID);
        }
        return climbDriveSolenoid;
    }

    public IntakeSubsystem getIntakeSubsystem() {
        if (intakeSubsystem == null) {
            intakeSubsystem = new IntakeSubsystem();
        }
        return intakeSubsystem;
    }


    public TurretSubsystem getTurretSubsystem() {
        if (turretSubsystem == null) {
            turretSubsystem = new TurretSubsystem();
        }
        return turretSubsystem;
    }

    public CANSparkMax getLeftMaster() {
        if (leftMaster == null) {
            leftMaster = new CANSparkMax(constants.DRIVE_LEFT_MASTER, MotorType.kBrushless);
        }
        return leftMaster;
    }

    public CANSparkMax getRightMaster() {
        if (rightMaster == null) {
            rightMaster = new CANSparkMax(constants.DRIVE_RIGHT_MASTER, MotorType.kBrushless);
        }
        return rightMaster;
    }

    public CANSparkMax getLeftSlave1() {
        if (leftSlave1 == null) {
            leftSlave1 = new CANSparkMax(constants.DRIVE_LEFT_SLAVE_ONE, MotorType.kBrushless);
        }
        return leftSlave1;
    }

    public CANSparkMax getLeftSlave2() {
        if (leftSlave2 == null) {
            leftSlave2 = new CANSparkMax(constants.DRIVE_LEFT_SLAVE_TWO, MotorType.kBrushless);
        }
        return leftSlave2;
    }

    public CANSparkMax getRightSlave1() {
        if (rightSlave1 == null) {
            rightSlave1 = new CANSparkMax(constants.DRIVE_RIGHT_SLAVE_ONE, MotorType.kBrushless);
        }
        return rightSlave1;
    }

    public CANSparkMax getRightSlave2() {
        if (rightSlave2 == null) {
            rightSlave2 = new CANSparkMax(constants.DRIVE_RIGHT_SLAVE_TWO, MotorType.kBrushless);
        }
        return rightSlave2;
    }

    public DriveSubsystem getDriveSubsystem() {
        if (driveSubsystem == null) {
            driveSubsystem = new DriveSubsystem();
        }
        return driveSubsystem;
    }


    public WOFManipulatorSubsystem getWofManipulatorSubsystem() {
        if (wofManipulatorSubsystem == null) {
            wofManipulatorSubsystem = new WOFManipulatorSubsystem();
        }
        return wofManipulatorSubsystem;
    }

    public Encoder getWofEncoder() {
        if (wofEncoder == null) {
            wofEncoder = new Encoder(constants.WOF_ENCODER_A_DIO_PORT, constants.WOF_ENCODER_B_DIO_PORT);
        }
        return wofEncoder;
    }

    public TalonSRX getShooterMotorOne() {
        if (shooterMotorOne == null) {
            shooterMotorOne = new TalonSRX(constants.SHOOTER_MASTER);
        }
        return shooterMotorOne;
    }

    public VictorSPX getShooterMotorTwo() {
        if (shooterMotorTwo == null) {
            shooterMotorTwo = new VictorSPX(constants.SHOOTER_SLAVE_ONE);
        }
        return shooterMotorTwo;
    }

    public VictorSPX getShooterMotorThree() {
        if (shooterMotorThree == null) {
            shooterMotorThree = new VictorSPX(constants.SHOOTER_SLAVE_TWO);
        }
        return shooterMotorThree;
    }

    public Counter getAChannel() {
        if (AChannel == null) {
            AChannel = new Counter(constants.SHOOTER_ENCODER_A_DIO);
        }
        return AChannel;
    }

    public ShooterVelocityCounter getVelocityCounter() {
        if (velocityCounter == null) {
            velocityCounter = new ShooterVelocityCounter();
        }
        return velocityCounter;
    }

    public FlywheelSubsystem getFlywheelSubsystem() {
        if (flywheelSubsystem == null) {
            flywheelSubsystem = new FlywheelSubsystem();
        }
        return flywheelSubsystem;
    }


    public AnalogPotentiometer getPotentiometer() {
        if (potentiometer == null) {
            potentiometer = new AnalogPotentiometer(constants.TURRET_POT_ANALOG_PORT);
        }
        return potentiometer;
    }

    public Limelight getLimelight() {
        if (limelight == null) {
            limelight = new Limelight();
        }
        return limelight;
    }

    public Servo getServo() {
        if (servo == null) {
            servo = new Servo(constants.HOOD_SERVO_PWM_PORT);
        }
        return servo;
    }

    public CANSparkMax getTurretMotor() {
        if (turretMotor == null) {
            turretMotor = new CANSparkMax(constants.TURRET_MOTOR_PORT, MotorType.kBrushless);
        }
        return turretMotor;
    }

    public IntermediateSubsystem getIntermediateSubsystem() {
        if (intermediateSubsystem == null) {
            intermediateSubsystem = new IntermediateSubsystem();
        }
        return intermediateSubsystem;
    }

    public CANSparkMax getIntermediateMotor() {
        if (intermediateMotor == null) {
            intermediateMotor = new CANSparkMax(constants.INTERMEDIATE_NEO, MotorType.kBrushless);
        }
        return intermediateMotor;
    }

    public TalonSRX getBagMotor() {
        if (intermediateBagMotor == null) {
            intermediateBagMotor = new TalonSRX(constants.INTERMEDIATE_BAG);
        }
        return intermediateBagMotor;
    }

    public HoodSubsystem getHoodSubsystem() {
        if (hoodSubsystem == null) {
            hoodSubsystem = new HoodSubsystem();
        }
        return hoodSubsystem;
    }

    public DigitalInput getElevatorBottomSensor() {
        if (elevatorBottomSensor == null) {
            elevatorBottomSensor = new DigitalInput(constants.ELEVATOR_BOTTOM_DIO_PORT);
        }
        return elevatorBottomSensor;
    }

    public TalonFX getElevatorMotor() {
        if (elevatorMotor == null) {
            elevatorMotor = new TalonFX(constants.ELEVATOR_MOTOR_PORT);
        }
        return elevatorMotor;
    }

    public ElevatorSubsytem getElevatorSubsystem() {
        if (elevatorSubsytem == null) {
            elevatorSubsytem = new ElevatorSubsytem();
        }
        return elevatorSubsytem;
    }

    public ClimbSubsystem getClimbSubsystem() {
        if (climbSubsystem == null) {
            climbSubsystem = new ClimbSubsystem();
        }
        return climbSubsystem;
    }

    public PigeonIMU getGyro() {
        if (gyro == null) {
            gyro = new PigeonIMU(constants.GYRO_PORT);
        }
        return gyro;
    }
}
