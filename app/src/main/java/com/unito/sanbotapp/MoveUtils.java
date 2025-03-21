package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.sleepy;

import android.util.Log;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

public class MoveUtils {

    public static void moveToOpera(String opera, WheelMotionManager wheelMotionManager, HardWareManager hardWareManager) {
        try {
            hardWareManager.switchBlackLineFilter(true);
            DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 80);
            sleepy(3);
            switch (opera) {
                case "Introduzione":
                    //wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    //sleepy(4);
                    break;
                case "Statua":
                    /*distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 40);
                    rotateAtRelativeAngle(wheelMotionManager, 180);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(8);
                    rotateAtRelativeAngle(wheelMotionManager, 180);
                    sleepy(4);*/
                    break;
                case "Impronte":
                    /*distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 180);
                    rotateAtRelativeAngle(wheelMotionManager, 90);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(10);
                    rotateAtRelativeAngle(wheelMotionManager, 65);
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 400);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(15);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 230);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(15);
                    rotateAtRelativeAngle(wheelMotionManager, 180);
                    sleepy(6);*/
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 180);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(10);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    sleepy(4);
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 310);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(10);
                    rotateAtRelativeAngle(wheelMotionManager, 180);
                    sleepy(4);
                    break;
                case "Sepolcro":
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 100);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(5);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 10, 1800);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(25);
                    rotateAtRelativeAngle(wheelMotionManager, 180);
                    sleepy(4);
                    break;
                case "Telo":
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 500);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(15);
                    rotateAtRelativeAngle(wheelMotionManager, 90);
                    sleepy(4);
                    break;
                case "Cassetta":
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 150);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(5);
                    rotateAtRelativeAngle(wheelMotionManager, 90);
                    sleepy(4);
                    break;
                case "Cassa":
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 500);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(10);
                    rotateAtRelativeAngle(wheelMotionManager, 90);
                    sleepy(4);
                    break;
                case "Foto":
                    distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 9, 600);
                    rotateAtRelativeAngle(wheelMotionManager, 270);
                    sleepy(4);
                    wheelMotionManager.doDistanceMotion(distanceWheelMotion);
                    sleepy(10);
                    rotateAtRelativeAngle(wheelMotionManager, 90);
                    sleepy(4);
                    break;
                default:
                    Log.e("MoveUtils", "Unknown opera: " + opera);
            }
        } catch (Exception e) {
            Log.e("MoveUtils", "Movement error: " + e.getMessage(), e);
        }
    }
    /**
     * rotates the robot to face a cardinal direction angle, the angle is in degrees clockwise from 0 corresponding to NORTH
     * @param wheelMotionManager the manager to let the robot rotate
     * @param currentCardinalAngle the cardinal direction is facing the robot, 0 is NORTH, 90 is EAST, 180 SOUTH, 270 WEST
     *                             /!\ IT HAS TO BE ALREADY COMPENSATED /!\ (use the compensationSanbotAngle() function on the gyro angle)
     * @param desiredCardinalAngle the cardinal direction desired the robot to face, 0 is NORTH, 90 is EAST, 180 SOUTH, 270 WEST
     * @return the angle calculated to rotate clockwise
     */
    public static int rotateAtCardinalAngle(WheelMotionManager  wheelMotionManager, int currentCardinalAngle, int desiredCardinalAngle) {
        int clockwiseRotationAngle = 0;
        //can be negative if the rotation in counter clockwise
        clockwiseRotationAngle = desiredCardinalAngle - currentCardinalAngle;
        //clockwise rotation passed to rotate
        rotateAtRelativeAngle( wheelMotionManager, clockwiseRotationAngle);
        //return the angle clockwise calculated to go back
        return clockwiseRotationAngle;
    }


    /**
     * compensates the error of the Sanbot compass, trial and error to find the magic values
     * @param passed the angle of the gyro
     * @return the angle corrected of the gyro
     */
    public static int compensationSanbotAngle(float passed) {
        float corrected = 180;
        //passed is between 0 and 60
        if (passed <=60) {
            corrected = passed/2-30;
        }
        //passed is between 60 and 180
        if (passed > 60 && passed <=180) {
            //v-60 : 120 = x : 180
            corrected=(passed-60)*180/120;
        }
        //passed is between 180 and 340
        if (passed > 180 && passed <= 340) {
            //v-180 : 160 = x-180 : 180
            corrected=((passed-180)*180/160)+180;
        }
        //passed is between 340 and 360
        if (passed > 340) {
            corrected = passed/2 + 170;
        }
        //final corrections to avoid angle overflow
        while (corrected<0)
            corrected+=360;
        while (corrected>=360)
            corrected-=360;
        return (int) corrected;
    }

    /**
     * rotation at a relative angle in the shortest way
     * @param wheelMotionManager the motion manager to rotate the robot
     * @param angle the relative angle clockwise desired to turn,
     *              can be negative to define counter clockwise
     * @return 1 if the rotation is clockwise, -1 otherwise
     */
    public static int rotateAtRelativeAngle(WheelMotionManager wheelMotionManager, int angle) {
        //correction negative angles
        while (angle < 0 ) angle = angle + 360;
        //calculation best direction
        if (angle < 180) {
            RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_RIGHT, 5, angle);
            wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
            Log.i("MoveUtils","turning right " + angle);
            return 1;
        } else {
            RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(
                    RelativeAngleWheelMotion.TURN_LEFT, 5, (360-angle));
            wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
            Log.i("MoveUtils","turning left " + (360-angle));
            return -1;
        }
    }

}