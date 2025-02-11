package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.sleepy;
import static com.unito.sanbotapp.MoveUtils.moveToOpera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.speech.SpeakStatus;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeakListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeechListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    @BindView(R.id.imageView2)
    ImageView imageView;

    SpeechManager speechManager;
    WheelMotionManager wheelMotionManager;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(ExplainActivity.class);
        super.onCreate(savedInstanceSTate);
        setContentView(R.layout.activity_explain);
        Log.i(TAG, "attività di spiegazione");

        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null) return;
        count = savedInstanceState.getInt("count");
    }

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");
        Log.i(TAG, "count: " + count);
        if(count == 0) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

            //moveToOpera("Plastico", wheelMotionManager);
            speechManager.startSpeak("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", speakOption);
            sleepy(10);
            concludeSpeak(speechManager, new SpeakCompleteAction());

        }
        else if (count == 1) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            //moveToOpera("Plastico", wheelMotionManager);
            speechManager.startSpeak("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.", speakOption);
            concludeSpeak(speechManager, new SpeakCompleteAction());
        }
        else if (count == 2) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Nisi ut aliquip ex ea commodo consequat.", speakOption);
            concludeSpeak(speechManager, new SpeakCompleteAction());
        }
        else if (count == 3) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Duis aute irure dolor in reprehenderit in voluptate velit esse.", speakOption);
            concludeSpeak(speechManager, new SpeakCompleteAction());
        }
        else if (count == 4) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", speakOption);
            concludeSpeak(speechManager, new SpeakCompleteAction());
        }
        else if (count == 5) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", speakOption);
            concludeSpeak(speechManager, new SpeakCompleteAction());
        }
        keepExplaining(count, speechManager);
        finishExplain();
    }

    private void finishExplain() {
        Log.i(TAG, "finishExplain");
        count++;
        Log.i(TAG, "count aumentato");
        Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
        ExplainActivity.this.startActivity(intent);
    }

    private void keepExplaining(int count, SpeechManager speechManager) {
        Log.i(TAG, "keepExplaining");
        if(count == 0){
            speechManager.startSpeak("Sto continuando a spiegare 1");
        }
        else if(count == 1){
            speechManager.startSpeak("Sto continuando a spiegare 2");
        }
        else if(count == 2){
            speechManager.startSpeak("Sto continuando a spiegare 3");
        }
        else if(count == 3){
            speechManager.startSpeak("Sto continuando a spiegare 4");
        }
        else if(count == 4){
            speechManager.startSpeak("Sto continuando a spiegare 5");
        }
        else if(count == 5){
            speechManager.startSpeak("Sto continuando a spiegare 6");
        }
    }

    public static class SpeakCompleteAction implements Runnable {
        private TextView textView;

        // Costruttore per passare la UI da aggiornare
        public SpeakCompleteAction(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void run() {
            Log.i("Sanbot", "Discorso terminato!");

            // Passa l'aggiornamento UI al main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Discorso terminato!");
                    Log.i("Sanbot", "UI aggiornata!");
                }
            });
        }
    }
}
