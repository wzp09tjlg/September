package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/9/28.
 */
public class EmptyBean implements Serializable {
    public int     res_code;
    public String  res_msg;

    @Override
    public String toString() {
        return "{res_code:" + res_code +";res_msg:" + res_msg + "}";
    }
}
