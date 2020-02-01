package com.team2073.robot.subsystem.Elevator;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;

public class ElevatorSubsytem implements AsyncPeriodicRunnable {

    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private double climbPercent;
    public Double setpoint;

    private ElevatorPositionConverter converter = new ElevatorPositionConverter();

    private static final double ENCODER_TICS_PER_INCH = 697d;

    private DigitalInput bottomLimit = appCtx.getElevatorZeroSensor();
    private TalonFX elevatorMotor = appCtx.getElevatorMotor();
    private ElevatorState currentState = ElevatorState.BOTTOM;

    public ElevatorSubsytem() {
        autoRegisterWithPeriodicRunner(10);

//        TalonUtil.resetTalon(); to do

        elevatorMotor.configPeakOutputForward(1,10);
        elevatorMotor.configPeakOutputReverse(-1,10);
    }

    @Override
    public void onPeriodicAsync() {

    }

    public enum ElevatorState {
        BOTTOM(0d),
        TOP(0d),
        WOFHEIGHT(0d);

        private Double height;

        ElevatorState(Double height) {
            this.height = height;
        }

        public double getValue() {
            return height;
        }
    }

    public void setElevatorState(ElevatorState state){
        this.currentState = state;
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
        return converter.asPosition(elevatorMotor.getSelectedSensorPosition(0));
    }

    public double velocity() {
        //values not permanent
        return converter.asPosition(elevatorMotor.getSelectedSensorVelocity(0) * 10);
    }

    public void zeroElevator() {
        //values not permanent
        elevatorMotor.setSelectedSensorPosition(converter.asTics(.5), 0, 10);
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
