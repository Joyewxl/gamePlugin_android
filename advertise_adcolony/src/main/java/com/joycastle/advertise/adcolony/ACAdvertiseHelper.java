package com.joycastle.advertise.adcolony;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;
import com.joycastle.gamepluginbase.AdvertiseDelegate;
import com.joycastle.gamepluginbase.SystemUtil;

/**
 * Created by gaoyang on 9/30/16.
 */

public class ACAdvertiseHelper implements AdvertiseDelegate {
    private static final String TAG = "ACAdvertiseHelper";

    private static ACAdvertiseHelper instance = new ACAdvertiseHelper();

    private String zone_id = null;
    private VideoAdListener videoAdListener = null;

    public static ACAdvertiseHelper getInstance() {
        return instance;
    }

    private ACAdvertiseHelper() {}

    @Override
    public int showBannerAd(boolean protrait, boolean bottom, BannerAdListener listener) {
        Log.i(TAG, "didn't support");
        return 0;
    }

    @Override
    public void hideBannerAd() {
        Log.i(TAG, "didn't support");
    }

    @Override
    public boolean isInterstitialAdReady() {
        Log.i(TAG, "didn't support");
        return false;
    }

    @Override
    public boolean showInterstitialAd(InterstitialAdListener listener) {
        Log.i(TAG, "didn't support");
        return false;
    }

    @Override
    public boolean isVideoAdReady() {
        String status = AdColony.statusForZone(zone_id);
        if (status.equals("active")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean showVideoAd(VideoAdListener listener) {
        if (this.isVideoAdReady()) {
            AdColonyV4VCAd ad = new AdColonyV4VCAd(zone_id);
            ad.show();
            videoAdListener = listener;
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Adcolony";
    }

    @Override
    public void init(Application application) {
        Log.i(TAG, "Adcolony installed");
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        String origin_store = SystemUtil.getMetaData(activity, "origin_store");
        String app_id = SystemUtil.getMetaData(activity, "app_id");
        zone_id = SystemUtil.getMetaData(activity, "zone_id");
        String client_options = "version:"+ SystemUtil.getAppVersion()+",store:"+origin_store;
        AdColony.configure(activity, client_options, app_id, zone_id);
        AdColony.addV4VCListener(new AdColonyV4VCListener() {
            @Override
            public void onAdColonyV4VCReward(AdColonyV4VCReward adColonyV4VCReward) {
                if (adColonyV4VCReward.success()) {
                    videoAdListener.onResult(true, false);
                }
            }
        });
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {
        AdColony.resume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        AdColony.pause();
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
