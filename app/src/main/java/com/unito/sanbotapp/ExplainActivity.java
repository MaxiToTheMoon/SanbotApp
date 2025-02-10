package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.speech.SpeakStatus;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeakListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeechListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    @BindView(R.id.imageView2)
    ImageView imageView;

    SpeechManager speechManager;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(ExplainActivity.class);
        super.onCreate(savedInstanceSTate);

        setContentView(R.layout.activity_explain);
        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

        if(count == 0) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", speakOption);
            concludeSpeak(speechManager);
        }
        else if (count == 1) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.", speakOption);
            concludeSpeak(speechManager);
        }
        else if (count == 2) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Nisi ut aliquip ex ea commodo consequat.", speakOption);
            concludeSpeak(speechManager);
        }
        else if (count == 3) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Duis aute irure dolor in reprehenderit in voluptate velit esse.", speakOption);
            concludeSpeak(speechManager);
        }
        else if (count == 4) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", speakOption);
            concludeSpeak(speechManager);
        }
        else if (count == 5) {
            SpeakOption speakOption = new SpeakOption();
            speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
            speechManager.startSpeak("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", speakOption);
            concludeSpeak(speechManager);
        }
        finishExplain();
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
    }

    private void finishExplain() {
        count++;
        Intent intent = new Intent(ExplainActivity.this, MainActivity.class);
        ExplainActivity.this.startActivity(intent);
    }
}
