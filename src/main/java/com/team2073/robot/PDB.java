package com.team2073.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;

public class PDB {
    private PowerDistributionPanel pdp = new PowerDistributionPanel();

    public static PDB instance = null;

    public static PDB getInstance(){
        if(instance == null){
            instance = new PDB();
        }
        return instance;
    }

    public double getCurrent(int port){
        return pdp.getCurrent(port == 16 ? 0 : port);
    }
}
