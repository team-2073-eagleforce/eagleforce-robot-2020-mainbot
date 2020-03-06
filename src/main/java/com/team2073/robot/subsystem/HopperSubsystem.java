package com.team2073.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class HopperSubsystem implements AsyncPeriodicRunnable {
    //TODO: test RPM to make sure constants are correct.
    private static final double POSITION_OFFSET = 5 / 360d;
    private static final double MAX_RPM = 11000 / 149d;
    private static final double unjamTime = .1; // how long to backspin for to unjam

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private CANSparkMax hopperMotor = appCtx.getHopperMotor();
    private DigitalInput hopperSensor = appCtx.getHopperSensor();
    private CANEncoder hopperEncoder = hopperMotor.getEncoder();
    private CANPIDController pid = hopperMotor.getPIDController();
    private HopperState state = HopperState.STOP;
    private HopperState lastState = HopperState.STOP;
    private Timer jamTimer = new Timer();
    private Timer shotReadyTimer = new Timer();
    private boolean shotReady = false;
    private boolean hasFlipped = false;
    private Double initialFlipPosition = null;
    private boolean started = false;
    private boolean hasJammed = false;
    private int timer;

    public HopperSubsystem() {
        hopperMotor.setOpenLoopRampRate(.5);
        hopperMotor.setSmartCurrentLimit(60);
        hopperMotor.setInverted(true);
        hopperEncoder.setPosition(0);
        hopperEncoder.setPositionConversionFactor(1 / 149d);
        hopperEncoder.setVelocityConversionFactor(1 / 149d);
        pid.setFF(1 / MAX_RPM);
        pid.setP(.01);
        pid.setD(.005);
        pid.setFeedbackDevice(hopperEncoder);
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodicAsync() {
            checkJam();

        if (CANError.fromInt(hopperMotor.getFaults()) == CANError.kInvalid) {
            hopperMotor.clearFaults();
        }

        switch (state) {
            case STOP:
                setMotor(0);
                break;
            case JAM:
            case IDLE:
                setMotor(state.getRpm());
                break;
            case PREP_SHOT:
                shootingPosition();
                break;
            case ELEVATOR_UP_SHOOT:
            case ELEVATOR_DOWN_SHOOT:
                if (shotReady) {
                    setMotor(state.getRpm());
                } else {
                    shootingPosition();
                }
                break;
        }
        lastState = state;
    }

    public void setState(HopperState state) {
        this.state = state;
    }

    public boolean isShotReady() {
        return shotReady;
    }

    public void setShotReady(boolean ready) {
        this.shotReady = ready;
    }

    private void shootingPosition() {
        if (hopperSensor.get() && !shotReady && !started) {
            setMotor(state.getRpm());
        } else if (!shotReady && !hopperSensor.get() && !started) {
            setMotor(0d);
            hopperMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

            shotReadyTimer.start();
            hopperEncoder.setPosition(0d);
            started = true;
        } else if (!shotReady && shotReadyTimer.hasPeriodPassed(.5) && started) {
            shotReady = true;
            shotReadyTimer.reset();
            shotReadyTimer.stop();
            started = false;
            setMotor(0);
        } else if (shotReady) {
            setMotor(0d);
        }

    }

    /**
     * Keeps the indexer at an encoder value
     */
    private void keepPosition(double target) {
        if (MathUtil.isInRange(getPosition(), target, POSITION_OFFSET)) {
            setMotor(0d);
        } else if (getPosition() > target + POSITION_OFFSET) {
            setMotor(-state.getRpm());
        } else if (getPosition() < target - POSITION_OFFSET) {
            setMotor(state.getRpm());
        }
    }

    private double getPosition() {
        return hopperEncoder.getPosition();
    }

    private void setMotor(double rpm) {
//        pid.setReference(rpm, ControlType.kVelocity);
        hopperMotor.set(rpm / MAX_RPM);
    }

    private void setMotorOpenLoop(double rpm) {
        hopperMotor.set(rpm / MAX_RPM);
    }

    private void checkJam() {
        if (!hasJammed && hopperMotor.getOutputCurrent() > 40d && hopperMotor.getAppliedOutput() > 0) {
            lastState = state;
//            System.out.println(state);
            state = HopperState.JAM;
            hasJammed = true;
            timer = 0;
            jamTimer.start();
        } else if (!(timer > 65) && hasJammed) {
            state = HopperState.JAM;
            timer++;
        } else if (hasJammed/*jamTimer.hasPeriodPassed(unjamTime)*/) {
            System.out.println("\n\n\n JAM OVER \n\n\n" + lastState);

            jamTimer.stop();
            state = lastState;
            hasJammed = false;
        }
    }

    public enum HopperState {
        STOP(0),
        IDLE(17d),
        PREP_SHOT(30d),
        ELEVATOR_UP_SHOOT(35d),
        ELEVATOR_DOWN_SHOOT(50d),
        JAM(0d);

        private double rpm;

        private HopperState(double rpm) {
            this.rpm = rpm;
        }

        public double getRpm() {
            return rpm;
        }
    }

}
