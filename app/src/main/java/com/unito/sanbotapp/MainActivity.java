package com.unito.sanbotapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sanbot.opensdk.base.BindBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

public class MainActivity extends BindBaseActivity {
    WheelMotionManager m_wm_manager;
    SpeechManager m_speech_manager;
    ProjectorManager m_projector_manager;
    SystemManager m_system_manager;

    Button m_button;

    //DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 5, 100);
    //RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 3, 90)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_button = (Button) findViewById(R.id.button);

        //SDK's manager implementation
        m_wm_manager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        m_speech_manager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        m_projector_manager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);

        //Set stopSpeak in m_button's onClickListener
        m_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_speech_manager.stopSpeak();
            }
        });
    }
    @Override
    protected void onMainServiceConnected() {
        m_system_manager.showEmotion(EmotionsType.LAUGHTER);
        SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);
        m_speech_manager.startSpeak("Ciao, sono Sanbot! Come posso aiutarti?", speakOption);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
