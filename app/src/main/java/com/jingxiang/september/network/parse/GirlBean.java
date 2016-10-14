package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/10/13.
 */
public class GirlBean implements Serializable {
    public String code;
    public String msg;
    public String title;
    public String description;
    public String picUrl;
    public String url;

    @Override
    public String toString() {
        return "{code:" + code + ";msg:" + msg + ";title:" + title
                + ";description:" + description +";picUrl:" + picUrl
                + ";url:" + url + "}";
    }
}
