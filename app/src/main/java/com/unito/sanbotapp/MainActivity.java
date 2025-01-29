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

import static com.unito.sanbotapp.Utils.moveAndTurnLeft;

public class MainActivity extends TopBaseActivity { ;

    WheelMotionManager wheelMotionManager;
    SpeechManager speechManager;
    ProjectorManager projectorManager;
    SystemManager systemManager;
    HandMotionManager handMotionManager;
    HeadMotionManager headMotionManager;

    Handler checkBatteryStatusHandler = new Handler();

    Button button;
    ImageView imageView;

    DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 5, 100);
    LocateAbsoluteAngleHeadMotion locateAbsoluteAngleHeadMotion = new LocateAbsoluteAngleHeadMotion(
            LocateAbsoluteAngleHeadMotion.ACTION_VERTICAL_LOCK,90,30
    );
    AbsoluteAngleHandMotion absoluteAngleWingMotion = new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH, 8, 180);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);

        //screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

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
                projectorManager.switchProjector(false);

                //hands down
                handMotionManager.doAbsoluteAngleMotion(absoluteAngleWingMotion);
                //head up
                headMotionManager.doAbsoluteLocateMotion(locateAbsoluteAngleHeadMotion);
            }
        }, 15000);

        checkBatteryStatusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int batteryLevel = systemManager.getBatteryValue();
                Log.i("BATTERY", "Battery level: " + batteryLevel);
                if(batteryLevel < 90) {
                    Intent intent = new Intent(MainActivity.this, BatteryActivity.class);
                    MainActivity.this.startActivity(intent);
                    finish();
                } else {
                    checkBatteryStatusHandler.postDelayed(this, 10000);
                }
            }
        }, 10000);
        //Show image
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.sindone
        );


        // Display image on projector
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                projectorManager.switchProjector(true);
                projectorManager.setMode(ProjectorManager.MODE_WALL);
            }
        }, 2000);

        //Set stopSpeak in button's onClickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAndTurnLeft(wheelMotionManager);
                speechManager.stopSpeak();
            }
        });
    }

    @Override
    protected void onMainServiceConnected() {
        systemManager.showEmotion(EmotionsType.LAUGHTER);
        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
        speechManager.startSpeak("Ciao, sono Sanbot! Come posso aiutarti?", speakOption);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}