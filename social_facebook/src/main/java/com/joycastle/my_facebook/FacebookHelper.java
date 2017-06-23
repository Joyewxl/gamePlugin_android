package com.joycastle.my_facebook;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.joycastle.gamepluginbase.LifeCycleDelegate;

import java.util.List;
import java.util.Map;

/**
 * Created by geekgy on 16/4/24.
 */
public class FacebookHelper implements LifeCycleDelegate {
    public interface OnLoginListener {
        void onLogin(String userId, String accessToken);
    }
    public interface OnResultListener {
        void onResult(boolean result, Map data);
    }

    private static final String TAG = "facebook";
    private Activity activity;
    private OnLoginListener loginListener;
    private CallbackManager callbackManager;
    private static FacebookHelper instance = new FacebookHelper();

    public static FacebookHelper getInstance() {
        return instance;
    }
    private FacebookHelper() {}

    public void setLoginListener(OnLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (null == token)
            return false;
        if (token.isExpired())
            return false;
        return true;
    }

    public void login(Activity activity, List<String> permissions) {
        if (this.isLogin())
            return;

        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    public String getUserId() {
        if (!this.isLogin())
            return null;
        return Profile.getCurrentProfile().getId();
    }

    public String getAccessToken() {
        if (!this.isLogin())
            return null;
        return AccessToken.getCurrentAccessToken().getToken();
    }

    public void getUserProfile(String userId, OnResultListener listener) {
        // TODO: 16/4/25 获取指定userid的用户信息
        listener.onResult(true, null);
    }

    /**
     *
     * @param shareType Links/Photos/Videos/Multimedia/OpenGraph
     * @param customInterface 使用Api
     * @param map 参数
     * @param message 默认消息，只在Api方式下有效
     * @param listener 回调
     */
    public void share(String shareType, boolean customInterface, Map<String, String> map, String message, OnResultListener listener) {
        ShareContent content = null;
        if ("Links".equals(shareType)) {
            ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
            builder.setContentUrl(map.get("ContentUrl") == null ? null : Uri.parse(map.get("ContentUrl")));
            builder.setContentTitle(map.get("ContentTitle"));
            builder.setImageUrl(map.get("ImageUrl") == null ? null : Uri.parse(map.get("ImageUrl")));
            builder.setContentDescription(map.get("ContentDescription"));
            content = builder.build();
        } else if ("Photos".equals(shareType)) {

            Bitmap bitmap = BitmapFactory.decodeFile("");
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
        } else if ("Videos".equals(shareType)) {
            Uri uri = null;
            ShareVideo video = new ShareVideo.Builder()
                    .setLocalUrl(uri)
                    .build();
            content = new ShareVideoContent.Builder()
                    .setVideo(video)
                    .setContentDescription("")
                    .setContentTitle("title")
                    .setPreviewPhoto(null)
                    .build();
        } else if ("Multimedia".equals(shareType)) {
            SharePhoto photo1 = new SharePhoto.Builder()
                    .setBitmap(null)
                    .build();
        } else if ("OpenGraph".equals(shareType)) {
            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                    .putString("og:type", "asdadwqdfwe.Jackpot")
                    .putString("og:title", "A Game of Thrones")
                    .build();
            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("asdadwqdfwe.hit")
                    .putObject("jackpot", object)
                    .build();
            content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("jackpot")
                    .setAction(action)
                    .build();
        }
        if (content == null)
            return;
        FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>(){

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i(TAG, "share success");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "share cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "share failed: "+error.toString());
            }
        };
        if (customInterface) {
            ShareApi shareApi = new ShareApi(content);
            shareApi.setMessage(message);
            shareApi.share(callback);
        } else {
            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.registerCallback(callbackManager, callback);
            shareDialog.show(content);
        }
    }

    public void logout() {
        if (!this.isLogin())
            return;
        LoginManager.getInstance().logOut();
    }

    @Override
    public void init(Application application) {

        FacebookSdk.sdkInitialize(application.getApplicationContext());
        AppEventsLogger.activateApp(application);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = AccessToken.getCurrentAccessToken().getUserId();
                String accessToken = AccessToken.getCurrentAccessToken().getToken();
                Log.i(TAG, "userId = "+userId+", accessToken = "+accessToken);
                if (null != FacebookHelper.this.loginListener)
                    FacebookHelper.this.loginListener.onLogin(userId, accessToken);
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"login cancel!");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG,"login failed!");
            }

        });
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        this.activity = activity;
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
