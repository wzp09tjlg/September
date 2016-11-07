package com.jingxiang.september.stats;

import android.content.Context;

/**
 * Created by wu on 2016/11/4.
 * 设计来源:花生客户端
 * 结构：  Event 事件
 *        EventFinal 事件的ID
 *        EventQueue 事件队列
 *        EventConnectSession 事件统计的会话
 *        EventManager  事件的管理器
 * 使用:
 *        EventManager |--EventSize
 *                     |--EventConnectSession
 *                     |--EventQueue
 *                     |
 *                     |---addEvent(Event)
 *        在事件的出发点进行记录事件,然后判断是否到达事件的发送阈值,到达阈值就发送统计
 * 统计分类:
 *      事件统计 和 页面统计
 */
public class EventManager {
    /** Data */
    private static EventManager eventManager;

    public static int EVENT_QUEUE_SIZE = 20;//可以根据控制来设定每次传递的统计的条数

    private static String urlStats = "";
    private static Context mContext = null;

    private EventQueue eventQueue;                   //一次会话的统计信息
    private EventConnectSession eventConnectSession; //一次会话

    private int queueLen = 0;        //消息的个数
    private int activityCount = 0;   //页面的统计

    private long mStartRecordTime = 0; //统计开始的时间
    /******************************/
    private EventManager(Context context,String path){
        mContext = context;
        urlStats = path;

        eventQueue = new EventQueue();
        eventConnectSession = new EventConnectSession(mContext,urlStats);
    }

    public static EventManager getInstance(){
        if(eventManager == null){
            eventManager = new EventManager(mContext,urlStats);
        }
        return eventManager;
    }

    public static void init(Context context,String path){
        mContext = context;
        urlStats = path;
    }

    //点击事件的统计
    public void addEvent(Event event){
        if(event == null) return;
        eventQueue.addEvent(event);
        queueLen = eventQueue.size();
        if(queueLen >= EVENT_QUEUE_SIZE){
            eventConnectSession.post(eventQueue.getEventStr());
        }
    }

    private void doStartRecordEvent(){
        mStartRecordTime = System.currentTimeMillis();
        if(eventConnectSession != null){
            eventConnectSession.start();
        }
    }

    private void doStopRecordEvent(){
        if(eventConnectSession != null){
            eventConnectSession.stop();
        }
    }

    //页面的统计
    public void startEvent(){
        activityCount ++;
        if (activityCount == 1) {//除引导页之外 其他源于主页的activity
            doStartRecordEvent();
        }
    }

    public void stopEvent(){
        activityCount --;
        if(activityCount == 0){//如果当前的activity堆栈中最后一个activity被销毁掉 创建一个结束事件结束这次会话
         //do a operate hand in current eventContectSession
            Event event = new Event(EventFinal.APP_EXIT);//追加一个退出应用事件
            addEvent(event);
            doStopRecordEvent();
        }
    }
}
