package com.unito.sanbotapp;

import android.os.Bundle;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SpeechManager;

//attività per chiedere se proseguire le spiegazioni, faq e eventuali aggiunte come l'uso di IA per l'interazione con l'utente
public class InteractActivity extends TopBaseActivity{
    private final static String TAG = "INTERACT";

    SpeechManager speechManager;

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(InteractActivity.class);
        super.onCreate(savedInstanceSTate);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

    }

    @Override
    protected void onMainServiceConnected() {
    }
}