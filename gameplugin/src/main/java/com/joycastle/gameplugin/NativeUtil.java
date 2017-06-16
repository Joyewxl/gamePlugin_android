package com.joycastle.gameplugin;

import com.joycastle.gamepluginbase.SystemUtil;

/**
 * Created by gaoyang on 10/19/16.
 */

public class NativeUtil {
    public static String invokeJavaMethod(String claxx, String method, String data, int requetId) {
        return String.valueOf(SystemUtil.getCpuTime());
    }
}
