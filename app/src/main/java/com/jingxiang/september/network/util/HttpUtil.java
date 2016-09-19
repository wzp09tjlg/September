package com.jingxiang.september.network.util;

import android.text.TextUtils;

import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.util.CommonHelper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wu on 2016/9/19.
 */
public class HttpUtil {
    public static OkHttpClient mClient = new OkHttpClient();

    static {
        mClient.setConnectTimeout(5, TimeUnit.SECONDS);
        mClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mClient.setReadTimeout(30, TimeUnit.SECONDS);
    }

    //get请求
    public static void doGetRequest(BaseRequest<?> request){
        Map<String,String> map = request.getParams();
        String sign = CommonHelper.getSign(map);
        map.put("showapi_sign",sign);
        String tempUrl = urlBuilder(request.getmUrl(),map);
        mClient.newCall(new Request.Builder().url(tempUrl).tag(request.getTag())
                .build()).enqueue(request);
    }

    //post请求  在header中设置请求的系统参数，在body中设置上传的参数(有区别哟)
    public static void doPostRequest(BaseRequest<?> request){
        Map<String,String> map = request.getParams();
        String tempSign = CommonHelper.getSign(map);
        if(!TextUtils.isEmpty(tempSign))
            map.put("showapi_sign",tempSign);
        Set<String> keys = map.keySet();
        FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();
        encodingBuilder.add("text/plain;charset=utf-8","");//这里是传content内容
        for(String key:keys){
            encodingBuilder.add(key,map.get(key));
        }
        RequestBody body = encodingBuilder.build();
        mClient.newCall(new Request.Builder().url(request.getmUrl()).tag(request.getTag())
                .post(body).build()).enqueue(request);
    }

    public static void cancelRequest(Object tag){
        mClient.cancel(tag);
    }

    private static String urlBuilder(String url, Map<String, String> params) {
        String paramsText = CommonHelper.paramstoString(params, true);
        String split = "?";
        if (url.indexOf("?") != -1) {
            split = "&";
        }
        return url + split + paramsText;
    }
}
