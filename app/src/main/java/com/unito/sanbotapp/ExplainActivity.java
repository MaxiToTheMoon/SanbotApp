package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.sleepy;
import static com.unito.sanbotapp.MoveUtils.moveToOpera;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    //private static boolean infiniteWakeup = true;
    //private static String lastRecognizedSentence = " ";
    //private static Handler speechResponseHandler = new Handler();
    //private static Handler noResponse = new Handler();


    //@BindView(R.id.request)
    //ImageView request;
    @BindView(R.id.explanation_text)
    TextView tts;


    SpeechManager speechManager;
    WheelMotionManager wheelMotionManager;
    ProjectorManager projectorManager;

    private int count;
    private String[] texts;
    private String action;
    private String textToShow;
    private boolean isViewLoaded = false;
    private boolean isServiceConnected = false;

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

        action = getIntent().getStringExtra("action");
        count = getIntent().getIntExtra("count", 0);

        Log.i(TAG, "count: " + count);
        //initListener(speechManager);

        if(action.equals("explainOpera")) {
            // Precarica i testi
            texts = new String[]{
                    getString(R.string.statua),
                    getString(R.string.impronte),
                    getString(R.string.sepolcro),
                    getString(R.string.telo),
                    getString(R.string.cassetta),
                    getString(R.string.cassa),
                    getString(R.string.foto)
            };
            if(count>=texts.length) finishExplain();
            final ViewTreeObserver observer = tts.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tts.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Update view and log once layout is complete
                    tts.setText(texts[count]);
                    Log.i(TAG, "View loaded and text set");

                    isViewLoaded = true;
                    proceedIfReady();
                }
            });
        }
        else if(action.equals("keepExplaining")) {
            // Precarica i testi
            texts = new String[]{
                    getString(R.string.statua_altro),
                    getString(R.string.impronte_altro),
                    getString(R.string.sepolcro),
                    getString(R.string.telo_altro),
                    getString(R.string.cassetta),
                    getString(R.string.cassa_altro),
                    getString(R.string.foto_altro)
            };
            if(count>=texts.length) finishExplain();
            final ViewTreeObserver observer = tts.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tts.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Update view and log once layout is complete
                    tts.setText(texts[count]);
                    Log.i(TAG, "View loaded and text set");

                    isViewLoaded = true;
                    proceedIfReady();
                }
            });
        }
        else {
            Log.e(TAG, "Action not recognized: " + action);
            finishExplain();
        }
    }

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");

        isServiceConnected = true;
        proceedIfReady();
    }

    private void explainOpera(int count) {

        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

        switch (count) {
            case 0:
                moveToOpera("Statua", wheelMotionManager);
                break;
            case 1:
                moveToOpera("Impronte", wheelMotionManager);
                break;
            case 2:
                moveToOpera("Sepolcro", wheelMotionManager);
                break;
            case 3:
                moveToOpera("Telo", wheelMotionManager);
                break;
            case 4:
                moveToOpera("Cassetta", wheelMotionManager);
                break;
            case 5:
                moveToOpera("Cassa", wheelMotionManager);
                break;
            case 6:
                moveToOpera("Foto", wheelMotionManager);
                break;
            default:
                Log.e(TAG, "Opera not recognized: " + count);
                finishExplain();
                return;
        }
        // Gestisci il discorso
        speechManager.startSpeak(textToShow, speakOption);

        concludeSpeak(speechManager);

        Log.i(TAG, "Ho finito di parlare");

        sleepy(1);

        Intent intent = new Intent(ExplainActivity.this, InteractActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
        finish();

    }

    private void keepExplaining(int count) {

        SpeakOption speakOption = new SpeakOption();

        // Gestisci il discorso
        speechManager.startSpeak(textToShow, speakOption);

        concludeSpeak(speechManager);
        if(count == 1){
            callVideoActivity();
            return;
        }
        if (count < 7) {
            count++; // Passa all'opera successiva dopo keepExplaining
            explainOpera(count);
        } else {
            finishExplain();
        }
    }

    private void proceedIfReady() {
        if (isViewLoaded && isServiceConnected) {
            textToShow = texts[count];
            // Aggiorna l'interfaccia utente immediatamente
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tts.setText(textToShow);
                }
            });
            if ("keepExplaining".equals(action)) {
                keepExplaining(count);
            } else if ("explainOpera".equals(action)) {
                explainOpera(count);
            } else if (count < 7) {
                explainOpera(count);
            } else {
                finishExplain();
            }
        }
    }

    private void callVideoActivity() {
        sleepy(1);
        Intent intent = new Intent(ExplainActivity.this, VideoActivity.class);
        intent.putExtra("count", count);
        ExplainActivity.this.startActivity(intent);
        finish();
    }

    private void finishExplain() {
        Log.i(TAG, "finishExplain");
        Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
        ExplainActivity.this.startActivity(intent);
        finish();
    }
}
