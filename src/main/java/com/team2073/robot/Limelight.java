package com.team2073.robot;

import com.team2073.robot.constants.AppConstants;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private double xOffset = 0d;
    private Pipeline currentPipeline = Pipeline.CLOSE;
    private AppConstants constants = AppConstants.getInstance();

    public double getTx() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }

    public double getTy() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    public double getTa() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    }

    public double getTv() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    }

    public double getTVert() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0);
    }

    public void setLedOn(boolean on) {
        if (on) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
        } else {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }
    }

    public Pipeline getCurrentPipeline() {
        return currentPipeline;
    }

    public void setCurrentPipeline(Pipeline currentPipeline) {
        this.currentPipeline = currentPipeline;
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(currentPipeline.getPipeline());
    }

    public double getHighDistance() {
        return (constants.TARGET_HEIGHT - constants.LIMELIGHT_HIGH_HEIGHT) / (Math.tan(Math.toRadians(getTy() + constants.LIMELIGHT_LENS_ANGLE)));
    }

    public double getDistanceWithElevator(double elevatorHeight) {
        return (constants.TARGET_HEIGHT - (constants.LIMELIGHT_LOW_HEIGHT + elevatorHeight)) / (Math.tan(Math.toRadians(getTy() + constants.LIMELIGHT_LENS_ANGLE)));
    }

    public double getLowDistance() {

        return (constants.TARGET_HEIGHT - constants.LIMELIGHT_LOW_HEIGHT) / (Math.tan(Math.toRadians(getTy() + constants.LIMELIGHT_LENS_ANGLE)));
    }

    public double getAreaBasedDistance() {
        return 303.31 * Math.pow(0.91, getTa());
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getAdjustedTx() {
        return getTx() + xOffset;
    }

    public enum Target {
        TRENCH(209d),
        INITIATION_LINE(10 * 12d);
        private double distance;

        Target(double distance) {
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }
    }

    public enum Pipeline {
        CLOSE(1),
        FAR(0);
        private int pipeline;

        Pipeline(int pipeline) {
            this.pipeline = pipeline;
        }

        public int getPipeline() {
            return pipeline;
        }
    }
}
