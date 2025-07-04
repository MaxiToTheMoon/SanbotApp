package com.unito.sanbotapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.handmotion.AbsoluteAngleHandMotion;
import com.sanbot.opensdk.function.beans.headmotion.LocateAbsoluteAngleHeadMotion;
import com.sanbot.opensdk.function.unit.HandMotionManager;
import com.sanbot.opensdk.function.unit.HeadMotionManager;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.sleepy;
import static com.unito.sanbotapp.GenericUtils.temporaryEmotion;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends TopBaseActivity {

    @BindView(R.id.button_a)
    Button button_a;
    @BindView(R.id.button_b)
    Button button_b;
    @BindView(R.id.exit_main)
    Button exitMain;

    SpeechManager speechManager;
    ProjectorManager projectorManager;
    SystemManager systemManager;
    HandMotionManager handMotionManager;
    HeadMotionManager headMotionManager;

    Handler checkBatteryStatusHandler = new Handler();

    //head motion
    LocateAbsoluteAngleHeadMotion locateAbsoluteAngleHeadMotion = new LocateAbsoluteAngleHeadMotion(
            LocateAbsoluteAngleHeadMotion.ACTION_VERTICAL_LOCK,90,30
    );
    //hands down
    AbsoluteAngleHandMotion absoluteAngleWingMotion = new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH, 8, 180);

    @OnClick(R.id.exit_main)
    public void exitMain(View view) {
        finish();
    }

    @OnClick(R.id.button_a)
    public void beginTourA(View view) {
        Intent intent = new Intent(MainActivity.this, ExplainActivity.class);
        intent.putExtra("tour", "a");
        try {
            MainActivity.this.startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e("ERROR", "Error starting ExplainActivity: " + e.getMessage());
        }
        finish();
    }

    @OnClick(R.id.button_b)
    public void beginTourB(View view) {
        Intent intent = new Intent(MainActivity.this, ExplainActivity.class);
        intent.putExtra("tour", "b");

        try {
            MainActivity.this.startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e("ERROR", "Error starting ExplainActivity: " + e.getMessage());
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);

        //screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //SDK's manager implementation
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
        systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        handMotionManager = (HandMotionManager) getUnitManager(FuncConstant.HANDMOTION_MANAGER);
        headMotionManager = (HeadMotionManager) getUnitManager(FuncConstant.HEADMOTION_MANAGER);

        // Disable button
        button_a.setEnabled(false);
        button_a.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        button_b.setEnabled(false);
        button_b.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        exitMain.setEnabled(false);
        exitMain.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //hands down
                handMotionManager.doAbsoluteAngleMotion(absoluteAngleWingMotion);
                //head up
                headMotionManager.doAbsoluteLocateMotion(locateAbsoluteAngleHeadMotion);
            }
        }, 1000);

        checkBatteryStatusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int batteryLevel = systemManager.getBatteryValue();
                Log.i("BATTERY", "Battery level: " + batteryLevel);
                if(batteryLevel < 20) {
                    Intent intent = new Intent(MainActivity.this, BatteryActivity.class);
                    MainActivity.this.startActivity(intent);
                    finish();
                } else {
                    checkBatteryStatusHandler.postDelayed(this, 10000);
                }
            }
        }, 10000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkBatteryStatusHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onMainServiceConnected() {
        temporaryEmotion(systemManager, EmotionsType.SMILE, 5);
        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
        speechManager.startSpeak(getString(R.string.saluti), speakOption);
        OperationResult configResult = projectorManager.queryConfig(ProjectorManager.CONFIG_SWITCH);
        if (configResult != null && "1".equals(configResult.getResult())) {
            projectorManager.switchProjector(false);
            sleepy(10);
            Log.i("PROJECTOR", "Projector OFF");
        }
        //concludeSpeak(speechManager);
        concludeSpeak(speechManager, new GenericUtils.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete(boolean success) {
                // Continue with your next steps here
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sleepy(1);
                        button_a.setEnabled(true);
                        button_a.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FEBAD")));
                        button_b.setEnabled(true);
                        button_b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FEBAD")));
                        exitMain.setEnabled(true);
                        exitMain.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FEBAD")));
                    }
                });
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}