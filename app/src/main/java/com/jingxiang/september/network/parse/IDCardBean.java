package com.jingxiang.september.network.parse;

import java.io.Serializable;

/**
 * Created by wu on 2016/9/20.
 */
public class IDCardBean implements Serializable {
    public int errNum;
    public RetData retData;
    public String retMsg;
    public int ret_code;

    @Override
    public String toString() {
        return "{errNum:" + errNum + ";retData:" + retData + ";retMsg:" + retMsg + ";ret_code:" + ret_code +"}";
    }

    public class RetData implements Serializable{
        public String address;  //地址
        public String birthday; //出生日期
        public String sex;      //性别 F女 M男

        @Override
        public String toString() {
            return "{address:" + address + ";birthday:" + birthday + ";sex:" + sex +"}";
        }
    }
}
