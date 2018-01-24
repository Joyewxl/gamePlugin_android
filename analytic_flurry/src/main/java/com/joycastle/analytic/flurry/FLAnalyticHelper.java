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
    public void setAccoutInfo(JSONObject map){
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
//        Map<String, Object> eventData = new HashMap<>();
//        eventData.put("default", eventLabel);
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("default",eventLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.onEvent(eventId, eventData);
    }

    @Override
    public void onEvent(String eventId, JSONObject eventData) {
        Map<String, String> hashMap = new HashMap<>();
        Iterator keyIterator = eventData.keys();
        while(keyIterator.hasNext()){
            String key = (String) keyIterator.next();
            Object value = null;
            try {
                value = eventData.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            hashMap.put(key, value.toString());
        }
        FlurryAgent.onEvent(eventId, hashMap);
    }

    @Override
    public void setLevel(Integer level){
        try {
            this.onEvent("level", String.valueOf(level.intValue())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void charge(String iapId, String cash, String coin, Integer channal){
//        Map<String, Object> eventData = new HashMap<>();
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("name", iapId);
            eventData.put("cash", cash);
            eventData.put("coin", coin);
            eventData.put("channal", String.valueOf(channal.intValue()));
            this.onEvent("charge", eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void reward(String coin, Integer reason){
//        Map<String, Object> eventData = new HashMap<>();
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("coin", coin);
            eventData.put("reason", String.valueOf(reason.intValue()));
            this.onEvent("reward", eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void purchase(String good, Integer amount, String coin){
//        Map<String, Object> eventData = new HashMap<>();
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("name", good);
            eventData.put("amount", String.valueOf(amount.intValue()));
            eventData.put("coin", coin);
            this.onEvent("purchase", eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use(String good, Integer amount, String coin){
//        Map<String, Object> eventData = new HashMap<>();
        JSONObject eventData = new JSONObject();
        try {
            eventData.put("name", good);
            eventData.put("amount", String.valueOf(amount.intValue()));
            eventData.put("coin", coin);
            this.onEvent("use", eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        String appKey = SystemUtil.getMetaData(application, "Flurry_AppKey");
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
