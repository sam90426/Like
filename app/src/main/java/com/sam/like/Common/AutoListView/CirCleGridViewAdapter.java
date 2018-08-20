package com.sam.like.Common.AutoListView;

/**
 * Created by wuxianxin on 2017/2/6.
 */

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sam.like.R;
import com.sam.like.Utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CirCleGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private int mPos;

    public CirCleGridViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getOwnposition() {
        return mPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mPos=position;
        convertView = layoutInflater.inflate(R.layout.send_circlegallery_item, null);
        mImageView = (ImageView) convertView.findViewById(R.id.send_circle_gallery_itemimg);
        int size = ScreenUtils.getScreenWidth(context)/4;
        //mImageView.setBackgroundResource(list.get(position));
//        Glide.with(context).load(Uri.parse(list.get(position))).override(size, size).into(mImageView);
        Picasso.with(context).load(Uri.parse(list.get(position))).resize(size, size).into(mImageView);
        //mImageView.setImageURI(Uri.parse(list.get(position)));

        return convertView;
    }

}