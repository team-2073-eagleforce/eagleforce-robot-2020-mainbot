package com.team2073.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMax;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.common.sim.SimulationComponent;
import edu.wpi.first.wpilibj.DigitalInput;

public class RobotDelegate extends AbstractRobotDelegate {
    TalonSRX talon = new TalonSRX(3);
    TalonFX falcon = new TalonFX(5);
    DigitalInput input = new DigitalInput(3);
    CANSparkMax sparkMax = new CANSparkMax(0, CANSparkMaxLowLevel.MotorType.kBrushless);
}
