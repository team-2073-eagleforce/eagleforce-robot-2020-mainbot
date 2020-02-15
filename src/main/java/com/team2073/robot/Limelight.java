package com.team2073.robot;

import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private static final double LIMELIGHT_HIGH_HEIGHT = 39d;
    private static final double LIMELIGHT_LOW_HEIGHT = 23d;
    private static final double LIMELIGHT_LENS_ANGLE = 18d;
    private static final double TARGET_HEIGHT = 87d;
    private double xOffset = 0d;

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

    public double getHighDistance(){
        //return (TARGET_HEIGHT - LIMELIGHT_HIGH_HEIGHT) / (Math.tan(Math.toRadians(getTy() + LIMELIGHT_LENS_ANGLE));
        return(TARGET_HEIGHT - LIMELIGHT_HIGH_HEIGHT) / (Math.tan(Math.toRadians(getTy() + LIMELIGHT_LENS_ANGLE)));
    }

    public double getLowDistance(){
        return (TARGET_HEIGHT - LIMELIGHT_LOW_HEIGHT) / (Math.tan(Math.toRadians(getTy() + LIMELIGHT_LENS_ANGLE)));
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

//    public void changeConfig(Target target) {
//        switch (target) {
//            case PLACE_HATCH:
//                NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
//                break;
//            case PICKUP_HATCH:
//                NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
//                break;
//            case PLACE_CARGO:
//                NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(2);
//                break;
//        }
//    }

    public enum Target {
        TRENCH,
        INITIATION_LINE,

    }
}
