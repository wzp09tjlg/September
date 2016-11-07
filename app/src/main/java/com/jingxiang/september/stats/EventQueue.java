package com.jingxiang.september.stats;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/11/4.
 * 参考花生故事 的数据统计
 * 保存一次会话的事件集
 */
public class EventQueue {
    private List<Event> eventList;
    public EventQueue(){
        eventList = new ArrayList<>();
    }

    public void addEvent(Event event){
        if(eventList == null)
            eventList = new ArrayList<>();
        if(event != null)
            eventList.add(event);
    }

    public String getEventStr(){
        String result = "";
        synchronized (this) {
            if (eventList != null && eventList.size() > 0) {
                Gson gson = new Gson();
                result = gson.toJson(eventList);
            }
        }
        eventList.clear();//每次获取统计信息之后,就清除内存缓存
        return result;
    }

    public int size(){
        if(eventList != null)
            return eventList.size();
        return 0;
    }
}
