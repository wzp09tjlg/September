package com.jingxiang.september.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.network.parse.SportsNewsBeanList;
import com.jingxiang.september.ui.activity.WebViewActivity;
import com.jingxiang.september.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/10/17.
 */
public class SportsNewsAdapter extends BaseAdapter {
    /** Data */
    private Context mContext;
    private List<SportsNewsBeanList.SportsNewsBean> mData;
    /**********************************************/
    public SportsNewsAdapter(Context context, List<SportsNewsBeanList.SportsNewsBean> data){
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final SportsNewsBeanList.SportsNewsBean bean = mData.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sports_news,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textTitle.setText(bean.title);
        GlideUtil.setImage(mContext,holder.imgContent,bean.picUrl
                ,R.drawable.icon_loading
                ,R.drawable.icon_loading_failure
                ,R.drawable.icon_loading_no_wifi);
        holder.textTime.setText(bean.ctime);
        holder.imgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TITLE","WEBVIEW");
                bundle.putString("URL",bean.url);
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public void addData(List<SportsNewsBeanList.SportsNewsBean> data){
        if(mData == null){
            mData = new ArrayList<>();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
    }

     class ViewHolder{
         public TextView textTitle;
         public ImageView imgDetail;
         public ImageView imgContent;
         public TextView  textTime;

        public ViewHolder(View view){
            textTitle = (TextView)view.findViewById(R.id.text_title);
            imgDetail = (ImageView)view.findViewById(R.id.img_detail);
            imgContent = (ImageView)view.findViewById(R.id.img_pic);
            textTime = (TextView)view.findViewById(R.id.text_time);
        }
    }
}
