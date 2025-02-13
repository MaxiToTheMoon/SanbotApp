package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.closeProjector;
import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.openProjector;
import static com.unito.sanbotapp.GenericUtils.sleepy;
import static com.unito.sanbotapp.MoveUtils.moveToOpera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.speech.Grammar;
import com.sanbot.opensdk.function.beans.speech.RecognizeTextBean;
import com.sanbot.opensdk.function.beans.speech.SpeakStatus;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.speech.RecognizeListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeakListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeechListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.WakenListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    private static boolean infiniteWakeup = true;
    private static String lastRecognizedSentence = " ";
    private static Handler speechResponseHandler = new Handler();
    private static Handler noResponse = new Handler();

    @BindView(R.id.imageView2)
    ImageView imageView;

    SpeechManager speechManager;
    WheelMotionManager wheelMotionManager;
    ProjectorManager projectorManager;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(ExplainActivity.class);
        super.onCreate(savedInstanceSTate);
        setContentView(R.layout.activity_explain);
        Log.i(TAG, "attività di spiegazione");

        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);

        // Recupera il valore di count dalle SharedPreferences
        count = getSharedPreferences("SanbotPrefs", MODE_PRIVATE)
                .getInt("count", 0);  // Valore di default: 0

    }

    private void initListener(final SpeechManager speechManager) {
        speechManager.setOnSpeechListener(new WakenListener() {
            @Override
            public void onWakeUp() {
                Log.i(TAG, "WAKE UP callback");
            }

            @Override
            public void onSleep() {
                Log.i(TAG, "SLEEP callback");
                if (infiniteWakeup) {
                    //recalling wake up to stay awake (not wake-Up-Listening() that resets the Handler)
                    speechManager.doWakeUp();
                } else {
                }
            }

            @Override
            public void onWakeUpStatus(boolean b) {

            }
        });
        speechManager.setOnSpeechListener(new RecognizeListener(){
            @Override
            public void onRecognizeText(@NonNull RecognizeTextBean recognizeTextBean) {
            }

            @Override
            public boolean onRecognizeResult(@NonNull Grammar grammar) {
                try {
                    lastRecognizedSentence = Objects.requireNonNull(grammar.getText()).toLowerCase();
                } catch (NullPointerException e) {
                    lastRecognizedSentence = "null";
                }
                speechResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Recognized: " + lastRecognizedSentence);

                        boolean recognizedValidResponse = false;

                        noResponse.removeCallbacksAndMessages(null);

                        if(lastRecognizedSentence.contains("si")||lastRecognizedSentence.contains("sì")){
                            keepExplaining(count);
                        }
                        else if(lastRecognizedSentence.contains("no")){
                            if (count < 6) {
                                spiegaOpera(count);
                                count++; // Passa all'opera successiva dopo keepExplaining
                            } else {
                                finishExplain();
                            }
                        }


                    }
                });
                return true;
            }

            @Override
            public void onRecognizeVolume(int i) {
            }

            @Override
            public void onStartRecognize() {

            }

            @Override
            public void onStopRecognize() {

            }

            @Override
            public void onError(int i, int i1) {
                Log.i(TAG, "ERROR callback");
            }

        });
    }

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");
        Log.i(TAG, "count: " + count);

        if (count < 6) {
            spiegaOpera(count);
            count++; // Incrementa subito per evitare duplicazioni
        } else {
            finishExplain();
        }
    }

    private void spiegaOpera(int count) {
        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

        switch (count) {
            case 0:
                speechManager.startSpeak("Lorem ipsum dolor sit amet.", speakOption);
                break;
            case 1:
                speechManager.startSpeak("Ut enim ad minim veniam.", speakOption);
                break;
            case 2:
                speechManager.startSpeak("Nisi ut aliquip ex ea commodo consequat.", speakOption);
                break;
            case 3:
                speechManager.startSpeak("Duis aute irure dolor in reprehenderit.", speakOption);
                break;
            case 4:
                speechManager.startSpeak("Excepteur sint occaecat cupidatat.", speakOption);
                break;
            case 5:
                speechManager.startSpeak("Lorem ipsum dolor sit amet.", speakOption);
                break;
        }

        concludeSpeak(speechManager, new Runnable() {
            @Override
            public void run() {
                askToContinue();
            }
        });
    }

    private void askToContinue() {
        speechManager.startSpeak("Vuoi maggiori dettagli su questa opera? Rispondi sì o no.", new SpeakOption());

        speechManager.setOnSpeechListener(new SpeechListener() {
            @Override
            public void onStartRecognize() {
                speechManager.startListen();
            }

            @Override
            public void onRecognizeText(String text) {
                Log.i(TAG, "Utente ha detto: " + text);
                if (text.equalsIgnoreCase("sì") || text.equalsIgnoreCase("si")) {
                    keepExplaining(count - 1); // Usa count - 1 perché lo abbiamo già incrementato
                } else if (text.equalsIgnoreCase("no")) {
                    if (count < 6) {
                        spiegaOpera(count);
                        count++; // Continua con la prossima opera
                    } else {
                        finishExplain();
                    }
                } else {
                    speechManager.startSpeak("Non ho capito, puoi ripetere?");
                    askToContinue();
                }
            }

            @Override
            public void onRecognizeResult(List<String> results) {}

            @Override
            public void onError() {
                speechManager.startSpeak("Errore nel riconoscimento. Puoi ripetere?");
                askToContinue();
            }
        });
    }

    private void keepExplaining(int operaIndex) {
        Log.i(TAG, "keepExplaining");

        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

        switch (operaIndex) {
            case 0:
                speechManager.startSpeak("Sto continuando a spiegare 1", speakOption);
                break;
            case 1:
                openProjector(projectorManager);
                speechManager.startSpeak("Sto continuando a spiegare 2", speakOption);
                closeProjector(projectorManager);
                break;
            case 2:
                speechManager.startSpeak("Sto continuando a spiegare 3", speakOption);
                break;
            case 3:
                speechManager.startSpeak("Sto continuando a spiegare 4", speakOption);
                break;
            case 4:
                speechManager.startSpeak("Sto continuando a spiegare 5", speakOption);
                break;
            case 5:
                speechManager.startSpeak("Sto continuando a spiegare 6", speakOption);
                break;
        }

        concludeSpeak(speechManager, new Runnable() {
            @Override
            public void run() {
                if (count < 6) {
                    spiegaOpera(count);
                    count++; // Passa all'opera successiva dopo keepExplaining
                } else {
                    finishExplain();
                }
            }
        });
    }


    private void nextExplain(int count, SpeechManager speechManager) {
        Log.i(TAG, "nextExplain");
        // Recupera le SharedPreferences e salva il nuovo valore di count
        getSharedPreferences("SanbotPrefs", MODE_PRIVATE)
                .edit()
                .putInt("count", count)
                .apply();  // Salvataggio asincrono
        Log.i(TAG, "count aumentato");
    }

    private void finishExplain() {
        Log.i(TAG, "finishExplain");
        getSharedPreferences("SanbotPrefs", MODE_PRIVATE)
                .edit()
                .remove("count")  // Rimuove il valore salvato
                .apply();
        Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
        ExplainActivity.this.startActivity(intent);
        finish();
    }

    public static class SpeakCompleteAction implements Runnable {
        //private TextView textView;

        // Costruttore per passare la UI da aggiornare
        public SpeakCompleteAction() {

        }

        @Override
        public void run() {
            Log.i("Sanbot", "Discorso terminato!");

            // Passa l'aggiornamento UI al main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //textView.setText("Discorso terminato!");
                    Log.i("Sanbot", "UI aggiornata!");
                }
            });
        }
    }
}
