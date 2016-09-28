package com.jingxiang.september.util;

import com.google.gson.Gson;

/**
 * Created by wu on 2016/9/19.
 * 提供两种方法,对数据进行转换
 */
public class GsonUtil<T> {
    private static Gson gson;

    public GsonUtil(){
        if(gson == null)
            gson = new Gson();
    }

    public T parse(String json,Class<T> cls) throws Exception {
        return gson.fromJson(json,cls);
    }
}
