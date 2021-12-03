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
    private static final double MAX_RPM = 11000 / 149d;

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private CANSparkMax hopperMotor = appCtx.getHopperMotor();
    private HopperState state = HopperState.STOP;
    private HopperState lastState = HopperState.STOP;

    public HopperSubsystem() {
        hopperMotor.setOpenLoopRampRate(.5);
        hopperMotor.setSmartCurrentLimit(60);
        hopperMotor.setInverted(true);
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodicAsync() {
        if (CANError.fromInt(hopperMotor.getFaults()) == CANError.kInvalid) {
            hopperMotor.clearFaults();
        }

        switch (state) {
            case STOP:
                setMotor(0);
                break;
            case REVERSE:
                setMotor(state.getRpm());
                break;
            case IDLE:
                setMotor(state.getRpm());
                break;
        }
        lastState = state;
    }

    public void setState(HopperState state) {
        this.state = state;
    }


    private void setMotor(double rpm) {
//        pid.setReference(rpm, ControlType.kVelocity);
        hopperMotor.set(rpm / MAX_RPM);
    }

    public enum HopperState {
        STOP(0),
        IDLE(50d),
        REVERSE(-75d);

        private double rpm;

        private HopperState(double rpm) {
            this.rpm = rpm;
        }

        public double getRpm() {
            return rpm;
        }
    }

}
