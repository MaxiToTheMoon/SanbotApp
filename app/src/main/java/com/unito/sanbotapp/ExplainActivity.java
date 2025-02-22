package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.sleepy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    //private static boolean infiniteWakeup = true;
    //private static String lastRecognizedSentence = " ";
    //private static Handler speechResponseHandler = new Handler();
    //private static Handler noResponse = new Handler();


    //@BindView(R.id.request)
    //ImageView request;


    SpeechManager speechManager;
    WheelMotionManager wheelMotionManager;
    ProjectorManager projectorManager;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(ExplainActivity.class);
        super.onCreate(savedInstanceSTate);
        setContentView(R.layout.activity_explain);


        sleepy(1);
        Log.i(TAG, "attività di spiegazione");

        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);

        //initListener(speechManager);


    }

    /*private void initListener(final SpeechManager speechManager) {
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

                        if (lastRecognizedSentence.contains("si") || lastRecognizedSentence.contains("sì")) {
                            keepExplaining(count);
                        } else if (lastRecognizedSentence.contains("no")) {
                            if (count < 6) {
                                explainOpera(count);
                                count++; // Passa all'opera successiva dopo keepExplaining
                            } else {
                                finishExplain();
                            }
                        } else {
                            speechManager.startSpeak("Non ho capito, puoi ripetere?");
                            speechManager.doWakeUp();
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
    }*/

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");

        String action = getIntent().getStringExtra("action");
        count = getIntent().getIntExtra("count", 0);

        Log.i(TAG, "count: " + count);

        if ("keepExplaining".equals(action)) {
            keepExplaining(count);
        } else if ("explainOpera".equals(action)) {
            explainOpera(count);
        } else if (count < 6) {
            explainOpera(count);
        } else {
            finishExplain();
        }
    }

    private void explainOpera(int count) {
        //infiniteWakeup = false;
        //speechManager.doSleep();

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
            default:
                throw new IllegalStateException("Unexpected value: " + count);
        }

        concludeSpeak(speechManager);

        Log.i(TAG, "Ho finito di parlare");

        sleepy(1);

        Intent intent = new Intent(ExplainActivity.this, InteractActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
        finish();

    }

    /*private void askToContinue() {

        speechManager.startSpeak("Vuoi maggiori dettagli su questa opera? Rispondi sì o no.", new SpeakOption());
        //speechManager.doWakeUp();
    }*/

    private void keepExplaining(int operaIndex) {

        // Then handle speech
        //infiniteWakeup = false;
        //
        //speechManager.doSleep();
        SpeakOption speakOption = new SpeakOption();

        switch (operaIndex) {
            case 0:
                speechManager.startSpeak("Sto continuando a spiegare 1", speakOption);
                break;
            case 1:
                speechManager.startSpeak("Sto continuando a spiegare 2", speakOption);
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

        concludeSpeak(speechManager);
        if(operaIndex == 1){
            callVideoActivity();
            return;
        }
        if (operaIndex < 6) {
            operaIndex++; // Passa all'opera successiva dopo keepExplaining
            explainOpera(operaIndex);
        } else {
            finishExplain();
        }
    }

    private void finishExplain() {
        Log.i(TAG, "finishExplain");
        Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
        ExplainActivity.this.startActivity(intent);
        finish();
    }
    private void callVideoActivity() {
        sleepy(1);
        Intent intent = new Intent(ExplainActivity.this, VideoActivity.class);
        intent.putExtra("count", count);
        ExplainActivity.this.startActivity(intent);
        finish();
    }
}
