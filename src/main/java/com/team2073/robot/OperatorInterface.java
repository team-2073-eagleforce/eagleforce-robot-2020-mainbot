package com.team2073.robot;

import com.team2073.common.trigger.ControllerTriggerTrigger;
import com.team2073.common.trigger.MultiTrigger;
import com.team2073.robot.command.ElevatorHeightsCommand;
import com.team2073.robot.command.InverseTrigger;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.WOF.ResetWOFCommand;
import com.team2073.robot.command.WOF.WOFPositionCommand;
import com.team2073.robot.command.WOF.WOFRotationCommand;
import com.team2073.robot.command.WOFModeTrigger;
import com.team2073.robot.command.hopper.HopperReverseCommand;
import com.team2073.robot.command.hopper.HopperStopCommand;
import com.team2073.robot.command.hopper.HopperToggleCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.command.intake.OuttakeCommand;
import com.team2073.robot.command.intake.ToggleFeederStationCommand;
import com.team2073.robot.command.intake.ToggleIntakePositionCommand;
import com.team2073.robot.command.shooter.RPMCommand;
import com.team2073.robot.command.shooter.RPMTrigger;
import com.team2073.robot.subsystem.ElevatorSubsytem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class OperatorInterface {

    private static ApplicationContext appCtx = ApplicationContext.getInstance();

    private Joystick controller = appCtx.getController();
    private Joystick driveWheel = appCtx.getDriveWheel();
    private Joystick driveStick = appCtx.getDriveStick();

    private JoystickButton a = new JoystickButton(controller, 1);
    private JoystickButton b = new JoystickButton(controller, 2);
    private JoystickButton x = new JoystickButton(controller, 3);
    private JoystickButton y = new JoystickButton(controller, 4);
    private JoystickButton lb = new JoystickButton(controller, 5);
    private InverseTrigger lbInverse = new InverseTrigger(lb);
    private JoystickButton rb = new JoystickButton(controller, 6);
    private JoystickButton controllerBack = new JoystickButton(controller, 7);
    private JoystickButton controllerStart = new JoystickButton(controller, 8);
    private POVButton dPadUp = new POVButton(controller, 0);
    private POVButton dPadRight = new POVButton(controller, 90);
    private POVButton dPadDown = new POVButton(controller, 180);
    private POVButton dPadLeft = new POVButton(controller, 270);
    private JoystickButton backTrigger = new JoystickButton(driveStick, 1);
    private JoystickButton stickTwo = new JoystickButton(driveStick, 2);
    private JoystickButton stickThree = new JoystickButton(driveStick, 3);
    private JoystickButton stickFour = new JoystickButton(driveStick, 4);
    private JoystickButton stickFive = new JoystickButton(driveStick, 5);
    private JoystickButton stickSix = new JoystickButton(driveStick, 6);
    private JoystickButton stickTen = new JoystickButton(driveStick, 10);

    private JoystickButton leftPaddle = new JoystickButton(driveWheel, 1);
    private JoystickButton wheelCircle = new JoystickButton(driveWheel, 2);
    private JoystickButton rightPaddle = new JoystickButton(driveWheel, 3);
    private JoystickButton wheelTriangle = new JoystickButton(driveWheel, 4);
    private ControllerTriggerTrigger leftTrigger = new ControllerTriggerTrigger(controller, 2);
    private ControllerTriggerTrigger rightTrigger = new ControllerTriggerTrigger(controller, 3);

    private ControllerTriggerTrigger rightWheelButton = new ControllerTriggerTrigger(driveWheel, 3);
    private ControllerTriggerTrigger leftWheelButton = new ControllerTriggerTrigger(driveWheel, 2);
    private JoystickButton L3 = new JoystickButton(driveWheel, 9);
    private JoystickButton R3 = new JoystickButton(driveWheel, 10);

    private RPMTrigger rpmTrigger = new RPMTrigger();
    private MultiTrigger ptoEngage = new MultiTrigger(leftWheelButton, rightWheelButton);
    private MultiTrigger resetAndNotStart;
    private Trigger resetWofTrigger;

    public OperatorInterface() {

    }

    public void init() {
        resetWofTrigger = new WOFModeTrigger(() -> !Mediator.getInstance().getCurrentState().equals(Mediator.RobotState.WHEEL_OF_FORTUNE));
        resetAndNotStart = new MultiTrigger(resetWofTrigger, new InverseTrigger(controllerStart), new InverseTrigger(controllerBack));
        dPadUp.whenActive(new ElevatorHeightsCommand(ElevatorSubsytem.ElevatorState.TOP));
        dPadRight.whenActive(new MediatorCommand(Mediator.RobotState.WHEEL_OF_FORTUNE));
        dPadDown.whenActive(new ElevatorHeightsCommand(ElevatorSubsytem.ElevatorState.BOTTOM));
        dPadLeft.whenActive(new MediatorCommand(Mediator.RobotState.STOW));

        a.whileHeld(new IntakeRollerCommand());
        b.whileHeld(new OuttakeCommand());
        x.whileHeld(new HopperToggleCommand());
        y.whileHeld(new HopperReverseCommand());
//        lb.whenPressed(new MediatorCommand(Mediator.RobotState.INTAKE_BALL));
        lb.toggleWhenActive(new ToggleIntakePositionCommand());
        rb.toggleWhenActive(new ToggleFeederStationCommand());
        resetAndNotStart.whenActive(new ResetWOFCommand());
        controllerBack.whenActive(new WOFPositionCommand());
        controllerStart.whenActive(new WOFRotationCommand());
//        lbInverse.toggleWhenActive(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));

        stickThree.whenActive(new CloseShotCommand(true));
        stickThree.whenActive(new MediatorCommand(Mediator.RobotState.PREP_SHOT));

        stickTwo.whenActive(new CloseShotCommand(false));
        stickTwo.whenActive(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
        backTrigger.whenActive(new MediatorCommand(Mediator.RobotState.SHOOTING));
        backTrigger.whenReleased(new MediatorCommand(Mediator.RobotState.STOW));
        rpmTrigger.whenActive(new RPMCommand());

        L3.whenPressed(new MediatorCommand(Mediator.RobotState.PREP_CLIMB));
        R3.whenPressed(new MediatorCommand(Mediator.RobotState.CLIMB));

    }

}
