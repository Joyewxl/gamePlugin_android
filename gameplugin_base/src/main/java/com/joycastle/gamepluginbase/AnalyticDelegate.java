package com.joycastle.gamepluginbase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by geekgy on 16/4/22.
 */
public interface AnalyticDelegate extends LifeCycleDelegate {
    /**
     * 设置账户信息
     * @param map（string，string）
     */
    public void setAccoutInfo(JSONObject map) throws JSONException;

    /**
     * 自定义事件
     * @param eventId
     */
    public void onEvent(String eventId);

    /**
     * 自定义事件
     * @param eventId
     * @param eventLabel
     */
    public void onEvent(String eventId, String eventLabel) throws JSONException;

    /**
     * 自定义事件
     * @param eventId
     * @param eventData  Map<String, Object>
     */
    public void onEvent(String eventId,JSONObject eventData) throws JSONException;

    /**
     * 设置等级
     * @param level
     */
    public void setLevel(Integer level) throws JSONException;

    /**
     * 充值
     * @param iapId
     * @param cash
     * @param coin
     * @param channal
     */
    public void charge(String iapId, Double cash, Double coin, Integer channal) throws JSONException;

    /**
     * 奖励
     * @param coin
     * @param reason
     */
    public void reward(Double coin, Integer reason) throws JSONException;

    /**
     * 购买
     * @param good
     * @param amount
     * @param coin
     */
    public void purchase(String good, Integer amount, Double coin) throws JSONException;

    /**
     * 使用
     * @param good
     * @param amount
     * @param coin
     */
    public void use(String good, Integer amount, Double coin) throws JSONException;

    /**
     * 开始任务
     * @param missionId
     */
    public void onMissionBegin(String missionId);

    /**
     * 任务达成
     * @param missionId
     */
    public void onMissionCompleted(String missionId);

    /**
     * 任务失败
     * @param missionId
     * @param reason
     */
    public void onMissionFailed(String missionId, String reason);

}
