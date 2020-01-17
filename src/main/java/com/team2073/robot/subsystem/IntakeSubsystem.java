package com.team2073.robot.subsystem;

import com.revrobotics.CANSparkMax;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeSubsystem implements AsyncPeriodicRunnable {

    private final RobotContext robotContext = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private boolean pistonsExtended = false;
    private boolean rollersActivated = false;

    private CANSparkMax intakeMotor = appCtx.getIntakeMotor();
    private Solenoid piston = new Solenoid(1);

    @Override
    public void onPeriodicAsync() {
        if (appCtx.getController().getRawButton(1)) {
            togglePistons();
            //Button 1 is a placeholder until we know which button to assign
        }
        if (appCtx.getController().getRawButton(2)) {
            rampRPM();
            //Button 2 is a placeholder until we know which button to assign
        }
        if (appCtx.getController().getRawButton(3)) {
            toggleRollers();
            //Button 3 is a placeholder until we know which button to assign
        }
        if (appCtx.getController().getRawButton(4)) {
            reverseMotor();
            //Button 4 is a placeholder until we know which button to assign
        }
    }

    public void togglePistons() {
        if (!pistonsExtended) {
            piston.set(true);
            pistonsExtended = true;
        } else {
            piston.set(false);
            pistonsExtended = false;
        }
    }

    private void rampRPM() {

    }

    private void toggleRollers() {
        if (!rollersActivated) {
            intakeMotor.set(.5);
            rollersActivated = true;
        } else {
            intakeMotor.set(0);
            rollersActivated = false;
        }
    }
    private void reverseMotor() {
        intakeMotor.set(-.5);
    }
}

