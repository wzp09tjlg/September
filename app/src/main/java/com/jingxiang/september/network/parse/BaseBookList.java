package com.jingxiang.september.network.parse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wu on 2016/9/27.
 */
public class BaseBookList extends ArrayList implements Serializable {
    public String book_id;
    public String is_vip;
    public String paytype;
    public double price;
    public String intro;
    public String title;
    public String updatetime;
    public String s_bid;
    public String img;
    public String status;
    public String src;
    public String sina_id;
    public Chapter last_chapter;
    public String cate_name;
    public int cate_id;
    public int chapter_total;
    public int chapter_num;
    public double chapter_amount;
    public String author;
    public String bid;
    public String cid;
    public String createtime;
    public String ios_app;
    public String isbuy;
    public int buy_type;
    public String suite_id;
    public int is_suite;
    public String suite_name;
    public String kind;
    public int index;

    @Override
    public String toString() {
        return "{book_id:" + book_id + ";is_vip:" + is_vip + ";paytype:" + paytype
                + ";price:" + price + ";intro:" + intro + ";title:" + title
                + ";updatetime:" + updatetime + ";s_bid:" + s_bid + ";img:" + img
                + ";status:" + status + ";src:" + src + ";sina_id:" + sina_id
                + ";last_chapter:" + last_chapter + ";cate_name:" + cate_name
                + ";cate_id:" + cate_id + ";chapter_total:" + chapter_total
                + ";chapter_num:" + chapter_num + ";chapter_amount:" + chapter_amount
                + ";author:" + author + ";bid:" + bid + ";cid:" + cid
                + ";createtime:" + createtime + ";ios_app:" + ios_app + ";isbuy:" + isbuy
                + ";buy_type:" + buy_type + ";suite_id:" + suite_id + ";is_suite:" + is_suite
                + ";suite_name:" + suite_name + ";kind:" + kind + ";index:" + index + "}";
    }

    public class Chapter implements Serializable{
        public String chapter_id;
        public String title;
        public String is_vip;

        @Override
        public String toString() {
            return "{chapter_id:" + chapter_id + ";title:" + title + ";is_vip:" + is_vip + "}";
        }
    }
}
