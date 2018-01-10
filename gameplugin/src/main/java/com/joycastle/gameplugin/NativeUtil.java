package com.joycastle.gameplugin;

import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by gaoyang on 10/19/16.
 */

public class NativeUtil {

    interface InvokeJavaMethodListener {
        void onFinish(JSONObject resObject);
    }

    private static final String TAG = "NativeUtil";

    /**
     *  C++调用Java函数
     * @param className 类名
     * @param methodName 函数名
     * @param reqData json数据
     * @param requestId requestId
     * @return json数据
     */
    public static String invokeJavaMethod(String className, String methodName, String reqData, final int requestId) {
        if (requestId < 0) {
            return invokeJavaMethod(className, methodName, reqData);
        } else {
            return invokeJavaMethodAsync(className, methodName, reqData, new InvokeJavaMethodListener() {
                @Override
                public void onFinish(JSONObject resObject) {
                    invokeCppMethod(requestId, resObject.toString());
                }
            });
        }
    }

    /**
     * C++调用Java函数
     * @param className 类名
     * @param methodName 函数名
     * @param reqData json数据
     * @return json数据
     */
    private static String invokeJavaMethod(String className, String methodName, String reqData) {
        String resData = "{}";
        try {
            JSONObject reqObject = new JSONObject(reqData);
            Class clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, JSONObject.class);
            Object resObject = method.invoke(null, reqObject);
            if (resObject != null) {
                resData = resObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }

    /**
     * C++调用Java函数
     * @param className 类名
     * @param methodName 函数名
     * @param reqData json数据
     * @param listener 回调函数
     * @return json数据
     */
    private static String invokeJavaMethodAsync(String className, String methodName, String reqData, InvokeJavaMethodListener listener) {
        String resData = "{}";
        try {
            JSONObject reqObject = new JSONObject(reqData);
            Class clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, JSONObject.class, InvokeJavaMethodListener.class);
            Object resObject = method.invoke(null, reqObject, listener);
            if (resObject != null) {
                resData = resObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }

    /**
     * Java调用C++函数
     * @param responseId 对应invokeJavaMethod的requestId
     * @param resData json数据
     */
    public static native void invokeCppMethod(int responseId, String resData);
}
