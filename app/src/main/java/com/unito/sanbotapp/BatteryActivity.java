package com.unito.sanbotapp;

import static com.unito.sanbotapp.GenericUtils.sleepy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.ErrorCode;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.unit.ModularMotionManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;

import butterknife.ButterKnife;

public class BatteryActivity extends TopBaseActivity {
    private final static String TAG = "BATTERY";

    private WheelMotionManager wheelMotionManager;
    private SystemManager systemManager;
    private ModularMotionManager modularMotionManager;

    Handler checkBatteryStatus = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceSTate) {
        register(BatteryActivity.class);

        super.onCreate(savedInstanceSTate);

        setContentView(R.layout.activity_main);

        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        modularMotionManager = (ModularMotionManager) getUnitManager(FuncConstant.MODULARMOTION_MANAGER);

        //cyclic check battery
        checkBatteryStatus.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Attempt to switch charge on
                modularMotionManager.switchCharge(true);
                //if still not charging (glitch of SDK) switch charge off and then on
                if (!modularMotionManager.getAutoChargeStatus().getResult().equals("1")) {
                    Log.i(TAG, "battery still not charging");
                    modularMotionManager.switchCharge(false);
                    modularMotionManager.switchCharge(true);
                }
                //grab battery value
                int battery_value = systemManager.getBatteryValue();
                Log.i(TAG, "battery: "+ battery_value);
                //check if the charge is enough
                if (battery_value >= 90) {
                    finishCharging();
                } else {
                    //re-post the same handler in X seconds
                    checkBatteryStatus.postDelayed(this, 10000);
                }
            }
        }, 10000);
    }

    @Override
    protected void onMainServiceConnected() {
    }

    private void finishCharging() {
        modularMotionManager.switchCharge(false);

        //go ahead 20 cm
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(
                DistanceWheelMotion.ACTION_FORWARD_RUN,  5,20
        );
        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
        sleepy(5);

        Intent intent = new Intent(BatteryActivity.this, MainActivity.class);
        BatteryActivity.this.startActivity(intent);
        finish();
    }
}
