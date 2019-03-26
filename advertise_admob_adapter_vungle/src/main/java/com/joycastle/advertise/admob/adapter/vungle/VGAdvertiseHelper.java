package com.joycastle.advertise.admob.adapter.vungle;import android.app.Activity;import android.app.Application;import android.content.Intent;import android.os.Bundle;import android.util.Log;import com.joycastle.advertise.admob.AMAdvertiseHelper;import com.joycastle.gamepluginbase.LifeCycleDelegate;import com.joycastle.gamepluginbase.SystemUtil;import com.vungle.mediation.VungleAdapter;import com.vungle.mediation.VungleExtrasBuilder;/** * Created by joye on 2018/1/17. */public class VGAdvertiseHelper implements LifeCycleDelegate {    private static final String TAG = "VGAdvertiseHelper";    private static VGAdvertiseHelper instance = new VGAdvertiseHelper();    public static VGAdvertiseHelper getInstance() {        return instance;    }    private VGAdvertiseHelper() {}    @Override    public void init(Application application) {        String spotId = SystemUtil.getInstance().getPlatCfgValue("vungle_spot_id");        String videoId = SystemUtil.getInstance().getPlatCfgValue("vungle_video_id");        String[] placements = new String[]{spotId, videoId};        Bundle extras = new VungleExtrasBuilder(placements).build();//        AMAdvertiseHelper.getInstance().setVungle(VungleAdapter.class, extras);        Log.i(TAG, "vungle adapter is init");    }    @Override    public void onCreate(Activity activity, Bundle savedInstanceState) {    }    @Override    public void onStart(Activity activity) {    }    @Override    public void onResume(Activity activity) {    }    @Override    public void onPause(Activity activity) {    }    @Override    public void onStop(Activity activity) {    }    @Override    public void onDestroy(Activity activity) {    }    @Override    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {    }}