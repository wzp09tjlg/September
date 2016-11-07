package com.jingxiang.september.stats;

import android.content.Context;

import com.jingxiang.september.network.NetworkManager;
import com.jingxiang.september.network.parse.EmptyBean;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.util.CommonHelper;
import com.jingxiang.september.util.DeviceInfoManager;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.ThreadPool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by wu on 2016/11/4.
 * 事件传递的会话,每次进行数据统计的时候 都会使用这个会话将数据传递到后台中
 */
public class EventConnectSession {
    private static final String TAG = EventConnectSession.class.getSimpleName();
    /** Data */
    private ConcurrentLinkedQueue<List<NameValuePair>> mLinkedQueue;//ConcurrentLinkedQueue 是线程安全的对列
    private PostEventRunnable postEventRunnable = null;
    private Context mContext;
    private String mStatPath;
    private String SessionId;//会话的ID唯一  ??是否有必要 这个字段.一次会话的ID??
    private boolean isBegin = false, isEnd = false;
    /******************************************/
    public EventConnectSession(Context context,String path){
        this.mContext = context;
        this.mStatPath = path;
        mLinkedQueue = new ConcurrentLinkedQueue<>();
    }

    public void start(){
        isBegin = true;
        isEnd = false;

        SessionId = CommonHelper.getUniqueString();
        doPost();
    }

    public void stop(){
        isBegin = false;
        isEnd = true;
        
        postEventRunnable = new PostEventRunnable();
        ThreadPool.execute(postEventRunnable);
    }

    public void post(String events){ //每次统计的事件 包括{前一段时间的事件集,当前的设备号,会话ID,如果登录了还有UID}
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("device_id", DeviceInfoManager.getDeviceID(mContext)));
        data.add(new BasicNameValuePair("session_id",SessionId));
        if (CommonHelper.isUserLogded()) {
            String uid = "";
            data.add(new BasicNameValuePair("uid", uid));
        }
        data.add(new BasicNameValuePair("events", events));
        mLinkedQueue.offer(data);

        doPost();
    }

    private void doPost(){
       if(mLinkedQueue == null) return;
       if(mLinkedQueue.size() < 0) return;
       postEventRunnable = new PostEventRunnable();
        ThreadPool.execute(postEventRunnable);
    }

    private class PostEventRunnable implements Runnable{
        @Override
        public void run() {
            NetworkManager<EmptyBean> manager = new NetworkManager<>(EmptyBean.class,
                    new BaseRequest.Listener<EmptyBean>() {
                        @Override
                        public void onFailure(String errMsg, BaseRequest request) {
                            LogUtil.e("do statistics failure");
                        }

                        @Override
                        public void onSuccess(EmptyBean emptyBean, BaseRequest request) {
                            LogUtil.e("do statistics success");
                        }
                    });
            Map<String, String> params = new HashMap<>();
            for (NameValuePair valuePair : mLinkedQueue.peek()) {
                params.put(valuePair.getName(), valuePair.getValue());
            }
            manager.sendPostRequest(mStatPath,TAG,params);
        }
    }
}
