package com.unito.sanbotapp;

import android.os.Handler;
import android.util.Log;

import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;

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
    /*public static boolean concludeSpeak(SpeechManager speechManager) {
        try {
            while ("1".equals(speechManager.isSpeaking().getResult())) {
                Thread.sleep(1000); // Evita di sovraccaricare la CPU
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            return false;
        } catch (NullPointerException e) {
            Log.e("concludeSpeak", "SpeechManager o getResult() ha restituito null", e);
            return false;
        }
        return true;
    }*/

    /**
     * Waits until the speech is finished without blocking the UI thread
     * @param speechManager the speech manager to check
     * @param onCompleteListener callback that executes when speech completes
     */
    public static void concludeSpeak(final SpeechManager speechManager, final OnSpeechCompleteListener onCompleteListener) {
        final Handler handler = new Handler();
        final Runnable checkSpeechStatus = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!"1".equals(speechManager.isSpeaking().getResult())) {
                        // Speech is done
                        if (onCompleteListener != null) {
                            onCompleteListener.onSpeechComplete(true);
                        }
                    } else {
                        // Still speaking, check again after delay
                        handler.postDelayed(this, 500);
                    }
                } catch (NullPointerException e) {
                    Log.e("concludeSpeak", "SpeechManager or getResult() returned null", e);
                    if (onCompleteListener != null) {
                        onCompleteListener.onSpeechComplete(false);
                    }
                }
            }
        };

        // Start checking
        handler.post(checkSpeechStatus);
    }

    /**
     * Interface for speech completion callback
     */
    public interface OnSpeechCompleteListener {
        void onSpeechComplete(boolean success);
    }

    /**
     * Non-blocking version of concludeSpeak
     * @param speechManager the speech manager to check
     */
    public static void concludeSpeak(final SpeechManager speechManager) {
        concludeSpeak(speechManager, null);
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

    public static String getOperaName(int count) {
        switch (count) {
            case 0: return "Statua";
            case 1: return "Foto";
            case 2: return "Impronte";
            case 3: return "Sepolcro";
            case 4: return "Telo";
            case 5: return "Cassetta";
            case 6: return "Cassa";
            case 7: return "IntroAreaB";
            default: return "";
        }
    }
}
