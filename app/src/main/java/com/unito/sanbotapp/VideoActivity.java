package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.closeProjector;
import static com.unito.sanbotapp.GenericUtils.openProjector;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.ProjectorManager;
import com.sanbot.opensdk.function.unit.SpeechManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends TopBaseActivity {
    private final static String TAG = "VIDEO";

    @BindView(R.id.videoView)
    VideoView video;

    private ProjectorManager projectorManager;
    private SpeechManager speechManager;
    private int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "creo video");
        register(VideoActivity.class);


        //screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

        initListener();
    }

    public void initListener(){
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                speechManager.startSpeak("Chiudo il proiettore, aspetta qualche secondo!");
                closeProjector(projectorManager);
                finishThisActivity();
            }
        });
    }

    @Override
    protected void onMainServiceConnected() {
        count = getIntent().getIntExtra("count", 0);

        speechManager.startSpeak("Inizio a proiettare, aspetta qualche secondo!");
        openProjector(projectorManager);

        video = findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.macchinette));
        video.setMediaController(new MediaController(this));
        video.requestFocus();
        video.start();
        Log.i(TAG, "start video called");
    }

    private void finishThisActivity() {
        Intent intent = new Intent(VideoActivity.this, ExplainActivity.class);
        intent.putExtra("action", "explainOpera");
        intent.putExtra("count", count);
        VideoActivity.this.startActivity(intent);

        finish();
    }
}
