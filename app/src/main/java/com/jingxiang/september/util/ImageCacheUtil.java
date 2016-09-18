package com.jingxiang.september.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.jingxiang.september.R;

/**
 * Created by wu on 2016/9/14.
 * 图片缓存处理的工具类
 */
public class ImageCacheUtil {
    //内存缓存图片
    private static LruCache<String, Bitmap> mMemoryCache = null;

    private ImageCacheUtil(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    // 存图片
    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    //取图片
    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void loadBitmap(Context context,int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.icon_cache_defualt);
            BitmapWorkerTask task = new BitmapWorkerTask(context,null);
            task.execute(imageKey);
        }
    }

    // 单开子线程,将图片加载到本地文件缓存中,以id或者是url为key值
    static class BitmapWorkerTask extends AsyncTask<String,Void,Void> {
        private Context mContext;
        private DoSetImageListener doSetImageListener;

        public BitmapWorkerTask(Context context, DoSetImageListener listener) {
            mContext = context;
            doSetImageListener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params == null || TextUtils.isEmpty(params[0])) return null;
            int id = Integer.parseInt(params[0]);
            final Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(
                    mContext.getResources(), id, 100, 100);
            addBitmapToMemoryCache(params[0], bitmap);
            if(doSetImageListener != null)
               doSetImageListener.doSetImageListener(bitmap);
            return null;
        }
    }

    public interface DoSetImageListener{
        void doSetImageListener(Bitmap bitmap);
    }
}
