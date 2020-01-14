package com.team2073.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;

public class HopperSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private CANSparkMax hopperMotor = appCtx.getHopperMotor();
    private DigitalInput hopperSensor = appCtx.getHopperSensor();
    private CANEncoder hopperEncoder = hopperMotor.getEncoder();

    private final double IDLE_SPEED = 40d;
    private final double ASSUME_POSITION_SPEED = 60d;
    private final double POSITION_OFFSET = 4d;

    @Override
    public void onPeriodicAsync() {

    }

    private void idle() {
        setMotor(IDLE_SPEED);
    }

    private void shootingPosition() {
        if(hopperSensor.get()) {
            setMotor(0d);

            hopperEncoder.setPosition(0d);
        }
        else {
            setMotor(ASSUME_POSITION_SPEED);
        }
    }

    /**
     * Keeps the indexer at an encoder value
     */
    private void keepPosition(double target) {
        if(MathUtil.isInRange(getPosition(), target, POSITION_OFFSET)) {
            setMotor(0d);
        } else if(getPosition() > target + POSITION_OFFSET) {
            setMotor(- ASSUME_POSITION_SPEED);
        } else if(getPosition() < target - POSITION_OFFSET) {
            setMotor(ASSUME_POSITION_SPEED);
        }
    }

    private double getPosition() {
        //TODO do maths
        return hopperEncoder.getPosition();
    }

    private void setMotor(double speed) {
        hopperMotor.set(speed);
    }

    private boolean isJammed() {
        if (hopperMotor.getAppliedOutput() > 30d) {
            //TODO fill this out
        }

        return false;
    }

}
