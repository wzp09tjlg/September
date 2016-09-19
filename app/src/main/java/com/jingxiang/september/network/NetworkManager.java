package com.jingxiang.september.network;

import com.jingxiang.september.network.request.BaseRequest;

import java.util.Map;

/**
 * Created by wu on 2016/9/19.
 * 网络访问的工具类
 * 使用:
 * 1.设置转换类型,及回调
 * 2.如果是分页数据的请求,建议设置请求页数的参数,以便返回数据时判断是否为请求的返回
 * 3.提供了post和get的方法,度需要url,tag,param
 * 4.提供了网络请求的取消功能,当页面(activity或者fragment)被销毁时,去掉网络的请求
 * 5.暂时未做请求的数据缓存.看情况需求情况,一般在应用中是不需要做缓存的.因为请求都是需要实时的信息.
 */
public class NetworkManager<T>{
    private int mRequestPage = -1; //请求的页数
    private Class<T> mType;
    private BaseRequest.Listener<T> listener;
    private BaseRequest<T> request;

    public NetworkManager(Class<T> type, BaseRequest.Listener<T> uiDataListener){
        this.mType = type;
        this.listener = uiDataListener;
    }

    public void sendGetRequest(final String url, Object tag, Map<String,String> params){
        request = new BaseRequest<T>(url,tag,params);
        request.setmCls(mType);
        request.setListener(listener);
        request.setmRequestPage(this.mRequestPage);
        request.sendGetRequest();
    }

    public void sendPostRequest(final String url, Object tag, Map<String,String> params){
        request = new BaseRequest<T>(url,tag,params);
        request.setmCls(mType);
        request.setListener(listener);
        request.setmRequestPage(this.mRequestPage);
        request.sendPostRequest();
    }

    public void cancelRequest(Object tag){
        request.cancelRequest(tag);
    }

    public void setmRequestPage(int mRequestPage) {
        this.mRequestPage = mRequestPage;
    }
}
