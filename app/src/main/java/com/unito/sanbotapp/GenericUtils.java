package com.unito.sanbotapp;

import android.os.Handler;
import android.util.Log;

import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

/**
 * a class for utils of SanBot
 */
public class GenericUtils {


    public static void openProjector(ProjectorManager projectorManager) {
            projectorManager.switchProjector(true);
            projectorManager.setMode(ProjectorManager.MODE_WALL);
            sleepy(12);
    }

    public static void closeProjector(ProjectorManager projectorManager) {
        projectorManager.switchProjector(false);
        sleepy(12);
        }

    /**
     * makes the thread sleep fot the seconds passed,
     * useful to avoid speech over speech.
     * @param seconds seconds to block the thread
     */
    public static void sleepy(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * waits until the speech is finished
     * @param speechManager the speech manager to check
     */
    public static boolean concludeSpeak(SpeechManager speechManager) {
        try {
            while ("1".equals(speechManager.isSpeaking().getResult())) {
                Thread.sleep(100); // Evita di sovraccaricare la CPU
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            return false;
        } catch (NullPointerException e) {
            Log.e("concludeSpeak", "SpeechManager o getResult() ha restituito null", e);
            return false;
        }
        return true;
    }

    /**
     * sets an emotion that expires after x seconds, then becomes normal
     * @param emotionPassed the emotion for the eyes
     */
    public static void temporaryEmotion(final SystemManager systemManager, EmotionsType emotionPassed, int seconds_passed) {
        systemManager.showEmotion(emotionPassed);
        //reset face after passed seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                systemManager.showEmotion(EmotionsType.NORMAL);
            }
        }, seconds_passed * 1000);
    }
    public static void temporaryEmotion(final SystemManager systemManager, EmotionsType emotionPassed) {
        //if no 2nd argument holds it for 10 seconds
        temporaryEmotion(systemManager, emotionPassed, 10);
    }




}
