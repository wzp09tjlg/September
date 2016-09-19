package com.jingxiang.september.network.request;

import android.os.Handler;
import android.os.Looper;

import com.jingxiang.september.network.util.HttpUtil;
import com.jingxiang.september.util.GsonUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wu on 2016/9/19.
 */
public class BaseRequest<T> implements Callback{
    private Class<T> mCls;
    private String mUrl = "";
    private int mRequestPage = -1;
    private Object mTag = null;
    private Map<String,String> mParams = null;
    private Listener<T> listener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private BaseRequest mRequest = null;

    public BaseRequest(String url,Object tag,Map<String,String> params){
        this.mUrl = url;
        this.mTag = tag;
        this.mParams = params;
    }

    public void sendGetRequest(){
        HttpUtil.doGetRequest(this);
    }

    public void sendPostRequest(){
        HttpUtil.doPostRequest(this);
    }

    public void cancelRequest(Object tag){
      HttpUtil.cancelRequest(tag);
    }

    @Override
    public void onFailure(final Request request,final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null)
                   listener.onFailure(e.getMessage(),mRequest);
        }
        });
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                byte[] data = null;
                try{
                    data = response.body().bytes();
                }catch (Exception e){
                    if(listener != null)
                        listener.onFailure("数据转换错误",null);
                }
                if(listener != null)
                    listener.onSuccess(parseNetworkResponse(data),mRequest);
            }
        });
    }

    public String getmUrl() {
        return mUrl;
    }

    public Object getTag(){
        return mTag;
    }

    public Map<String,String> getParams(){
        return mParams;
    }

    public void setmCls(Class<T> cls){
        this.mCls = cls;
    }

    public int getmRequestPage() {
        return mRequestPage;
    }

    public void setmRequestPage(int mRequestPage) {
        this.mRequestPage = mRequestPage;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }

    protected T parseNetworkResponse(byte[] data){
        if(data == null) return null;
        try {
            JSONObject jsonObject = new JSONObject(new String(data));
            GsonUtil<T> parse = new GsonUtil();
            return parse.parse(jsonObject.get("showapi_res_body").toString(),mCls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //回调的接口
    public interface Listener<T>{
        void onFailure(String errMsg,BaseRequest request);
        void onSuccess(T t,BaseRequest request);
    }

    public interface Method{
       int GET = 0;
       int POST = 1;
    }
}
