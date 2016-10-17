package com.jingxiang.september.network.parse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wu on 2016/10/17.
 */
public class SportsNewsBeanList implements Serializable {
    public String  code;
    public String  msg;
    public List<SportsNewsBean> newslist;

    @Override
    public String toString() {
        return "{code:" + code + ";msg:" + msg + ";newslist:" + newslist + "}";
    }

    public class SportsNewsBean implements Serializable{
        public String title;
        public String picUrl;
        public String description;
        public String ctime;
        public String url;

        @Override
        public String toString() {
            return "{title:" + title + ";picUrl:" + picUrl
                    + ";description:" + description
                    + ";ctime:" + ctime + ";url:" + url + "}";
        }
    }
}
