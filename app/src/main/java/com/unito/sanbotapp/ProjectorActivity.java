package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.sleepy;

import android.os.Bundle;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.unit.ProjectorManager;

import butterknife.ButterKnife;

public class ProjectorActivity extends TopBaseActivity {
    private ProjectorManager projectorManager;

    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(ProjectorActivity.class);
        super.onCreate(savedInstanceState);

        projectorManager = (ProjectorManager) getUnitManager(FuncConstant.PROJECTOR_MANAGER);
        ButterKnife.bind(this);

        OperationResult configResult = projectorManager.queryConfig(ProjectorManager.CONFIG_SWITCH);
        if (configResult != null && "0".equals(configResult.getResult())) {
            projectorManager.switchProjector(true);
            projectorManager.setMode(ProjectorManager.MODE_WALL);
            sleepy(12);
        }

    }

    @Override
    protected void onMainServiceConnected() {
    }

    protected void finishProjection(){
        count++;
    }
}
