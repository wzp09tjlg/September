package com.jingxiang.september.network.request;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jingxiang.september.network.util.HttpUtil;
import com.jingxiang.september.util.GsonUtil;
import com.jingxiang.september.util.LogUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wu on 2016/9/19.
 */
public class BaseRequest<T> implements Callback{
    public static final int ERROR_NET_DISCONNET = 0; // 无网络
    public static final int ERROR_NET_FAIL = 1; // 网络错误 404 502...
    public static final int ERROR_DATA_FAIL = 2; // 数据解析错误
    public static final int ERROR_DATA_EMPTY = 3; // 空数据
    public static final int ERROR_LOGIN_ERROR = 4; // 登录失败
    public static final int ERROR_INTERFACE_FAIL = 5; // 接口返回错误信息

    /** Data */
    private Class<T> mCls;
    private String mUrl = "";
    private int mRequestPage = -1;
    private Object mTag = null;
    private Map<String,String> mParams = null;
    private Listener<T> listener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private BaseRequest mRequest = this;

    /******************************************************/
    public BaseRequest(String url,Object tag,Map<String,String> params){
        this.mUrl = url;
        this.mTag = tag;
        this.mParams = params;
        this.mRequest = this;
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
        byte[] data = null;
        try{
            ResponseBody body = response.body();
            if(body != null)
                data = body.bytes();
        }catch (Exception e){
            LogUtil.e("Exception: data is null:" + (data == null));
        }
        final T t = parseNetworkResponse(data);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(t == null) listener.onFailure("convert error",BaseRequest.this);
                if(listener != null)
                    listener.onSuccess(t,mRequest);
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
            Object tempObject = jsonObject.get("data");//"showapi_res_body");
            if(tempObject instanceof JSONArray){
                String tempJson = tempObject.toString();
                if(tempJson.equals("[]"))
                    return parse.parse(jsonObject.toString(),mCls);
            }
            return parse.parse(tempObject.toString(),mCls);//jsonObject.get("showapi_res_body").toString(),mCls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void notifyDataChanged(T data) {
        if (listener != null) {
            listener.onSuccess(data, this);
        }
    }

    protected void notifyErrorHappened(int errorType, int errorCode, String errorMessage) {
        if (listener != null) {
            String msg = "异常";
            try{
                switch (errorType) {
                    case ERROR_NET_DISCONNET:
                        msg = "网络异常, 请检查手机设置";
                        break;

                    case ERROR_NET_FAIL:
                        msg = "网络异常, 请稍后重试";
                        break;

                    case ERROR_DATA_FAIL:
                        msg = "数据异常, 请稍后重试";
                        break;

                    case ERROR_DATA_EMPTY:
                        msg = "暂无内容";
                        break;

                    case ERROR_LOGIN_ERROR:
                        // token失效后的登录失败
                        if (errorCode == -1) {
                            // 登录取消
                            msg = "登录已失效,请重新登录";
                        } else {
                            // 登录失败
                            msg = "登录已失效,请重新登录";
                        }
                        break;

                    default:
                        if (TextUtils.isEmpty(errorMessage)) {
                            msg = "数据异常, 请稍后重试";
                        } else {
                            msg = errorMessage;
                        }
                        break;
                }
            }catch (Exception e){}

            if(listener != null){
                listener.onFailure(msg, this);
            }
        }
    }

    //回调的接口
    public interface Listener<T>{
        void onFailure(String errMsg,BaseRequest request);
        void onSuccess(T t,BaseRequest request);
    }
}
