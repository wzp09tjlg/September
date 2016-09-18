package com.jingxiang.september.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

/**
 * Created by wu on 2016/9/14.
 * 本地文件图片缓存
 * (文件缓存图片没有处理完,待之后再处理)
 */
public class ImageFileCacheUtil {// TODO: 2016/9/14 早上记得处理内存缓存图片及 文件系统缓存图片处理 
    //文件系统中存图片
    private DiskLruCache mDiskCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    public ImageFileCacheUtil(Activity context){
        try{
            File cacheDir = context.getCacheDir();
            mDiskCache = DiskLruCache.open(cacheDir, 1, DISK_CACHE_SIZE,DISK_CACHE_SIZE*4);
        }catch (Exception e){}
    }


    class BitmapWorkerTask extends AsyncTask<String,Void,Void> {
        private Context mContext;

        public BitmapWorkerTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            final String imageKey = String.valueOf(params[0]);
            Bitmap bitmap = getBitmapFromDiskCache(imageKey);
            if (bitmap == null && mContext != null) { // Not found in disk cache

                bitmap = ImageUtil.decodeSampledBitmapFromResource(
                        mContext.getResources(), Integer.parseInt(params[0]), 100, 100);
            }
            addBitmapToCache(imageKey, bitmap);
            return null;
        }
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (ImageCacheUtil.getBitmapFromMemCache(key) == null) {
            ;//mDiskCache.(key, bitmap);
        }

        /*if (!mDiskCache.containsKey(key)) {
            mDiskCache.put(key, bitmap);
        }*/
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        try{
            return null;//(Bitmap)mDiskCache.get(key);
        }catch (Exception e){}
        return null;
    }

    public static File getCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
                || !Environment.isExternalStorageRemovable() ?
                context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
}
