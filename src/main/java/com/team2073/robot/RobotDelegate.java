package com.team2073.robot;


import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.subsystem.WOFManipulatorSubsystem;
import com.team2073.robot.subsystem.drive.DriveSubsystem;

public class RobotDelegate extends AbstractRobotDelegate {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private WOFManipulatorSubsystem wofManipulatorSubsystem;
    private OperatorInterface oi;
    public RobotDelegate(double period){
        super(period);
    }

    @Override
    public void robotInit() {
        DriveSubsystem drive = new DriveSubsystem();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void testInit() {
        WOFManipulatorSubsystem.createCSV();
        wofManipulatorSubsystem = appCtx.getWofManipulatorSubsystem();
    }

    @Override
    public void testPeriodic() {
        wofManipulatorSubsystem.calibrate();
    }
}
