package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private boolean pistonsExtended = false;

    private CANSparkMax intakeMotor = appCtx.getIntakeMotor();
    private Solenoid pistonTop = appCtx.getIntakeSolenoidTop();
    private Solenoid pistonBottom = appCtx.getIntakeSolenoidBottom();
    private IntakePositionState positionState = IntakePositionState.STARTING_CONFIG;
    private IntakeRollerState rollerState = IntakeRollerState.STOP;

    public IntakeSubsystem() {
        autoRegisterWithPeriodicRunner(20);
        intakeMotor.setOpenLoopRampRate(.125);
        intakeMotor.setInverted(true);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(rollerState.getPercent());
//        hln("Intake: " + intakeMotor.getAppliedOutput());
        switch (positionState) {
            case STARTING_CONFIG:
                togglePistons(false, false);
                break;
            case STOW:
                togglePistons(false, false);
                break;
            case INTAKE_OUT:
                togglePistons(true, true);
                break;
            case AUTO_INTAKE:
                togglePistons(false, true);
                break;
            case FEEDER_STATION:
                togglePistons(false, true);
        }


    }

    private void setPower(Double percent) {
        intakeMotor.set(percent);
    }

    public void setRollerState(IntakeRollerState state) {
        this.rollerState = state;
    }

    public void setPosition(IntakePositionState goalState) {
        positionState = goalState;
    }

    private void togglePistons(boolean topOut, boolean bottomOut) {
        pistonTop.set(topOut);
        pistonBottom.set(bottomOut);
    }

    public enum IntakePositionState {
        STARTING_CONFIG,
        INTAKE_OUT,
        STOW,
        AUTO_INTAKE,
        FEEDER_STATION,

    }

    public enum IntakeRollerState {
        INTAKE(.95d),
        OUTTAKE(-.95d),
        STOP(0d),
        DISABLED(0d);

        private Double percent;

        IntakeRollerState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }
}

