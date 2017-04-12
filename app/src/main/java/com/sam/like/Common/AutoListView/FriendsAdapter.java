package com.sam.like.Common.AutoListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sam.like.Common.MyApplication;
import com.sam.like.R;
import com.sam.like.Utils.T;
import com.sam.like.View.UserCenter.FriendOperate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wuxianxin on 2017/1/20.
 */

public class FriendsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> list;
    private Context context;

    public FriendsAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.friends_list_item, parent, false);
            convertView.setTag(holder);

            //region 控件初始化
            holder.friendnametext = (TextView) convertView.findViewById(R.id.friend_name);
            holder.friendsextext = (TextView) convertView.findViewById(R.id.friends_sex);
            holder.operationbtn = (Button) convertView.findViewById(R.id.friends_operation);
            //endregion

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //region 控件绑定值
        final String str = list.get(position);
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(str);
            holder.friendnametext.setText(dataJson.getString("FriendUserName"));
            int sex=dataJson.getInt("FriendSex");
            if (sex == 1) {
                holder.friendsextext.setText("男");
            } else if(sex == 2){
                holder.friendsextext.setText("女");
            }else {
                holder.friendsextext.setText("保密");
            }

            //region 操作
            holder.operationbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent().setClass(MyApplication.getInstance(), FriendOperate.class);
                    intent.putExtra("FriendInfo", str);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getInstance().startActivity(intent);
                }
            });
            //endregion

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        return convertView;
    }

    private static class ViewHolder {
        TextView friendnametext;
        TextView friendsextext;
        Button operationbtn;
    }

}

