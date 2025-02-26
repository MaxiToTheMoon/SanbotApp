package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.concludeSpeak;
import static com.unito.sanbotapp.GenericUtils.sleepy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.unit.SpeechManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//attività per chiedere se proseguire le spiegazioni, faq e eventuali aggiunte come l'uso di IA per l'interazione con l'utente
public class InteractActivity extends TopBaseActivity{
    private final static String TAG = "INTERACT";

    @BindView(R.id.yes)
    Button yes;

    @BindView(R.id.no)
    Button no;

    SpeechManager speechManager;
    private int count;

    @OnClick(R.id.exit_req)
    public void exitRequest(View view) {
        Intent intent = new Intent(InteractActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.yes)
    public void yes(View view) {
        Intent intent = new Intent(InteractActivity.this, ExplainActivity.class);
        intent.putExtra("action", "keepExplaining");
        intent.putExtra("count", count);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.no)
    public void no(View view) {
        if (count < 6) {
            count++; // Passa all'opera successiva dopo keepExplaining
            Intent intent = new Intent(InteractActivity.this, ExplainActivity.class);
            intent.putExtra("action", "explainOpera");
            intent.putExtra("count", count);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(InteractActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(InteractActivity.class);
        super.onCreate(savedInstanceSTate);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

        // Disable button
        yes.setEnabled(false);
        yes.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        // Disable button
        no.setEnabled(false);
        no.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
    }

    @Override
    protected void onMainServiceConnected() {

        count = getIntent().getIntExtra("count", 0);

        SpeakOption speakOption = new SpeakOption();

        speechManager.startSpeak("Vuoi maggiori dettagli su questa opera? Clicca \"sì\" o \"no.\"", speakOption);
        concludeSpeak(speechManager);
        sleepy(1);
        yes.setEnabled(true);
        yes.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FEBAD")));

        no.setEnabled(true);
        no.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FEBAD")));
    }
}