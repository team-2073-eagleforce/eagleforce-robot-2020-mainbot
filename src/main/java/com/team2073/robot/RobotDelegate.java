package com.team2073.robot;


import com.ctre.phoenix.sensors.PigeonIMU;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.command.auton.*;
import com.team2073.robot.constants.MainBotConstants;
import com.team2073.robot.subsystem.*;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2073.robot.Main.isMain;

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
    private ElevatorSubsytem elevator;
    private PigeonIMU gryo;
    private DriveSubsystem drive;
    //    private Servo servo = appCtx.getServo();
    private boolean started = false;
    private boolean end = false;
    private AutoRun autonomous;
    private SendableChooser<AutoRun> autonRun;

    public RobotDelegate(double period) {
        super(period);
    }

    @Override
    public void robotInit() {
        autonRun = new SendableChooser<>();
        autonomous = AutoRun.SHOOT_THREE;
        CameraServer.getInstance().startAutomaticCapture();
        if (isMain) {
            try {
                new MainBotConstants().applyMainbotChanges();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Mediator.getInstance();
        oi = new OperatorInterface();
        oi.init();
        drive = appCtx.getDriveSubsystem();
//        SmartDashboard.putNumber("servo", 0);
        SmartDashboard.putNumber("Flywheel RPM", 5000);

        autonRun.addOption("TEST", AutoRun.TEST);
        autonRun.addOption("SHOOT THREE", AutoRun.SHOOT_THREE);
        autonRun.addOption("TOP TEN", AutoRun.TOP_TEN);
        autonRun.addOption("TRENCH", AutoRun.TRENCH);
        autonRun.addOption("SLALOM", AutoRun.SLALOM);
        autonRun.addOption("BARREL", AutoRun.BARREL);
        autonRun.addOption("BOUNCE", AutoRun.BOUNCE);
        autonRun.addOption("GalaticSearch", AutoRun.GALACTIC_SEARCH);
        SmartDashboard.putData(autonRun);
//        SmartDashboard.putNumber("servo", servo.getAngle());

    }

    @Override
    public void robotPeriodic() {
        drive.getOutput();
        if (isDisabled()) {
            autonomous = autonRun.getSelected();
        }

        if (isAutonomous() && isEnabled()) {
            if (!started) {
                System.out.println("STARTED");
                if (autonomous == AutoRun.TEST) {
                    drive.resetPosition(46d, 44d, 0d);
                    new Test().start();

                } else if (autonomous == AutoRun.SHOOT_THREE) {
                    drive.resetPosition(130d, 112d - 28, 0d);
//                    new Shoot3Pick5Straight().start();

                } else if (autonomous == AutoRun.SLALOM) {
                    drive.resetPosition(44d, 30d, 0d);
                    new Slalom().start();
                } else if (autonomous == AutoRun.BARREL) {
                    drive.resetPosition(47d, 90d, 0d);
                    new Barrel().start();
                } else if (autonomous == AutoRun.BOUNCE) {
                    drive.resetPosition(45d, 90d, 0d);
////                    drive.resetPosition(195d, 150d, 260);
                    new Bounce().start();
                } else if (autonomous == AutoRun.GALACTIC_SEARCH){
                    drive.resetPosition(15d, 150d, 0d);
                    new GalaticSearch().start();
                }else {
                    System.out.println("NOTHING SET");
                }
            }
//                    new TopSide10Ball().start();
            started = true;
        }
    }


//        if(RobotState.isEnabled() && !started){
//            AutonStarter starter = new AutonStarter();
//            starter.getAutonomousCommand().schedule();
//            started = true;
//            CommandScheduler.getInstance().enable();
//        }
//        CommandScheduler.getInstance().run();

    //System.out.println("Velocity: " +appCtx.getHopperMotor().getEncoder().getVelocity() + "\t Output: " + appCtx.getHopperMotor().getAppliedOutput());
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
//        double angle = SmartDashboard.getNumber("servo", 50);
//        servo.setAngle(angle);


//        if(controller.getPOV() == 180){
//            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.BOTTOM);
//        }else if(controller.getPOV() == 90){
//            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.WOF_HEIGHT);
//        }else if(controller.getPOV() == 0){
//            elevator.setElevatorState(ElevatorSubsytem.ElevatorState.TOP);
//        }
    //System.out.println("Hopper: " + hopper + "\t Pot: " + pot + "\t Wof Encoder: " + wofEncoder + "\t Elevator Sensor: " + elevatorSensor + "\t aChannel: " + aChannel);
//        limelight.setLedOn(true);
//        System.out.println("LOW Distance: " + limelight.getLowDistance() + "\tHIGH Distance: " + limelight.getHighDistance());




    @Override
    public void testInit() {
        WOFManipulatorSubsystem.createCSV();
    }

    @Override
    public void testPeriodic() {
        wofManipulatorSubsystem.calibrate();
    }

    public enum AutoRun {
        TEST,
        SHOOT_THREE,
        TOP_TEN,
        SLALOM,
        BARREL,
        BOUNCE,
        GALACTIC_SEARCH,
        TRENCH;
    }
}



