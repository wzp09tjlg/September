package com.jingxiang.september.stats;

import java.io.Serializable;

/**
 * Created by wu on 2016/11/4.
 * 参考花生的数据统计做的 统计事件
 */
public class Event implements Serializable {
    public String id;      //定义的事件ID
    public int position;   //事件的位置
    public long time;      //事件发生的时间
    public String info;    //事件的简介
    public String arg1;    //事件备用参数1
    public String arg2;    //事件备用参数2
    public String arg3;    //事件备用参数3

    /**************************************/
    public Event(){}
    public Event(String _id){
        this.id = _id;
    }
    public Event(String _id,int _position,long _time){
        this.id = _id;
        this.position = _position;
        this.time = _time;
    }
    public Event(String _id,int _position,long _time,String _info){
        this.id = _id;
        this.position = _position;
        this.time = _time;
        this.info = _info;
    }
    public Event(String _id,int _position,long _time,String _info,String _arg1,String _arg2,String _arg3){
        this.id = _id;
        this.position = _position;
        this.time = _time;
        this.info = _info;
        this.arg1 = _arg1;
        this.arg2 = _arg2;
        this.arg3 = _arg3;
    }
    public Event(Event event){
        this.id = event.id;
        this.position = event.position;
        this.time = event.time;
        this.info = event.info;
        this.arg1 = event.arg1;
        this.arg2 = event.arg2;
        this.arg3 = event.arg3;
    }

    @Override
    public String toString() {
        return "{id:" + id + ";position:" + position
                + ";time:" + time + ";info:" + info
                + ";agr1:" + arg1 + ";arg2:" + arg2
                + ";arg3:" + arg3 + "}";
    }
}
