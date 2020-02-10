package com.team2073.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class HopperSubsystem implements AsyncPeriodicRunnable {

    private static final double POSITION_OFFSET = 5/360d;
    private static final double MAX_RPM = 73d;
    private static final double unjamTime = .1; // how long to backspin for to unjam

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private CANSparkMax hopperMotor = appCtx.getHopperMotor();
//    private DigitalInput hopperSensor = appCtx.getHopperSensor();
    private CANEncoder hopperEncoder = hopperMotor.getEncoder();

    private HopperState state = HopperState.STOP;
    private HopperState lastState = HopperState.STOP;

    private Timer jamTimer = new Timer();
    private boolean shotReady = true;

    public HopperSubsystem(){
        hopperMotor.setOpenLoopRampRate(0.5);
        hopperMotor.setSmartCurrentLimit(30);
        hopperMotor.setInverted(true);
        hopperEncoder.setPositionConversionFactor(1/125d);
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodicAsync() {
        checkJam();
//        System.out.println(hopperMotor.getOutputCurrent());
        switch (state) {
            case STOP:
                setMotor(0);
                break;
            case JAM:
                setMotor(state.getRpm());
                break;
            case IDLE:
                setMotor(state.getRpm());
                break;
            case PREP_SHOT:
                shootingPosition();
                break;
            case SHOOT:
                if(shotReady){
                    setMotor(state.getRpm());
                }else {
                    shootingPosition();
                }
        }
    }

    public void setState(HopperState state){
        this.state = state;
    }

    private void shootingPosition() {
//        if (!hopperSensor.get()) {
//            setMotor(state.getRpm());

//        } else if (!shotReady){
//            setMotor(0d);
//            shotReady = true;
//            hopperEncoder.setPosition(0d);
//        }else{
//            keepPosition(0);
//        }
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
        hopperMotor.set(rpm / MAX_RPM);
    }

    private boolean hasJammed = false;
    private int timer;
    private void checkJam() {
        if (!hasJammed && hopperMotor.getOutputCurrent() > 15d && hopperMotor.getAppliedOutput() > 0) {
            lastState = state;
            System.out.println(state);
            state = HopperState.JAM;
            hasJammed = true;
            timer = 0;
            jamTimer.start();
        }else if(!(timer>65) && hasJammed){
            state = HopperState.JAM;
            timer ++;
        }else if(hasJammed/*jamTimer.hasPeriodPassed(unjamTime)*/) {
            System.out.println("\n\n\n JAM OVER \n\n\n" + lastState);

            jamTimer.stop();
            state = lastState;
            hasJammed = false;
        }
    }

    public enum HopperState {
        STOP(0),
        IDLE(20d),
        PREP_SHOT(10d),
        SHOOT(60d),
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
