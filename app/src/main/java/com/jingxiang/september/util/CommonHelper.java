package com.jingxiang.september.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by wu on 2016/9/19.
 */
public class CommonHelper {

    /** map转化为字符串 */
    public static String paramstoString(Map<String, String> params, boolean isEncodeValue) {
        if (params != null && params.size() > 0) {
            String paramsEncoding = "UTF-8";
            StringBuilder encodedParams = new StringBuilder();
            try {
                int index = 0;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(isEncodeValue ? URLEncoder.encode(entry.getKey(), paramsEncoding) : entry.getKey());
                    encodedParams.append('=');
                    encodedParams.append(isEncodeValue ? URLEncoder.encode(entry.getValue(), paramsEncoding) : entry.getValue());

                    index++;
                    if (index < params.size()) {
                        encodedParams.append('&');
                    }
                }
                return encodedParams.toString();
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
            }
        }
        return null;
    }

    /** Md5签名 */
    public static String getSign(Map<String, String> params) {
        Map<String, String> copyMap = new HashMap<String, String>();
        copyMap.putAll(params);
        // 添加加密key 只是针对传入的参数做md5操作
        //copyMap.put("app_key", FinalUtil.SHOW_API_APP_KEY);
        //copyMap.put("app_secret", FinalUtil.SHOW_API_APP_SECRET);

        Map<String, String> descMap = sortMapByKey(copyMap);
        String paramStr = CommonHelper.paramstoString(descMap, false);

        String md5Sign = MD5Helper.encodeToLowerCase(paramStr);
        return md5Sign;
    }

    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    private static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /** 易源添加公参 */
    public static void splitCommonParam(Context context, Map<String,String> map){
        if(map == null) map = new HashMap<>();
        map.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);//必传
        //map.put("showapi_timestamp",getCurTimeStr());//不必须传
        //map.put("showapi_sign_method","md5");        //不必须传
        //map.put("showapi_res_gzip","0");             //不必须传
    }

    private static String getCurTimeStr(){
        String tempTime = String.format("yyyyMMddHHmmss",new Date(System.currentTimeMillis()));
        return tempTime;
    }

    /** 获取json中的某个字段 */
    public static String getJsonElement(String json,String elementName){
        if(TextUtils.isEmpty(json) || TextUtils.isEmpty(elementName)) return "";
        String tempJson = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            tempJson = jsonObject.get(elementName).toString();
        }catch (Exception e){}
        return tempJson;
    }

    /** 获取json的key是数字的json转化为字符串数组 */
    public static String getKeyNumJson(String json){
        if(TextUtils.isEmpty(json)) return "";
        JsonParser jsonParser = new JsonParser();
        Set<Map.Entry<String,JsonElement>> entrySet = jsonParser.parse(json)
                .getAsJsonObject().entrySet();
        StringBuilder builder = new StringBuilder("[");
        int tempLen = entrySet.size();
        for(Map.Entry<String, JsonElement> entry : entrySet){
            builder.append(entry.getValue().toString());
            -- tempLen;
            if(tempLen>0)
                builder.append(",");
        }
        builder.append("]");
        return builder.toString();
    }

    /** 获取json中key为数字的串的第一个bean */
    public static String getKeyNumBeanJson(String json){
        if(TextUtils.isEmpty(json)) return "";
        JsonParser jsonParser = new JsonParser();
        Set<Map.Entry<String,JsonElement>> entrySet = jsonParser.parse(json)
                .getAsJsonObject().entrySet();
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, JsonElement> entry : entrySet){
            builder.append(entry.getValue().toString());
            break;
        }
        return builder.toString();
    }
}
