package com.team2073.robot;

import com.team2073.common.trigger.ControllerTriggerTrigger;
import com.team2073.robot.command.ElevatorHeightsCommand;
import com.team2073.robot.command.MediatorCommand;
import com.team2073.robot.command.shooter.RPMCommand;
import com.team2073.robot.command.shooter.RPMTrigger;
import com.team2073.robot.command.hopper.HopperFlipCommand;
import com.team2073.robot.command.hopper.HopperIdleCommand;
import com.team2073.robot.command.hopper.HopperStopCommand;
import com.team2073.robot.command.intake.IntakeRollerCommand;
import com.team2073.robot.command.intake.OuttakeCommand;
import com.team2073.robot.command.InverseTrigger;
import com.team2073.robot.subsystem.ElevatorSubsytem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;

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
    private JoystickButton stickTen = new JoystickButton(driveStick, 10);

    private JoystickButton leftPaddle = new JoystickButton(driveWheel, 1);
    private JoystickButton wheelCircle = new JoystickButton(driveWheel, 2);
    private JoystickButton rightPaddle = new JoystickButton(driveWheel, 3);
    private JoystickButton wheelTriangle = new JoystickButton(driveWheel, 4);
    private ControllerTriggerTrigger leftTrigger = new ControllerTriggerTrigger(controller, 2);
    private ControllerTriggerTrigger rightTrigger = new ControllerTriggerTrigger(controller, 3);

    private ControllerTriggerTrigger rightWheelButton = new ControllerTriggerTrigger(driveWheel, 3);
    private ControllerTriggerTrigger leftWheelButton = new ControllerTriggerTrigger(driveWheel, 2);

    private RPMTrigger rpmTrigger = new RPMTrigger();

    public OperatorInterface(){

    }

    public void init(){
        dPadUp.whenActive(new ElevatorHeightsCommand(ElevatorSubsytem.ElevatorState.TOP));
        dPadRight.whenActive(new MediatorCommand(Mediator.RobotState.WHEEL_OF_FORTUNE));
        dPadDown.whenActive(new ElevatorHeightsCommand(ElevatorSubsytem.ElevatorState.BOTTOM));

        a.whileHeld(new IntakeRollerCommand());
        b.whileHeld(new OuttakeCommand());
        y.whenPressed(new HopperStopCommand());
        x.whenPressed(new HopperFlipCommand());
        rightTrigger.whenActive(new HopperIdleCommand());
        lb.whenPressed(new MediatorCommand(Mediator.RobotState.INTAKE_BALL));
//        lb.toggleWhenActive(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));
//        lbInverse.toggleWhenActive(new IntakePositionCommand(IntakeSubsystem.IntakePositionState.INTAKE_OUT));

        stickTwo.whenActive(new MediatorCommand(Mediator.RobotState.PREP_SHOT));
        backTrigger.whenActive(new MediatorCommand(Mediator.RobotState.SHOOTING));
        backTrigger.whenReleased(new MediatorCommand(Mediator.RobotState.STOW));

        rpmTrigger.whenActive(new RPMCommand());

    }

}
