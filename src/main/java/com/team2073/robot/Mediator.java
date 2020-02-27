package com.team2073.robot;

import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.HopperSubsystem.HopperState;
import com.team2073.robot.subsystem.IntakeSubsystem.IntakePositionState;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2073.robot.AppConstants.Mediator.MAX_DRIVE_PERCENT_IN_CLIMB;
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

    public void setCloseShot(boolean closeShot){
        this.closeShot = closeShot;
    }

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

    @Override
    public void onPeriodicAsync() {
        if ((desiredState != CLIMB && desiredState != PREP_CLIMB) && (lastState == CLIMB || lastState == PREP_CLIMB)) {
            drive.capTeleopOutput(1);
        }
        SmartDashboard.putNumber("LimelightDistance", targetDistance());
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
                if(!closeShot) {
                    hood.setHood(HoodSubsystem.HoodState.EXTENDED);
                    elevator.setElevatorState(calcElevatorShotHeight());
                    turret.setState(TurretSubsystem.TurretState.SEEK);
                    flywheel.setRPM(turret.calcRPMGoal(elevator.getCurrentState()));
                }else{
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.TOP);
                    turret.setAutoSetpoint(35d);
                    turret.setState(TurretSubsystem.TurretState.AUTO);
                    flywheel.setRPM(AppConstants.Shooter.NO_TARGET_RPM);
                    hood.setHood(HoodSubsystem.HoodState.CLOSE_SHOT);
                }
//                flywheel.setRPM(SmartDashboard.getNumber("Flywheel RPM", 5000));

//                Turret calcs rpm for shooter,

                break;
            case SHOOTING:
                drive.capTeleopOutput(0);
                if (!flywheel.atReference()) {
                    if(!closeShot) {
                        flywheel.setRPM(turret.calcRPMGoal(elevator.getCurrentState()));
                    }else{
                        flywheel.setRPM(AppConstants.Shooter.NO_TARGET_RPM);
                    }
                    break;
                }
                hopper.setState(HopperState.SHOOT);
                if (hopper.isShotReady()) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
                }
                break;
            case STOW:
                if (lastState != desiredState) {
                    drive.capTeleopOutput(1);
                    hood.determineHoodAngle(false);
//                    intake.setPosition(IntakePositionState.STOW);
                    hopper.setState(HopperState.IDLE);
                    hood.setHood(HoodSubsystem.HoodState.RETRACTED);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
                    turret.setState(TurretSubsystem.TurretState.WAIT);
                    intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
                    flywheel.setRPM(null);
                    hopper.setShotReady(false);
                }

                break;
            case WHEEL_OF_FORTUNE:
                if (lastState != desiredState) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.WAIT_FOR_WOF);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
                }

                break;
            case PREP_CLIMB:
                if (lastState != desiredState) {
                    drive.capTeleopOutput(MAX_DRIVE_PERCENT_IN_CLIMB);
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

    public void setDesiredState(RobotState state) {
        this.desiredState = state;
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
