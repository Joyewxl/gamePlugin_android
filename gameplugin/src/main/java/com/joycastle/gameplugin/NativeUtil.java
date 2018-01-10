package com.joycastle.gameplugin;

import com.joycastle.gamepluginbase.SystemUtil;

/**
 * Created by gaoyang on 10/19/16.
 */

public class NativeUtil {
    private static NativeUtil _instance = new NativeUtil();

    //JNI 获取调用实例
    public static Object getJavaObj(){ return _instance;}

    public void init(){
        _instance = this;

        System.out.print("NativeUtil test###");
    }

    public void Test(){
        System.out.print("its native test function!");
    }

    public static String invokeJavaMethod(String claxx, String method, String data, int requestId) {
        return String.valueOf(SystemUtil.getCpuTime());
    }

}
