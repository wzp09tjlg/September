package com.jingxiang.september.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.network.parse.GirlsBean;
import com.jingxiang.september.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/10/13.
 */
public class GirlsAdapter extends BaseAdapter {
    /** Data */
    private Context mContext;
    private List<GirlsBean.Girl> mData;
    /**********************************/
    public GirlsAdapter(Context context, List<GirlsBean.Girl> list){
        mContext = context;
        mData = list;
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
        ViewHolder viewHolder = null;
        GirlsBean.Girl bean = mData.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_girl_pic,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textPic.setText(bean.url);
        GlideUtil.setImage(mContext,viewHolder.imgPic,bean.picUrl
                ,R.drawable.icon_loading_chosen_cover
                ,R.drawable.icon_loading_chosen_cover_failed
                ,R.drawable.icon_loading_chosen_cover_no_wifi);
        return convertView;
    }

    //对外暴露的方法
    public String getItemPicUrl(int position){
        if(position < 0 || position > mData.size()) return "";
        return mData.get(position).url;
    }

    public void addData(List<GirlsBean.Girl> data){
        if(mData == null) mData = new ArrayList<>();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(GirlsBean.Girl bean){
        if(mData == null) mData = new ArrayList<>();
        mData.add(bean);
        notifyDataSetChanged();
    }

    static class ViewHolder{
        private TextView textPic;
        private ImageView imgPic;

        public ViewHolder(View view){
            textPic = (TextView)view.findViewById(R.id.text_pic);
            imgPic = (ImageView)view.findViewById(R.id.img_pic);
        }
    }
}
