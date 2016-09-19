package com.jingxiang.september.util;

import com.google.gson.Gson;

/**
 * Created by wu on 2016/9/19.
 */
public class GsonUtil<T> {
    public Gson gson = new Gson();
    public GsonUtil(){}

    public T parse(String json,Class<T> cls){
        return gson.fromJson(json,cls);
    }
}
