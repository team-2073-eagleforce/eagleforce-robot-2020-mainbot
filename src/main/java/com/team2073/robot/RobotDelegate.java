package com.team2073.robot;


import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystem.Elevator.ElevatorSubsytem;
import com.team2073.robot.subsystem.FlywheelSubsystem;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntermediateSubsystem;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.Joystick;

public class RobotDelegate extends AbstractRobotDelegate {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private OperatorInterface oi;
    private IntermediateSubsystem intermediate;
    private ElevatorSubsytem elevatorSubsytem;
    private HopperSubsystem hopper;
    private Joystick controller = appCtx.getController();
    private FlywheelSubsystem flywheel;

    public RobotDelegate(double period) {
        super(period);
    }

    @Override
    public void robotInit() {
        oi = new OperatorInterface();
//        intermediate = appCtx.getIntermediateSubsystem();
//        hopper = appCtx.getHopperSubsystem();
//        DriveSubsystem drive = appCtx.getDriveSubsystem();
//        appCtx.getTurretSubsystem();
//        hopper = appCtx.getHopperSubsystem();
//        intermediate = appCtx.getIntermediateSubsystem();
//        intermediate.set(IntermediateSubsystem.IntermediateState.IDLE);
//        hopper.setState(HopperSubsystem.HopperState.IDLE);
//        flywheel = appCtx.getFlywheelSubsystem();
//        elevatorSubsytem = appCtx.getElevatorSubsystem();
        wofManipulatorSubsystem = appCtx.getWofManipulatorSubsystem();
    }

    @Override
    public void robotPeriodic() {
//        boolean hopper = appCtx.getHopperSensor().get();
//        double pot = appCtx.getPotentiometer().get();
//        double wofEncoder = appCtx.getWofEncoder().get();
//        boolean elevatorSensor = appCtx.getElevatorBottomSensor().get();
//        double aChannel = appCtx.getAChannel().get();
//        if (!isOperatorControl()) {
//            flywheel.reset();
//            hopper.setShotReady(false);
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
        if (isEnabled()) {
            if (controller.getPOV() == 180) {
                elevatorSubsytem.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
            }

            if (controller.getPOV() == 0) {
                elevatorSubsytem.setElevatorState(ElevatorSubsytem.ElevatorState.TOP);
            }

            if (controller.getPOV() == 90){
                elevatorSubsytem.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
            }
        }

//        if (controller.getRawButton(9)){
//            elevatorSubsytem.setPower(-.2);
//        }

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
