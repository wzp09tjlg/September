package com.jingxiang.september.network.util;

import android.text.TextUtils;

import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.util.CommonHelper;
import com.jingxiang.september.util.LogUtil;
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
        mClient.setConnectTimeout(20, TimeUnit.SECONDS);
        mClient.setWriteTimeout(20, TimeUnit.SECONDS);
        mClient.setReadTimeout(20, TimeUnit.SECONDS);
    }

    //get请求
    public static void doGetRequest(BaseRequest<?> request){
        Map<String,String> map = request.getParams();
        String tempUrl = request.getmUrl();
        if(map != null){
            String sign = CommonHelper.getSign(map);
            map.put("showapi_sign",sign);
            tempUrl = urlBuilder(request.getmUrl(),map);
        }
        LogUtil.e("" + tempUrl);
        mClient.newCall(new Request.Builder().url(tempUrl).tag(request.getTag())
                .build()).enqueue(request);
    }

    //post请求  在header中设置请求的系统参数，在body中设置上传的参数(有区别哟)
    public static void doPostRequest(BaseRequest<?> request){
        Map<String,String> map = request.getParams();
        FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();
        if(map != null){
            String tempSign = CommonHelper.getSign(map);
            if(!TextUtils.isEmpty(tempSign))
                map.put("showapi_sign",tempSign);
            Set<String> keys = map.keySet();
            for(String key:keys){
                encodingBuilder.add(key,map.get(key));
            }
        }

        LogUtil.e("" + request.getmUrl());
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
