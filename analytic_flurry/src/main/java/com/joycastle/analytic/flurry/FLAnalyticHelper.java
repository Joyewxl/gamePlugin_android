package com.joycastle.analytic.flurry;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.joycastle.gamepluginbase.AnalyticDelegate;
import com.joycastle.gamepluginbase.SystemUtil;

import java.util.HashMap;

/**
 * Created by geekgy on 16/4/23.
 */

public class FLAnalyticHelper implements AnalyticDelegate {
    private static final String TAG = "FLAnalyticHelper";
    private static FLAnalyticHelper instance = new FLAnalyticHelper();

    public static FLAnalyticHelper getInstance() {
        return instance;
    }

    private FLAnalyticHelper() {}

    @Override
    public void setAccoutInfo(HashMap map){
        Object userId = map.get("userId");
        Object gender = map.get("gender");
        Object age = map.get("age");
        if (userId != null) {
            FlurryAgent.setUserId((String) userId);
        }
        if (gender != null) {
            if (gender.equals("male")) {
                FlurryAgent.setGender(Constants.MALE);
            } else if (gender.equals("female")) {
                FlurryAgent.setGender(Constants.FEMALE);
            }
        }
        if (age != null) {
            FlurryAgent.setAge((int)age);
        }
    }

    @Override
    public void onEvent(String eventId) {
        FlurryAgent.logEvent(eventId);
    }

    @Override
    public void onEvent(String eventId, String eventLabel){
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("key", eventLabel);
        this.onEvent(eventId, eventData);
    }

    @Override
    public void onEvent(String eventId, HashMap eventData) {
        FlurryAgent.logEvent(eventId, eventData);
    }

    @Override
    public void setLevel(int level){
    }

    @Override
    public void charge(String iapId, double cash, double coin, int channal){
    }

    @Override
    public void reward(double coin, int reason){
    }

    @Override
    public void purchase(String good, int amount, double coin){
    }

    @Override
    public void use(String good, int amount, double coin){
    }

    @Override
    public void onMissionBegin(String missionId) {
    }

    @Override
    public void onMissionCompleted(String missionId) {
    }

    @Override
    public void onMissionFailed(String missionId, String reason) {
    }

    @Override
    public void init(Application application) {
        String appKey = SystemUtil.getInstance().getMetaData("flurry_key");
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(false)
                .withContinueSessionMillis(10000)
                .build(application, appKey);
        Log.i(TAG, "Flurry installed, Version: "+FlurryAgent.getReleaseVersion());
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }
}
