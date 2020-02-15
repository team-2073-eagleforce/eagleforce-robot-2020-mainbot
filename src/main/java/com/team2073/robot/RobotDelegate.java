package com.team2073.robot;


import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystem.*;
import edu.wpi.first.wpilibj.Joystick;

public class RobotDelegate extends AbstractRobotDelegate {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private OperatorInterface oi;
    private IntermediateSubsystem intermediate;
    private HopperSubsystem hopper;
    private Joystick controller = appCtx.getController();
    private FlywheelSubsystem flywheel;
    private HoodSubsystem hood;
    private TurretSubsystem turret;
    private Limelight limelight;
    public RobotDelegate(double period){
        super(period);
    }

    @Override
    public void robotInit() {
        limelight = appCtx.getLimelight();
        intermediate = appCtx.getIntermediateSubsystem();
        hopper = appCtx.getHopperSubsystem();
        hood = appCtx.getHoodSubsystem();
////        DriveSubsystem drive = new DriveSubsystem();
        turret = appCtx.getTurretSubsystem();
        flywheel = appCtx.getFlywheelSubsystem();
        intermediate = appCtx.getIntermediateSubsystem();
        intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
        hopper.setState(HopperSubsystem.HopperState.IDLE);

    }

    @Override
    public void robotPeriodic() {
//        boolean hopper = appCtx.getHopperSensor().get();
//        double pot = appCtx.getPotentiometer().get();
//        double wofEncoder = appCtx.getWofEncoder().get();
//        boolean elevatorSensor = appCtx.getElevatorBottomSensor().get();
//        double aChannel = appCtx.getAChannel().get();
        System.out.println("Limelight Distance: " + limelight.getLowDistance());

        if (!isOperatorControl()) {

            hopper.setShotReady(false);

        }
        if(isDisabled()){
            intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
            hopper.setState(HopperSubsystem.HopperState.IDLE);
            hopper.setShotReady(false);
            if(controller.getRawButton(5)){

                flywheel.reset();
            }

        if (controller.getRawButton(1)) {
            hopper.setState(HopperSubsystem.HopperState.PREP_SHOT);
        } else if (controller.getRawButton(2)) {
            hopper.setState(HopperSubsystem.HopperState.IDLE);
        }
        if (controller.getRawButton(3)) {
            hopper.setState(HopperSubsystem.HopperState.SHOOT);
        }

        if (hopper.isShotReady() && isOperatorControl()) {
            intermediate.set(IntermediateSubsystem.IntermediateState.SHOOT);
        }
        System.out.println("Limelight: " + limelight.getLowDistance());


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
