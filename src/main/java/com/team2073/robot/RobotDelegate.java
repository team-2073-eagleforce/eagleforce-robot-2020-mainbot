package com.team2073.robot;


import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.ElevatorSubsytem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDelegate extends AbstractRobotDelegate {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private OperatorInterface oi;
    private IntermediateSubsystem intermediate;
    private HopperSubsystem hopper;
    private Joystick controller = appCtx.getController();
    private FlywheelSubsystem flywheel;
//    private HoodSubsystem hood;
    private TurretSubsystem turret;
    private Limelight limelight;
    private ElevatorSubsytem elevator;
    private Servo servo;

    public RobotDelegate(double period) {
        super(period);
    }

    @Override
    public void robotInit() {

        Mediator.getInstance();
        servo = appCtx.getServo();
//        limelight = appCtx.getLimelight();
//        intermediate = appCtx.getIntermediateSubsystem();
//        hopper = appCtx.getHopperSubsystem();
//        hood = appCtx.getHoodSubsystem();
////        DriveSubsystem drive = new DriveSubsystem();
//        turret = appCtx.getTurretSubsystem();
//        flywheel = appCtx.getFlywheelSubsystem();
//        intermediate = appCtx.getIntermediateSubsystem();
//        intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
//        hopper.setState(HopperSubsystem.HopperState.IDLE);
//        elevator = appCtx.getElevatorSubsystem();
        SmartDashboard.putNumber("servo", 50);
        SmartDashboard.putNumber("Flywheel RPM", 5000);
    }

    @Override
    public void robotPeriodic() {
//        boolean hopper = appCtx.getHopperSensor().get();
//        double pot = appCtx.getPotentiometer().get();
//        double wofEncoder = appCtx.getWofEncoder().get();
//        boolean elevatorSensor = appCtx.getElevatorBottomSensor().get();
//        double aChannel = appCtx.getAChannel().get();

//        if (!isOperatorControl()) {
//
//            hopper.setShotReady(false);
//
//        }
//        if (isDisabled()) {
//            intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
//            hopper.setState(HopperSubsystem.HopperState.IDLE);
//            hopper.setShotReady(false);
//        }
//        if (controller.getRawButton(5)) {
//
//            flywheel.reset();
//        }
//
//        if (controller.getRawButton(1)) {
//            hopper.setState(HopperSubsystem.HopperState.PREP_SHOT);
//        } else if (controller.getRawButton(2)) {
//            hopper.setState(HopperSubsystem.HopperState.IDLE);
//        }
//        if (controller.getRawButton(3)) {
//            hopper.setState(HopperSubsystem.HopperState.SHOOT);
//        }
//
//        if (hopper.isShotReady() && isOperatorControl()) {
//            intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
//        }
        double angle = SmartDashboard.getNumber("servo", 50);
        servo.setAngle(angle);



        if(controller.getPOV() == 180){
            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
        }else if(controller.getPOV() == 90){
            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
        }else if(controller.getPOV() == 0){
            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.TOP);
        }
        //System.out.println("Hopper: " + hopper + "\t Pot: " + pot + "\t Wof Encoder: " + wofEncoder + "\t Elevator Sensor: " + elevatorSensor + "\t aChannel: " + aChannel);
    }


    @Override
    public void testInit() {
        WOFManipulatorSubsystem.createCSV();
    }

    @Override
    public void testPeriodic() {
        wofManipulatorSubsystem.calibrate();
    }
}
