package com.team2073.robot;

import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.HopperSubsystem.HopperState;
import com.team2073.robot.subsystem.IntakeSubsystem.IntakeState;
import com.team2073.robot.subsystem.drive.DriveSubsystem;

import static com.team2073.robot.AppConstants.Mediator.MAX_DRIVE_PERCENT_IN_CLIMB;
import static com.team2073.robot.Mediator.RobotState.*;

public class Mediator implements AsyncPeriodicRunnable {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotState desiredState = STARTING_CONFIG;
    private RobotState lastState = DISABLED;
    private static Mediator instance = null;

    private DriveSubsystem drive;
    private IntakeSubsystem intake;
    private HopperSubsystem hopper;
    private IntermediateSubsystem intermediate;
    private TurretSubsystem turret;
    private WOFManipulatorSubsystem wof;
    private HoodSubsystem hood;


    public static Mediator getInstance(){
        if(instance == null){
            instance = new Mediator();
        }
        return instance;
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
    }

    @Override
    public void onPeriodicAsync() {
        if ((desiredState != CLIMB  && desiredState != PREP_CLIMB) && (lastState == CLIMB || lastState == PREP_CLIMB)) {
            drive.capTeleopOutput(1);
        }

        switch (desiredState) {
            case STARTING_CONFIG:
                if(lastState != desiredState) {
                    hopper.setState(HopperState.IDLE);
                    intake.set(IntakeState.STOWED);
//                    elevator.goDown;
                }
                break;
            case INTAKE_BALL:
                if(lastState != desiredState) {
                    hopper.setState(HopperState.IDLE);
                    intake.set(IntakeState.INTAKE_OUT);
                }
                break;
            case PREP_SHOT:
                intake.set(IntakeState.STOWED);
                hopper.setState(HopperState.PREP_SHOT);
                hood.determineHoodAngle();
//                turret.find target;
//                elevator.move?

                break;
            case SHOOTING:
                hopper.setState(HopperState.SHOOT);
                intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
                break;
            case WHEEL_OF_FORTUNE:
                intermediate.set(IntermediateSubsystem.IntermediateState.WAIT_FOR_WOF);
//                wof.DoTheThing
                wof.rotationControl();
//                elevator.toWOFHeight

                break;
            case PREP_CLIMB:
                if (lastState != desiredState) {
                    drive.capTeleopOutput(MAX_DRIVE_PERCENT_IN_CLIMB);
                    intake.set(IntakeState.STOWED);
                    hopper.setState(HopperState.STOP);

//                    elevator.goUp
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

    public void setDesiredState(RobotState state){
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
        DISABLED
    }


}
