package com.team2073.robot;


import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystem.HopperSubsystem;
import com.team2073.robot.subsystem.IntakeSubsystem;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.Joystick;

public class RobotDelegate extends AbstractRobotDelegate {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private OperatorInterface oi;
    private HopperSubsystem hopper;
    private IntakeSubsystem intake;
    public RobotDelegate(double period){
        super(period);
    }

    @Override
    public void robotInit() {
        DriveSubsystem drive = new DriveSubsystem();
//       hopper = appCtx.getHopperSubsystem();
//       intake = appCtx.getIntakeSubsystem();
    }
//    private Joystick xbox = new Joystick(0);
    @Override
    public void robotPeriodic() {

//        if(isOperatorControl()){
//            if(xbox.getRawButton(1)){
//               hopper.setState(HopperSubsystem.HopperState.IDLE);
//            }else if(xbox.getRawButton(2)){
//                hopper.setState(HopperSubsystem.HopperState.SHOOT);
//            }else if(xbox.getRawButton(3)){
//                hopper.setState(HopperSubsystem.HopperState.STOP);
//            }
//
//            if(xbox.getRawButton(5)){
//                intake.set(IntakeSubsystem.IntakeState.INTAKE_OUT);
//            }else if(xbox.getRawButton(6)){
//                intake.set(IntakeSubsystem.IntakeState.STOWED);
//            }
//
//        }
    }

    @Override
    public void testInit() {
//        WOFManipulatorSubsystem.createCSV();
//        wofManipulatorSubsystem = appCtx.getWofManipulatorSubsystem();
    }

    @Override
    public void testPeriodic() {
//        wofManipulatorSubsystem.calibrate();

    }
}
