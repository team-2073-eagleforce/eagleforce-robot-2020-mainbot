package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2073.common.motionmagic.MotionMagicHandler;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import edu.wpi.first.wpilibj.DigitalInput;

public class ElevatorSubsytem implements AsyncPeriodicRunnable {

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private double climbPercent;
    public Double setpoint;
    private static final double MAX_VELOCITY = 27d;
    private TalonFX elevatorMotor = appCtx.getElevatorMotor();
    private ElevatorPositionConverter converter = new ElevatorPositionConverter();
    // distances are in inches
    private MotionMagicHandler profile = new MotionMagicHandler(elevatorMotor, converter, 3, MAX_VELOCITY, MAX_VELOCITY / 0.3);

    private static final double ENCODER_TICS_PER_INCH = 7138.1;
    private static final double MAX_HEIGHT = 11.5;
    private static final double MIN_HEIGHT = 0;
    private static final double LOW_ZERO = 0.04;
    private static final double HIGH_ZERO = 0.8159;
    private static final double KG = 0.04;
    private static final double KV = .461/10d;
    private static final double KA = .0126;
    private static final double KP = 0.04;

    private DigitalInput bottomLimit = appCtx.getElevatorBottomSensor();
    private ElevatorState currentState = ElevatorState.BOTTOM;
    private Limelight limelight = appCtx.getLimelight();

    private boolean hasZeroed = false;
    private boolean pastSensor = false;

    public ElevatorSubsytem() {
        elevatorMotor.setSelectedSensorPosition(0,0,10);
        autoRegisterWithPeriodicRunner(10);

//        TalonUtil.resetTalon(); to do
        profile.setGains(KP, KV);
        elevatorMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);
        elevatorMotor.setInverted(true);
        elevatorMotor.configPeakOutputForward(1,10);
        elevatorMotor.configPeakOutputReverse(-1,10);
    }
    @Override
    public void onPeriodicAsync() {
        zeroElevator();
        profile.update(currentState.getHeight(), KG);
    }

    public enum ElevatorState {
        BOTTOM(.25),
        TOP(11d),
        WOF_HEIGHT(8.25);

        private Double height;

        ElevatorState(Double height) {
            this.height = height;
        }
        public double getHeight() {
            return height;
        }
    }

    public void setElevatorState(ElevatorState state){
        this.currentState = state;
    }

    public void setPower(double output){
        elevatorMotor.set(ControlMode.PercentOutput, output);
    }


    public ElevatorState getCurrentState(){
        return currentState;
    }
    public void setClimbPercent(double climbPercent){
        this.climbPercent = climbPercent;
    }
    public void set(Double setpoint){
        this.setpoint = setpoint;
    }
    private boolean isAtBottom() {
        return !bottomLimit.get();
    }
    public Double getSetpoint() {
        return setpoint;
    }

    public double position() {
        //values not permanent
        return profile.currentPosition();
    }

    public double velocity() {
        //values not permanent
        return profile.currentVelocity();
    }

    public void zeroElevator() {
        if (!hasZeroed) {
            boolean currentSensor = isAtBottom();
            if (velocity() > 0) {
                if (currentSensor != pastSensor){
                    if (currentSensor){
                        elevatorMotor.setSelectedSensorPosition(converter.asTics(LOW_ZERO));
                    }else{
                        elevatorMotor.setSelectedSensorPosition(converter.asTics(HIGH_ZERO));
                    }
                    hasZeroed = true;
                }
            } else if (velocity() < 0) {
                if (currentSensor != pastSensor){
                    if (currentSensor){
                        elevatorMotor.setSelectedSensorPosition(converter.asTics(HIGH_ZERO));
                    }else{
                        elevatorMotor.setSelectedSensorPosition(converter.asTics(LOW_ZERO));
                    }
                    hasZeroed = true;
                }
            }
            pastSensor = isAtBottom();
        }
    }

    private class ElevatorPositionConverter implements PositionConverter {

        @Override
        public double asPosition(int tics) {
            return tics / ENCODER_TICS_PER_INCH;
        }

        @Override
        public int asTics(double position) {
            return (int) (position * ENCODER_TICS_PER_INCH);
        }

        @Override
        public String positionalUnit() {
            return Units.INCHES;
        }
    }
}
