package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.rotateAtRelativeAngle;
import static com.unito.sanbotapp.GenericUtils.sleepy;

import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

public class MoveUtils {
    public static void moveToOpera(String opera, WheelMotionManager wheelMotionManager) {
        switch (opera) {
            case "Plastico":
                //move to plastico
                DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 4, 100);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                sleepy(2);
                rotateAtRelativeAngle(wheelMotionManager, 270);
                sleepy(2);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                break;
            case "Cassa":
                DistanceWheelMotion distanceWheelMotion1 = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 4, 100);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion1);
                sleepy(2);
                rotateAtRelativeAngle(wheelMotionManager, 180);
                sleepy(2);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion1);
                break;
            case "Foto":
                DistanceWheelMotion distanceWheelMotion2 = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 4, 100);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion2);
                sleepy(2);
                rotateAtRelativeAngle(wheelMotionManager, 90);
                sleepy(2);
                wheelMotionManager.doDistanceMotion(distanceWheelMotion2);
                break;
            case "Telo":
            case "Cassetta":
            case "Parti":

        }
    }
}
