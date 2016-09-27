package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/9/19.
 * 这里只是简单的返回一个数据
 */
public class PhoneNumBean implements Serializable {
    public int    ret_code;//未知参数
    public String prov;    //省
    public String city;    //市
    public String name;    //运营商
    public String num;     //号段
    public String provCode;//省别编码
    public String type;    //1为移动 2为电信 3为联通

    @Override
    public String toString() {
        return "{ret_code:" + ret_code + ";prov:" + prov + ";city:" + city
                + ";name:" + name +";num:" + num +";provCode:" + provCode
                + ";type:" + type +"}";
    }
}
