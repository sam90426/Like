package com.sam.like.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxianxin on 2017/2/8.
 */

public class ImageUtils {
    public static List<Bitmap> ImageFromFile(String srcPath) {
        List<Bitmap> list=new ArrayList<>();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        list.add(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int xTopLeft = 0;
        int yTopLeft = 0;
        //int xTopLeft = bitmap.getWidth();
        //int yTopLeft = bitmap.getHeight();
        //int yTopLeft = (bitmap.getHeight() - xTopLeft) / 2;
        if(width>height){
            xTopLeft=(width-height)/2;
            width=height;
        }else if(width<height){
            yTopLeft=(height-width)/2;
            height=width;
        }


        Bitmap smallbitmap=Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, width, height);
        list.add(smallbitmap);
        return list;
    }
}
