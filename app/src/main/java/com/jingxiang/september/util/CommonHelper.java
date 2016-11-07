package com.jingxiang.september.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by wu on 2016/7/23.
 * 常用那个的工具类
 */
public class CommonHelper {
    /** Data */
    private static final int MAX_EVENT_SESSION_COUNT = 99999;
    private static int CURRENT_SESSION_COUNT = 0;

    /****************************************************/
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

    /**实现从dip到px的转换*/
    public static int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * (float) dps);
    }

    public static int pxToDp(Context context, float px) {
        return Math.round(px / context.getResources().getDisplayMetrics().density);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 获取手机的分辨率 */
    public static int[] getDisplayValue(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        Display display = wm.getDefaultDisplay();
        display.getSize(point);
        int[] value = new int[2];
        value[0] = point.x;
        value[1] = point.y;
        return value;
    }

    /** 手机密度 */
    public static float getDisplayDesity(Context context){
        float desity = context.getResources().getDisplayMetrics().density;
        return  desity;
    }

    /** 增加一个实例化方法 */
    public static  <T extends View> T $(View view,int id){
        return (T)view.findViewById(id);
    }

    /** url转map */
    public static Map<String,String> Url2Map(String url){
        if(TextUtils.isEmpty(url)) return null;
        int tempPosition = url.indexOf("?");
        if(tempPosition<1) return null;
        String SubUrl = url.substring(tempPosition + 1);
        Map<String, String> map = null;
        if (SubUrl != null && SubUrl.indexOf("&") > -1 && SubUrl.indexOf("=") > -1) {
            map = new HashMap<String, String>();
            String[] arrTemp = SubUrl.split("&");
            for (String str : arrTemp) {
                String[] qs = str.split("=");
                map.put(qs[0], qs[1]);
            }
        }
        return map;
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

    /** 创建带圆角的bitmap */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float corner) {
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        Bitmap roundCornerBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        PorterDuffXfermode xfermode=new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }

    /** 生成唯一字符串*/
    public static synchronized String getUniqueString() {
        if (CURRENT_SESSION_COUNT > MAX_EVENT_SESSION_COUNT) {
            CURRENT_SESSION_COUNT = 0;
        }

        String tempUnique = Long.toString(System.currentTimeMillis()) + Integer.toString(CURRENT_SESSION_COUNT);
        CURRENT_SESSION_COUNT++;
        return tempUnique;
    }

    /** 检查当前是否登录 */
    public static boolean isUserLogded(){
        return false;
    }

    private static DecimalFormat df = new DecimalFormat("#.##");
    /**格式化大小*/
    public static String formatSize(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit;
    }

    public static String formatSizeBySecond(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit + "/s";
    }

    public static String format(long size) {
        String unit = "B";
        float len = size;
        if (len > 1000) {
            len /= 1024f;
            unit = "KB";
            if (len > 1000) {
                len /= 1024f;
                unit = "MB";
                if (len > 1000) {
                    len /= 1024f;
                    unit = "GB";
                }
            }
        }
        return df.format(len) + "\n" + unit + "/s";
    }
}
