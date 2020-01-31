package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private boolean pistonsExtended = false;
    private boolean rollersActivated = false;
    private double ampLimit = 10;

    private CANSparkMax intakeMotor = appCtx.getIntakeMotor();
    private Solenoid pistonLeft = appCtx.getIntakeSolenoidLeft();
    private Solenoid pistonRight = appCtx.getIntakeSolenoidRight();
    private IntakeState state = IntakeState.DISABLED;
    private PowerDistributionPanel getMotorAmps = appCtx.getIntakeMotorAmps();

    public IntakeSubsystem(){
        intakeMotor.setOpenLoopRampRate(1);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(state.getPercent());
    }
    public void setPower(Double percent) {
        intakeMotor.set(percent);
    }
    public void set(IntakeState goalState) {
        state = goalState;
    }
    public void togglePistons(boolean value) {
        pistonLeft.set(value);
        pistonRight.set(value);
    }

    public enum IntakeState {
        STOWED(0d),
        INTAKE(1d),
        OUTTAKE(-.9),
        STOP(0d),
        DISABLED(0d);

        private Double percent;
        IntakeState(Double percent) {
            this.percent = percent;
        }
        public Double getPercent(){
            return percent;
        }
    }
    public double amperageWarning() {
    if ((getMotorAmps.getCurrent(3)) > ampLimit) {
        System.out.println("Intake motor is drawing excessive amount of amperage");
    }
    return ampLimit;
    }
}


