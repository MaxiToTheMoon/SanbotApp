package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.getOperaName;
import static com.unito.sanbotapp.MoveUtils.moveToOpera;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends TopBaseActivity {
    private final static String TAG = "EXPLAIN";

    @BindView(R.id.explanation_text)
    TextView tts;

    SpeechManager speechManager;
    WheelMotionManager wheelMotionManager;
    ProjectorManager projectorManager;
    HardWareManager hardWareManager;

    private int count;
    private String[] talks;
    private String[] texts;
    private String action;
    private SpeakOption speakOption;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(ExplainActivity.class);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceSTate);
        setContentView(R.layout.activity_explain);

        Log.i(TAG, "attività di spiegazione");

        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);

        speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

        action = getIntent().getStringExtra("action") != null ? getIntent().getStringExtra("action") : "introduction";
        count = getIntent().getIntExtra("count", 0);

        Log.i(TAG, "count: " + count);
        if (count >= 6) finishExplain();

        if(action.equals("explainOpera")){
            talks = new String[]{
                    getString(R.string.statua),
                    getString(R.string.impronte),
                    getString(R.string.sepolcro),
                    getString(R.string.telo),
                    getString(R.string.cassetta),
                    getString(R.string.cassa),
                    getString(R.string.foto)
            };
            texts = new String[]{
                    getString(R.string.tStatua),
                    getString(R.string.tImpronte),
                    getString(R.string.tSepolcro),
                    getString(R.string.tTelo),
                    getString(R.string.tCassetta),
                    getString(R.string.tCassa),
                    getString(R.string.tFoto)
            };
            tts.setText(texts[count]);
        }
        else if(action.equals("keepExplaining")){
            talks = new String[]{
                    getString(R.string.statua_altro),
                    getString(R.string.impronte_altro),
                    getString(R.string.sepolcro),
                    getString(R.string.telo_altro),
                    getString(R.string.cassetta),
                    getString(R.string.cassa_altro),
                    getString(R.string.foto_altro)
            };
            texts = new String[]{
                    getString(R.string.tStatua_altro),
                    getString(R.string.tImpronte_altro),
                    getString(R.string.tSepolcro),
                    getString(R.string.tTelo_altro),
                    getString(R.string.tCassetta),
                    getString(R.string.tCassa_altro),
                    getString(R.string.tFoto_altro)
            };
            tts.setText(texts[count]);
        }
        else if(!action.equals("introduction")){
            Log.e(TAG, "Action not recognized: " + action);
            finishExplain();
        }
    }

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");

        if ("keepExplaining".equals(action)) {
            keepExplaining(count);
        } else if ("explainOpera".equals(action)) {
            explainOpera(count);
        } else if ("introduction".equals(action)){
            introduction();
        }
    }

    private void introduction(){

        moveToOpera("Introduzione", wheelMotionManager, hardWareManager);

        speechManager.startSpeak(getString(R.string.introduzione), speakOption);
        concludeSpeak(speechManager);

        Intent intent = new Intent(ExplainActivity.this, ExplainActivity.class);
        intent.putExtra("action", "explainOpera");
        startActivity(intent);
        finish();
    }

    private void explainOpera(int count) {

        moveToOpera(getOperaName(count), wheelMotionManager, hardWareManager);

        speechManager.startSpeak(talks[count], speakOption);
        concludeSpeak(speechManager);

        Intent intent = new Intent(ExplainActivity.this, InteractActivity.class);
        intent.putExtra("count", count);
        startActivity(intent);
        finish();
    }

    private void keepExplaining(int count) {

        speechManager.startSpeak(talks[count], speakOption);
        concludeSpeak(speechManager);

        if(count == 1) {
            callVideoActivity();
            return;
        }
        if (count < 6) {
            Intent intent = new Intent(ExplainActivity.this, ExplainActivity.class);
            intent.putExtra("count", count);
            intent.putExtra("action", "explainOpera");
            startActivity(intent);
            finish();
        } else {
            finishExplain();
        }
    }

    private void callVideoActivity() {
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
