package com.team2073.robot;

import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.constants.AppConstants;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.HopperSubsystem.HopperState;
import com.team2073.robot.subsystem.IntakeSubsystem.IntakePositionState;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2073.robot.Mediator.RobotState.*;

public class Mediator implements AsyncPeriodicRunnable {
    private static Mediator instance = null;
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotState desiredState = STARTING_CONFIG;
    private RobotState lastState = DISABLED;
    private DriveSubsystem drive;
    private IntakeSubsystem intake;
    private HopperSubsystem hopper;
    private IntermediateSubsystem intermediate;
    private TurretSubsystem turret;
    private WOFManipulatorSubsystem wof;
    private HoodSubsystem hood;
    private ElevatorSubsytem elevator;
    private FlywheelSubsystem flywheel;
    private Limelight limelight;
    private ClimbSubsystem climb;
    private boolean closeShot;
    private double angleToTargetOffset = 0;
    private AppConstants constants = AppConstants.getInstance();

    private Mediator() {
        autoRegisterWithPeriodicRunner(10);
        drive = appCtx.getDriveSubsystem();
        intake = appCtx.getIntakeSubsystem();
        hopper = appCtx.getHopperSubsystem();
        intermediate = appCtx.getIntermediateSubsystem();
        turret = appCtx.getTurretSubsystem();
        wof = appCtx.getWofManipulatorSubsystem();
        hood = appCtx.getHoodSubsystem();
        elevator = appCtx.getElevatorSubsystem();
        flywheel = appCtx.getFlywheelSubsystem();
        limelight = appCtx.getLimelight();
        climb = appCtx.getClimbSubsystem();

    }

    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    public void setCloseShot(boolean closeShot) {
        this.closeShot = closeShot;
    }

    @Override
    public void onPeriodicAsync() {
        SmartDashboard.putNumber("LimelightDistance", targetDistance());
        if ((desiredState != CLIMB && desiredState != PREP_CLIMB) && (lastState == CLIMB || lastState == PREP_CLIMB)) {
            drive.capTeleopOutput(1);
        }
        switch (desiredState) {
            case STARTING_CONFIG:
                if (lastState != desiredState) {
                    hopper.setState(HopperState.IDLE);
                    intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
//                    intake.setPosition(IntakePositionState.STARTING_CONFIG);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
                }
                break;
            case INTAKE_BALL:
                if (lastState != desiredState) {
                    hopper.setState(HopperState.IDLE);
                    intake.setPosition(IntakePositionState.INTAKE_OUT);
                }
                break;
            case PREP_SHOT:
//                hopper.setState(HopperState.PREP_SHOT);
                if (!closeShot) {
                    hood.setHood(HoodSubsystem.HoodState.EXTENDED);
                    elevator.setElevatorState(calcElevatorShotHeight());
                    turret.setState(TurretSubsystem.TurretState.SEEK);
                    flywheel.setRPM(turret.calcRPMGoal(elevator.getCurrentState()));
                } else {
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.TOP);
                    turret.setState(TurretSubsystem.TurretState.FACE_FRONT);
                    flywheel.setRPM(constants.NO_TARGET_RPM);
                    hood.setHood(HoodSubsystem.HoodState.CLOSE_SHOT);
                }
//                flywheel.setRPM(SmartDashboard.getNumber("Flywheel RPM", 5000));

//                Turret calcs rpm for shooter,

                break;
            case SHOOTING:
                drive.capTeleopOutput(0);
//                if (!flywheel.atReference()) {
//                    if (!closeShot) {
//                        flywheel.setRPM(turret.calcRPMGoal(elevator.getCurrentState()));
//                    } else {
//                        flywheel.setRPM(AppConstants.Shooter.NO_TARGET_RPM);
//                    }
//                    break;
//                }
                if(elevator.getSetpoint() == null){
                    hopper.setState(HopperState.ELEVATOR_DOWN_SHOOT);
                }else if (elevator.getSetpoint() > ElevatorSubsytem.ElevatorState.TOP.getHeight() / 2) {
                    hopper.setState(HopperState.ELEVATOR_UP_SHOOT);
                } else {
                    hopper.setState(HopperState.ELEVATOR_DOWN_SHOOT);
                }
                if (hopper.isShotReady()) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
                }
                break;
            case STOW:
                if (lastState != desiredState) {
                    System.out.println("Mediator STOW");
                    drive.capTeleopOutput(1);
                    hood.determineHoodAngle(false);
//                    intake.setPosition(IntakePositionState.STOW);
                    hopper.setState(HopperState.IDLE);
                    hood.setHood(HoodSubsystem.HoodState.RETRACTED);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
                    turret.setState(TurretSubsystem.TurretState.GYRO);
                    intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
                    flywheel.setRPM(null);
                    hopper.setShotReady(false);
                    turret.resetReachedClosest();
                }

                break;
            case WHEEL_OF_FORTUNE:
                if (lastState != desiredState) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.WAIT_FOR_WOF);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
                    turret.setState(TurretSubsystem.TurretState.WOF);
                }

                break;
            case PREP_CLIMB:
                if (lastState != desiredState) {
                    drive.capTeleopOutput(constants.MAX_DRIVE_PERCENT_IN_CLIMB);
                    intake.setPosition(IntakePositionState.INTAKE_OUT);
                    hopper.setState(HopperState.STOP);
                    climb.setState(ClimbSubsystem.ClimbState.HOOKS);
                }
                break;
            case CLIMB:
//                elevator.goDown
                climb.setState(ClimbSubsystem.ClimbState.PTO);

                break;
            case DISABLED:
                break;
        }
        lastState = desiredState;
    }

    private ElevatorSubsytem.ElevatorState calcElevatorShotHeight() {
        if (elevator.getCurrentState() == ElevatorSubsytem.ElevatorState.TOP) {
            if (targetDistance() > 210 /*&& targetDistance() < 280*/) {
                return ElevatorSubsytem.ElevatorState.BOTTOM;
            } else {
                return ElevatorSubsytem.ElevatorState.TOP;
            }
        } else {
            if (targetDistance() < 190 /*|| targetDistance() > 300d*/) {
                return ElevatorSubsytem.ElevatorState.TOP;
            } else {
                return ElevatorSubsytem.ElevatorState.BOTTOM;
            }
        }

    }

    public double targetDistance() {
        if (limelight.getTv() == 0) {
            return 6 * 12;
        }
        return limelight.getDistanceWithElevator(elevator.position());
    }

    public double robotAngleToTarget() {
        double angle = drive.getPose().getRotation().getDegrees();
        if( angle > 180){
            angle -= 360;
        }
        return angle + 180 + angleToTargetOffset;
    }

    public void setRobotAngleToTargetOffset(double offset) {
        this.angleToTargetOffset = offset;
    }

    public void setDesiredState(RobotState state) {
        this.desiredState = state;
    }

    public RobotState getCurrentState(){
        return desiredState;
    }


    public enum RobotState {
        STARTING_CONFIG,
        INTAKE_BALL,
        PREP_SHOT,
        SHOOTING,
        WHEEL_OF_FORTUNE,
        PREP_CLIMB,
        CLIMB,
        STOW,
        DISABLED
    }


}
