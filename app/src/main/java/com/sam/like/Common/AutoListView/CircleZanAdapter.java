package com.sam.like.Common.AutoListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.like.R;
import com.sam.like.Utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wuxianxin on 2017/2/17.
 */

public class CircleZanAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private int mPos;

    public CircleZanAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.circle_item_zan_item, null);
        TextView zanuser = (TextView) convertView.findViewById(R.id.circle_item_zan_item_username);
        String str = list.get(position);
        JSONObject datajson = null;
        String zanusername = "";
        try {
            datajson = new JSONObject(str);
            zanusername = datajson.getString("UserName");
            L.i("ZANUSERNAME::::", zanusername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        zanuser.setText(zanusername);
        return convertView;
    }
}
