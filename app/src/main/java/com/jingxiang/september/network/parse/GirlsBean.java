package com.jingxiang.september.network.parse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wu on 2016/10/13.
 */
public class GirlsBean implements Serializable {
    public String code;
    public String msg;
    public List<Girl> newslist;

    @Override
    public String toString() {
        return "{code:" + code + ";msg:" + msg + ";newslist:" + newslist + "}";
    }

    public static class Girl implements Serializable{
        public String title;
        public String picUrl;
        public String description;
        public String ctime;
        public String url;

        @Override
        public String toString() {
            return "{title:" + title + ";picUrl:" + picUrl
                    + ";description:" + description + ";ctime:" + ctime
                    + ";url:" + url + "}";
        }
    }
}
