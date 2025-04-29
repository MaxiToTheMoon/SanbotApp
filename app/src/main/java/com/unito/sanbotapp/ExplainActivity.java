package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.getOperaName;
import static com.unito.sanbotapp.GenericUtils.sleepy;
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
        hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);

        speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_ITALIAN);

        action = getIntent().getStringExtra("action") != null ? getIntent().getStringExtra("action") : "introduction";
        count = getIntent().getIntExtra("count", 0);

        Log.i(TAG, "count: " + count);
        if (count >= 6) finishExplain();

        if(action.equals("explainOpera")|action.equals("introduction")){
            talks = new String[]{
                    getString(R.string.statua),
                    getString(R.string.foto),
                    getString(R.string.impronte),
                    getString(R.string.sepolcro),
                    getString(R.string.telo),
                    getString(R.string.cassetta),
                    getString(R.string.cassa)
            };
            texts = new String[]{
                    getString(R.string.tStatua),
                    getString(R.string.tFoto),
                    getString(R.string.tImpronte),
                    getString(R.string.tSepolcro),
                    getString(R.string.tTelo),
                    getString(R.string.tCassetta),
                    getString(R.string.tCassa)
            };
            tts.setText(texts[count]);
        }
        else if(action.equals("keepExplaining")){
            talks = new String[]{
                    getString(R.string.statua_altro),
                    getString(R.string.foto_altro),
                    getString(R.string.impronte_altro),
                    getString(R.string.sepolcro),
                    getString(R.string.telo_altro),
                    getString(R.string.cassetta),
                    getString(R.string.cassa_altro)
            };
            texts = new String[]{
                    getString(R.string.tStatua_altro),
                    getString(R.string.tFoto_altro),
                    getString(R.string.tImpronte_altro),
                    getString(R.string.tSepolcro),
                    getString(R.string.tTelo_altro),
                    getString(R.string.tCassetta),
                    getString(R.string.tCassa_altro)
            };
            tts.setText(texts[count]);
        }
        else{
            Log.e(TAG, "Action not recognized: " + action);
            finishExplain();
        }
    }

    @Override
    protected void onMainServiceConnected() {
        Log.i(TAG, "onMainServiceConnected");
        if ("introduction".equals(action)) {
            introduction();
        } else if ("explainOpera".equals(action)) {
            explainOpera(count);
        } else if ("keepExplaining".equals(action)){
            keepExplaining(count);
        }else{
            Log.e(TAG, "Action not recognized: " + action);
            finishExplain();
        }
    }

    private void introduction(){

        //moveToOpera("Introduzione", wheelMotionManager);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tts.setText(getText(R.string.tIntroduzione));
            }
        });
        speechManager.startSpeak(getString(R.string.introduzione), speakOption);

        // New non-blocking way
                concludeSpeak(speechManager, new GenericUtils.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete(boolean success) {
                        // Continue with your next steps here
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sleepy(3);
                                // UI updates after speech completes
                                explainOpera(0);                            }
                        });
                    }
                });
        //per video
            /*
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callVideoActivity();
                }
            });

            return;*/
    }

    private void explainOpera(final int count) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tts.setText(texts[count]);
                }
            });

        if(count != 0) {
            speechManager.startSpeak("Inizio a muovermi!", speakOption);
            sleepy(1);

            // Move robot first, then speak after movement completes
            moveToOpera(getOperaName(count), wheelMotionManager, hardWareManager);
            sleepy(1); // Give time for movement to complete
        }
        speechManager.startSpeak(talks[count], speakOption);

        concludeSpeak(speechManager, new GenericUtils.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete(boolean success) {
                // Continue with your next steps here
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // UI updates after speech completes
                        Intent intent = new Intent(ExplainActivity.this, InteractActivity.class);
                        intent.putExtra("count", count);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void keepExplaining(final int count) {
        // Update UI on UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tts.setText(texts[count]);
            }
        });

        sleepy(1); // Allow UI to render
        speechManager.startSpeak(talks[count], speakOption);
        // Use the non-blocking version with callback
        concludeSpeak(speechManager, new GenericUtils.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete(boolean success) {
                // This code runs after speech completes
                sleepy(1);

                if(count == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callVideoActivity();
                        }
                    });
                    return;
                }

                if (count < 6) {
                    final int nextCount = count + 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            action = "explainOpera";
                            explainOpera(nextCount);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finishExplain();
                        }
                    });
                }
            }
        });
    }

    private void callVideoActivity() {
        count++;
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
