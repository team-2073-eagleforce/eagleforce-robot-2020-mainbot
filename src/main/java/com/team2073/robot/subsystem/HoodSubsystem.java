package com.team2073.robot.subsystem;

import com.team2073.common.pathfollowing.math.InterpolatingDouble;
import com.team2073.common.pathfollowing.math.InterpolatingTreeMap;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import edu.wpi.first.wpilibj.Servo;

public class HoodSubsystem implements PeriodicRunnable {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Servo servo = appCtx.getServo();
    private Limelight limelight = appCtx.getLimelight();
    private HoodState state = HoodState.RETRACTED;
    private double setPoint = 0;
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> hoodToServo = new InterpolatingTreeMap<>();

    private static final double RETRACTED_HOOD_ANGLE = 144.0;
    private static final double EXTENDED_HOOD_ANGLE = 46.0;

    public HoodSubsystem() {
        autoRegisterWithPeriodicRunner();
//        buildMap();
    }
    private void buildMap(){
        hoodToServo.put(new InterpolatingDouble(RETRACTED_HOOD_ANGLE), new InterpolatingDouble(HoodState.RETRACTED.getServoDegree()));
        hoodToServo.put(new InterpolatingDouble(EXTENDED_HOOD_ANGLE), new InterpolatingDouble(HoodState.EXTENDED.getServoDegree()));
    }

    @Override
    public void onPeriodic() {
        if (state != HoodState.CALCULATED) {
            determineHoodAngle();
            servo.setAngle(state.getServoDegree());
        }else{
            servo.setAngle(setPoint);
        }
    }

    public void setHood(HoodState state){
        this.state = state;
    }

    public void setHood(double hoodAngle){
        state = HoodState.CALCULATED;
        setPoint = hoodAngleToServoAngle(hoodAngle);
    }

    public void determineHoodAngle(){
        if(limelight.getTv() > 0) {
            setHood(HoodState.EXTENDED);
        }else {
            setHood(HoodState.RETRACTED);
        }
    }

    private void calculateState(double distance) {
        if (state == HoodState.RETRACTED) {
            if (distance > 132) {
                state = HoodState.EXTENDED;
            }
        } else {
            if (distance < 108) {
                state = HoodState.RETRACTED;
            }
        }
    }

    public double getHoodAngle(){
        return servo.getAngle();
    }
    private double hoodAngleToServoAngle(double desiredHoodAngle){
        Double interpolated = hoodToServo.getInterpolated(new InterpolatingDouble(desiredHoodAngle)).value;
        return interpolated == null ? EXTENDED_HOOD_ANGLE : interpolated;
    }

    public enum HoodState {
        RETRACTED(144.0),
        EXTENDED(46.0),
        CALCULATED(null);

        private Double servoDegree;

        HoodState(Double degree) {
            this.servoDegree = degree;
        }

        public Double getServoDegree() {
            return servoDegree;
        }
    }
}
