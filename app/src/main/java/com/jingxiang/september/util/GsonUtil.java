package com.jingxiang.september.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by wu on 2016/9/19.
 * 提供两种方法,对数据进行转换
 */
public class GsonUtil<T> {
    private Type type;
    private static Gson gson;

    public GsonUtil(){
        if(gson == null)
            gson = new Gson();
    }

    public GsonUtil(Class<T> type){
        if(gson == null)
            gson = new Gson();
        this.type = type;
    }

    public T parse(String json,Class<T> cls) throws Exception {
        return gson.fromJson(json,cls);
    }

    public T parse(String json){
        return gson.fromJson(json,type);
    }
}
