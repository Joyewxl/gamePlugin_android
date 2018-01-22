package com.joycastle.advertise.admob.adapter.vungle;import android.app.Activity;import android.app.Application;import android.content.Intent;import android.os.Bundle;import com.google.android.gms.ads.AdRequest;import com.joycastle.advertise.admob.AMAdvertiseHelper;import com.joycastle.gamepluginbase.AdvertiseDelegate;import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;import com.joycastle.gamepluginbase.SystemUtil;import com.vungle.mediation.VungleAdapter;import com.vungle.mediation.VungleExtrasBuilder;/** * Created by joye on 2018/1/17. */public class VGAdvertiseHelper implements AdvertiseDelegate {    private static final String TAG = "VGAdvertiseHelper";    private static VGAdvertiseHelper instance = new VGAdvertiseHelper();    public static VGAdvertiseHelper getInstance() {        return instance;    }    private VGAdvertiseHelper() {}    @Override    public void init(Application application) {        String[] placements = new String[2];        placements[0] = SystemUtil.getMetaData(application, "vungle_spot_id");        placements[1] = SystemUtil.getMetaData(application, "vungle_video_id");        Bundle extras = new VungleExtrasBuilder(placements).build();        AMAdvertiseHelper.getInstance().setVungle(VungleAdapter.class, extras);    }    @Override    public void onCreate(Activity activity, Bundle savedInstanceState) {    }    @Override    public void onStart(Activity activity) {    }    @Override    public void onResume(Activity activity) {    }    @Override    public void onPause(Activity activity) {    }    @Override    public void onStop(Activity activity) {    }    @Override    public void onDestroy(Activity activity) {    }    @Override    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {    }    @Override    public int showBannerAd(boolean protrait, boolean bottom, BannerAdListener listener) {        return 0;    }    @Override    public void hideBannerAd() {    }    @Override    public boolean isInterstitialAdReady() {        return false;    }    @Override    public boolean showInterstitialAd(InvokeJavaMethodDelegate listener) {        return false;    }    @Override    public boolean isVideoAdReady() {        return false;    }    @Override    public boolean showVideoAd(InvokeJavaMethodDelegate listener) {        return false;    }    @Override    public String getName() {        return null;    }}