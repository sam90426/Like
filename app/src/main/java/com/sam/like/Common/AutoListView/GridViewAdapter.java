package com.sam.like.Common.AutoListView;

/**
 * Created by wuxianxin on 2017/2/6.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sam.like.R;
import com.sam.like.Utils.GalleryFinalUtil.PicassoImageLoader;
import com.sam.like.Utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;

    public GridViewAdapter(Context context, List<Bitmap> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size()+1;//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.send_circlegallery_item, null);
        mImageView = (ImageView) convertView.findViewById(R.id.send_circle_gallery_itemimg);

        if (position < list.size()) {
            mImageView.setImageBitmap(list.get(position));
        }
        if(list.size()<4){
            mImageView.setBackgroundResource(R.mipmap.ic_launcher);//最后一个显示加号图片
        }
        return convertView;
    }

}