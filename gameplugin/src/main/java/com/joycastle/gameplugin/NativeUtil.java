package com.joycastle.gameplugin;

import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by gaoyang on 10/19/16.
 */

public class NativeUtil {



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
            return invokeJavaMethodAsync(className, methodName, reqData, new InvokeJavaMethodDelegate() {
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
            JSONObject rData = new JSONObject(reqData);
            String jsonStr = rData.getString("json");
            JSONArray reqArray = new JSONArray(jsonStr == null ? jsonStr : "{}");
            Class clazz = Class.forName(className);
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            Class[] argsClass = new Class[reqArray.length()];
            Object[] reqObj = new Object[reqArray.length()];
            for(int i=0; i<reqArray.length(); i++) {
                argsClass[i] = reqArray.get(i).getClass();  //获得每一个参数的实际类型
                reqObj[i] = reqArray.get(i);
            }
            Method method = clazz.getMethod(methodName, argsClass);
            Object resObject = method.invoke(instance, reqObj);
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
    private static String invokeJavaMethodAsync(String className, String methodName, String reqData, InvokeJavaMethodDelegate listener) {
        String resData = "{}";
        try {
            JSONObject rData = new JSONObject(reqData);
            String jsonStr = rData.getString("json");
            JSONArray reqArray = new JSONArray(jsonStr == null ? jsonStr : "{}");
            Class clazz = Class.forName(className);
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            int reqLength = reqArray.length();
            Class[] argsClass = new Class[reqLength+1];
            Object[] reqObj = new Object[reqLength+1];
            for(int i=0; i<reqLength; i++) {
                argsClass[i] = reqArray.get(i).getClass();  //获得每一个参数的实际类型
                reqObj[i] = reqArray.get(i);
            }
            argsClass[reqLength] = InvokeJavaMethodDelegate.class;
            reqObj[reqLength] = listener;
            Method method = clazz.getMethod(methodName, argsClass);
            Object resObject = method.invoke(instance, reqObj);
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
