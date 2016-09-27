package com.jingxiang.september.network.parse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wu on 2016/9/19.
 */
public class EntertainmentVideoList implements Serializable {
    public int ret_code;
    public List<EntertainmentVideo> list;

    @Override
    public String toString() {
        return "{ret_code:" + ret_code + ";list:" + list + "}";
    }

    public class EntertainmentVideo implements Serializable{
        public String  img;
        public String  link;
        public String  num;
        public String  title;

        @Override
        public String toString() {
            return "{img:" + img +";link:" + link +";num:" + num + ";title:" + title +"}";
        }
    }
}
