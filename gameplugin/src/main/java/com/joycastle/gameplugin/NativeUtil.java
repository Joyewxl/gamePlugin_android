package com.joycastle.gameplugin;

import android.util.Log;

import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.my_facebook.FacebookHelper;

import java.lang.reflect.Method;

/**
 * Created by gaoyang on 10/19/16.
 */

public class NativeUtil {
    private static NativeUtil _instance = new NativeUtil();

    //JNI 获取调用实例
    public static Object getJavaObj(){
        System.out.print("nativeUtil getJavaObj");
        return _instance;
    }

    public void init(){
        _instance = this;

        System.out.print("NativeUtil test###");

        Log.e("native test log", "init: ", NativeUtil.nativeTest());
    }

    public void Test(){
        System.out.print("its native test function!");
        try{
            Class clazz = Class.forName("com.joycastle.social_facebook.FacebookHelper");
            Method method = clazz.getMethod("getInstance");
            FacebookHelper fbHelper = (FacebookHelper)method.invoke(null);
            fbHelper.isLogin();

        }catch (Exception e){
            System.out.print("method no refrence!");
        }

    }

    public static String invokeJavaMethod(String claxx, String method, String data, int requestId) {
        return String.valueOf(SystemUtil.getCpuTime());
    }

    public static native String nativeTest();
}
