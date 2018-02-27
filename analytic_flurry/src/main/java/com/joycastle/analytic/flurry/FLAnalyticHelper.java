package com.joycastle.analytic.flurry;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.joycastle.gamepluginbase.AnalyticDelegate;
import com.joycastle.gamepluginbase.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
//        String userId = null;
//        String gender = null;
//        String age = null;
//        String accountName = null;
//        try {
//            userId = map.getString("userId");
////            gender = map.getString("gender");
////            age = map.getString("age");
//            accountName = map.getString("accountName");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (userId != null) {
//            FlurryAgent.setUserId(userId);
//        }
//        if (gender != null) {
//            if (gender.equals("male")) {
//                FlurryAgent.setGender(Constants.MALE);
//            } else if (gender.equals("female")) {
//                FlurryAgent.setGender(Constants.FEMALE);
//            }
//        }
//        if (age != null) {
//            FlurryAgent.setAge(Integer.parseInt(age));
//        }
    }

    @Override
    public void onEvent(String eventId) {
        FlurryAgent.onEvent(eventId);
    }

    @Override
    public void onEvent(String eventId, String eventLabel){
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("default", eventLabel);
        this.onEvent(eventId, eventData);
    }

    @Override
    public void onEvent(String eventId, HashMap eventData) {
        FlurryAgent.onEvent(eventId, eventData);
    }

    @Override
    public void setLevel(int level){
        this.onEvent("level", level+"");
    }

    @Override
    public void charge(String iapId, double cash, double coin, int channal){
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("name", iapId);
        eventData.put("cash", String.valueOf(cash));
        eventData.put("coin", String.valueOf(coin));
        eventData.put("channal", String.valueOf(channal));
        this.onEvent("charge", eventData);
    }

    @Override
    public void reward(double coin, int reason){
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("coin", String.valueOf(coin));
        eventData.put("reason", String.valueOf(reason));
        this.onEvent("reward", eventData);
    }

    @Override
    public void purchase(String good, int amount, double coin){
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("name", good);
        eventData.put("amount", String.valueOf(amount));
        eventData.put("coin", String.valueOf(coin));
        this.onEvent("purchase", eventData);
    }

    @Override
    public void use(String good, int amount, double coin){
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("name", good);
        eventData.put("amount", String.valueOf(amount));
        eventData.put("coin", String.valueOf(coin));
        this.onEvent("use", eventData);
    }

    @Override
    public void onMissionBegin(String missionId) {
        Log.i(TAG, "didn't support");
    }

    @Override
    public void onMissionCompleted(String missionId) {
        Log.i(TAG, "didn't support");
    }

    @Override
    public void onMissionFailed(String missionId, String reason) {
        Log.i(TAG, "didn't support");
    }

    @Override
    public void init(Application application) {
        Log.i(TAG, "Flurry installed, Version: "+FlurryAgent.getReleaseVersion());
        String appKey = SystemUtil.getInstance().getMetaData("Flurry_AppKey");
        Log.i(TAG, "appKey = "+appKey);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withListener(new FlurryAgentListener() {
                    @Override
                    public void onSessionStarted() {
                        Log.i(TAG, "Flurry Session Started");
                    }
                })
                .build(application, appKey);
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
