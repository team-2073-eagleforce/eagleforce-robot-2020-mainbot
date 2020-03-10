package com.team2073.robot;

import com.team2073.common.robot.RobotApplication;
import com.team2073.robot.constants.MainBotConstants;
import edu.wpi.first.wpilibj.RobotBase;

import java.io.IOException;
import java.io.InputStream;


public class Main {

    public static final boolean isMain = true;
    public static void main(String... args) throws NoSuchFieldException, IllegalAccessException {

        RobotApplication.start(() -> new RobotDelegate(.01));

    }
}
