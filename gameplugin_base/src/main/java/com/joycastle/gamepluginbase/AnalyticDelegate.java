package com.joycastle.gamepluginbase;

import java.util.Map;

/**
 * Created by geekgy on 16/4/22.
 */
public interface AnalyticDelegate extends LifeCycleDelegate {
    /**
     * 设置账户信息
     * @param map
     */
    public void setAccoutInfo(Map<String, String> map);

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
    public void onEvent(String eventId, String eventLabel);

    /**
     * 自定义事件
     * @param eventId
     * @param eventData
     */
    public void onEvent(String eventId, Map<String, Object> eventData);

    /**
     * 设置等级
     * @param level
     */
    public void setLevel(int level);

    /**
     * 充值
     * @param iapId
     * @param cash
     * @param coin
     * @param channal
     */
    public void charge(String iapId, double cash, double coin, int channal);

    /**
     * 奖励
     * @param coin
     * @param reason
     */
    public void reward(double coin, int reason);

    /**
     * 购买
     * @param good
     * @param amount
     * @param coin
     */
    public void purchase(String good, int amount, double coin);

    /**
     * 使用
     * @param good
     * @param amount
     * @param coin
     */
    public void use(String good, int amount, double coin);

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
