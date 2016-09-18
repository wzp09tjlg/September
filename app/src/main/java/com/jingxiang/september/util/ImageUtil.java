package com.jingxiang.september.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by wu on 2016/9/13.
 * 网上总结的一些针对imageView的处理方法，避免oom
 * <!- 创建bitmap的四种方式 ->
 * BitmapFactory.decodeFile(filePath);//创建图片可以是一个本地的路径
 * BitmapFactory.decodeByteArray(byte[],0,options);//创建图片可以使byte[]数组
 * BitmapFactory.decodeResource(getResource(),resId); //创建图片可以使资源id
 * BitmapFactory.decodeStream(inputStream,0,options);//创建图片可以使inputStream流
 * <!- bitmap 和 drawable 的区别->
 * 对比项    显示清晰度  占用内存  支持缩放  支持色相色差调整  支持旋转  支持透明色  绘制速度  支持像素操作
 * Bitmap      相同       大       是          是            是        是        慢        是
 * Drawable    相同       小       是          否            是        是        快        否
 * Drawable在内存占用和绘制速度这两个非常关键的点上胜过Bitmap
 * Bitmap是Drawable . Drawable不一定是Bitmap .就像拇指是指头,但不是所有的指头都是拇指一样.
 */
public class ImageUtil {

    //inPurgeable 为true表示创建的bitmap的数组可以在内存紧张得时候得到回收,false则不能。但是前提是在创建bitmap的时候 能再获得bitmap的原始数据
    public Bitmap decodeFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        try {
            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(//通过反射设置这个参数 表示为加载的bitmap不会把使用的内存算到Vm中，从而达到避免内存溢出的错误
                    options, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (filePath != null) {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;
    }

    //通过修改图片的加载品质来降低图片的内存
    public Bitmap ReadBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   //小于ARGB_8888  样式大小
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    // 计算图片的大小
    public int calculateBitmapSize(Bitmap candidate, Resources res, int resId){
        BitmapFactory.Options targetOptions=new BitmapFactory.Options();
        targetOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, targetOptions);
        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;
        int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
        return byteCount;
    }

    private  int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    //压缩图片到指定的尺寸
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//inSampleSize 表示将图片长宽各压缩的倍数
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //图片压缩的比率
    public static int calculateInSampleSize(BitmapFactory.Options options
            , int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
