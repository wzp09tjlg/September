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

    public GsonUtil(Class<T> type){
        if(gson == null)
            gson = new Gson();
        this.type = type;
    }

    public T parse(String json) throws Exception{
        return gson.fromJson(json,type);
    }

    public T parse(String json,Class<T> cls) throws Exception {
        return gson.fromJson(json,cls);
    }
}
