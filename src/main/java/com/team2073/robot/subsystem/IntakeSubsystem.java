package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private boolean pistonsExtended = false;
    private boolean rollersActivated = false;

    private CANSparkMax intakeMotor = appCtx.getIntakeMotor();
    private Solenoid pistonLeft = appCtx.getIntakeSolenoidLeft();
    private Solenoid pistonRight = appCtx.getIntakeSolenoidRight();
    private IntakeState states = IntakeState.Disabled;

    public IntakeSubsystem(){
        intakeMotor.setOpenLoopRampRate(1);
    }

    @Override
    public void onPeriodicAsync() {
        setPower(states.getPercent());
    }
    public void setPower(Double percent) {
        intakeMotor.set(percent);
    }
    public void set(IntakeState goalState) {
        states = goalState;
    }
    public void togglePistons(boolean value) {
        pistonLeft.set(value);

    }

    public enum IntakeState {
        Stowed(0d),
        Intake(1d),
        Outtake(-.9),
        Stop(0d),
        Disabled(0d);

        private Double percent;
        IntakeState(Double percent) {
            this.percent = percent;
        }
        public Double getPercent(){
            return percent;
        }
    }
}

