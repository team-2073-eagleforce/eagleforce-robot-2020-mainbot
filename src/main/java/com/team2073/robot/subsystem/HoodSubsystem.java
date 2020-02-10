package com.team2073.robot.subsystem;

import com.team2073.common.pathfollowing.math.InterpolatingDouble;
import com.team2073.common.pathfollowing.math.InterpolatingTreeMap;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.MathUtil;
import com.team2073.robot.ApplicationContext;
import com.team2073.robot.Limelight;
import edu.wpi.first.wpilibj.Servo;

public class HoodSubsystem implements PeriodicRunnable {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Servo servo = appCtx.getServo(); // WARNING: Channel is randomly chosen!
    private Limelight limelight = appCtx.getLimelight();
    private HoodState state = HoodState.RETRACTED;
    private double setPoint = 0;
    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> hoodToServo = new InterpolatingTreeMap<>();

    private static final double RETRACTED_HOOD_ANGLE = 60.0;
    private static final double EXTENDED_HOOD_ANGLE = 25.9;

    public HoodSubsystem() {
        autoRegisterWithPeriodicRunner();
        buildMap();
    }
    private void buildMap(){
        hoodToServo.put(new InterpolatingDouble(RETRACTED_HOOD_ANGLE), new InterpolatingDouble(HoodState.RETRACTED.getServoDegree()));
        hoodToServo.put(new InterpolatingDouble(EXTENDED_HOOD_ANGLE), new InterpolatingDouble(HoodState.EXTENDED.getServoDegree()));
    }

    @Override
    public void onPeriodic() {
        if (state != HoodState.CALCULATED) {
            servo.setAngle(state.getServoDegree());
        }else{
            servo.set(setPoint);
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
        double dist = limelight.getLowDistance();
        if(dist > 8 * 12){
            setHood(HoodState.EXTENDED);
        }else{
            setHood(HoodState.RETRACTED);
        }
    }
    private double hoodAngleToServoAngle(double desiredHoodAngle){
        Double interpolated = hoodToServo.getInterpolated(new InterpolatingDouble(desiredHoodAngle)).value;
        return interpolated == null ? EXTENDED_HOOD_ANGLE : interpolated;
    }

    public enum HoodState {
        RETRACTED(40.0),
        EXTENDED(140.0),
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
