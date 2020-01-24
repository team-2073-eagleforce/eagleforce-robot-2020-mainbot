package com.team2073.robot;

import com.team2073.common.trigger.ControllerTriggerTrigger;
import com.team2073.robot.command.WOF.WOFPositionCommand;
import com.team2073.robot.command.WOF.WOFRotationCommand;
import com.team2073.robot.ctx.ApplicationContext;
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

    public OperatorInterface(){
        a.whileHeld(new WOFPositionCommand("Green"));
        b.whileHeld(new WOFPositionCommand("Red"));
        x.whileHeld(new WOFPositionCommand("Blue"));
        y.whileHeld(new WOFPositionCommand("Yellow"));

        lb.whileHeld(new WOFRotationCommand());

    }

}
