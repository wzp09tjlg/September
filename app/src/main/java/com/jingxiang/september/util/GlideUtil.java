package com.jingxiang.september.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by wu on 2016/9/13.
 * 图片处理的工具类
 */
public class GlideUtil {
   public static void setImage(Context context, ImageView imageView,String url
           ,int placeholder,int error,int mobile){
       imageView.setVisibility(View.VISIBLE);
       if(ComSharepref.get(FinalUtil.SEPTEMBER_OFF_MOBILE,FinalUtil.TURN_OFF) == FinalUtil.TURN_ON){
           imageView.setImageResource(mobile);
       }else{
           Glide.with(context).load(url)
                   .placeholder(placeholder)
                   .error(error)
                   .diskCacheStrategy(DiskCacheStrategy.ALL)
                   .dontAnimate()
                   .into(imageView);
       }
   }

    public static void setImage(Context context, ImageView imageView,int url
            ,int placeholder,int error,int mobile){
        imageView.setVisibility(View.VISIBLE);
        if(ComSharepref.get(FinalUtil.SEPTEMBER_OFF_MOBILE,FinalUtil.TURN_OFF) == FinalUtil.TURN_ON){
            imageView.setImageResource(mobile);
        }else{
            Glide.with(context).load(url)
                    .placeholder(placeholder)
                    .error(error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView);
        }
    }
}
