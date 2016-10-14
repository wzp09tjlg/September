package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/9/27.
 */
public class BaseBook implements Serializable {
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

    public class Chapter implements Serializable{
        public String chapter_id;
        public String title;
        public String is_vip;
    }
}
