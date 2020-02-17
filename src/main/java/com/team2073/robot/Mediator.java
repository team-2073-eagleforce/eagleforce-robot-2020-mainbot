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

        switch (desiredState) {
            case STARTING_CONFIG:
                if (lastState != desiredState) {
                    hopper.setState(HopperState.IDLE);
                    intake.setPosition(IntakePositionState.STARTING_CONFIG);
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
                intake.setPosition(IntakePositionState.STOW);
                hopper.setState(HopperState.PREP_SHOT);
                hood.determineHoodAngle();
                elevator.set(calcElevatorShotHeight());
                turret.setState(TurretSubsystem.TurretState.SEEK);

                flywheel.setRPM(SmartDashboard.getNumber("Flywheel RPM", 5000));
//                flywheel.setRPM(turret.calcRPMGoal(elevator.getCurrentState()));

//                Turret calcs rpm for shooter,

                break;
            case SHOOTING:
                hopper.setState(HopperState.SHOOT);
                if (hopper.isShotReady()) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
                }
                break;
            case STOW:
                if (lastState != desiredState) {
                    intake.setPosition(IntakePositionState.STOW);
                    hopper.setState(HopperState.IDLE);
                    hood.setHood(HoodSubsystem.HoodState.RETRACTED);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
                    turret.setState(TurretSubsystem.TurretState.WAIT);
                    flywheel.setRPM(null);
                }

                break;
            case WHEEL_OF_FORTUNE:
                if(lastState != desiredState) {
                    intermediate.set(IntermediateSubsystem.IntermediateState.WAIT_FOR_WOF);
                    elevator.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
                }

                break;
            case PREP_CLIMB:
                if (lastState != desiredState) {
                    drive.capTeleopOutput(MAX_DRIVE_PERCENT_IN_CLIMB);
                    intake.setPosition(IntakePositionState.STARTING_CONFIG);
                    hopper.setState(HopperState.STOP);
//                    climb.extendOrSomething
                }
                break;
            case CLIMB:
//                elevator.goDown
                break;
            case DISABLED:
                break;
        }
        lastState = desiredState;
    }

    private double calcElevatorShotHeight() {
        if (elevator.getCurrentState() == ElevatorSubsytem.ElevatorState.TOP) {
            if (targetDistance() > 210) {
                return ElevatorSubsytem.ElevatorState.BOTTOM.getValue();
            } else {
                return ElevatorSubsytem.ElevatorState.TOP.getValue();
            }
        } else {
            if (targetDistance() < 190) {
                return ElevatorSubsytem.ElevatorState.TOP.getValue();
            } else {
                return ElevatorSubsytem.ElevatorState.BOTTOM.getValue();
            }
        }

    }

    public double targetDistance() {
        if (elevator.getCurrentState() != ElevatorSubsytem.ElevatorState.BOTTOM) {
            return limelight.getHighDistance();
        } else {
            return limelight.getLowDistance();
        }
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
