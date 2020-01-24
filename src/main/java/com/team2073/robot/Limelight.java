package com.team2073.robot;

import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    public double getTy() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }

    public double getTx() {
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
