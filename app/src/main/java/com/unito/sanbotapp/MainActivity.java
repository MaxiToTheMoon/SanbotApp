package com.unito.sanbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.handmotion.AbsoluteAngleHandMotion;
import com.sanbot.opensdk.function.beans.headmotion.LocateAbsoluteAngleHeadMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.unit.HandMotionManager;
import com.sanbot.opensdk.function.unit.HeadMotionManager;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

import static com.unito.sanbotapp.GenericUtils.moveAndTurnLeft;
import static com.unito.sanbotapp.GenericUtils.sleepy;

import butterknife.ButterKnife;
import butterknife.BindView;


public class MainActivity extends TopBaseActivity { ;

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.imageView)
    ImageView imageView;

    WheelMotionManager wheelMotionManager;
    SpeechManager speechManager;
    ProjectorManager projectorManager;
    SystemManager systemManager;
    HandMotionManager handMotionManager;
    HeadMotionManager headMotionManager;

    Handler checkBatteryStatusHandler = new Handler();

    public static boolean busy = false;

    //head motion
    LocateAbsoluteAngleHeadMotion locateAbsoluteAngleHeadMotion = new LocateAbsoluteAngleHeadMotion(
            LocateAbsoluteAngleHeadMotion.ACTION_VERTICAL_LOCK,90,30
    );
    //hands down
    AbsoluteAngleHandMotion absoluteAngleWingMotion = new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH, 8, 180);

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
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
        systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        handMotionManager = (HandMotionManager) getUnitManager(FuncConstant.HANDMOTION_MANAGER);
        headMotionManager = (HeadMotionManager) getUnitManager(FuncConstant.HEADMOTION_MANAGER);

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

        busy = false;


        /* Display image on projector
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                projectorManager.switchProjector(true);
                projectorManager.setMode(ProjectorManager.MODE_WALL);
            }
        }, 2000);*/

        //Set stopSpeak in button's onClickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //moveAndTurnLeft(wheelMotionManager);
                Intent intent = new Intent(MainActivity.this, ExplainActivity.class);
                try {
                    MainActivity.this.startActivity(intent);

                } catch (Exception e) {
                    Log.e("ERROR", "Error starting ExplainActivity: " + e.getMessage());
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkBatteryStatusHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onMainServiceConnected() {
        systemManager.showEmotion(EmotionsType.LAUGHTER);
        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
        speechManager.startSpeak("Ciao, sono Sanbot! Come posso aiutarti?", speakOption);
        OperationResult configResult = projectorManager.queryConfig(ProjectorManager.CONFIG_SWITCH);
        if (configResult != null && "1".equals(configResult.getResult())) {
            projectorManager.switchProjector(false);
            sleepy(12);
            Log.i("PROJECTOR", "Projector OFF");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}