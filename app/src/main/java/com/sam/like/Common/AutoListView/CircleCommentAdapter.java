package com.sam.like.Common.AutoListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sam.like.Common.MyApplication;
import com.sam.like.R;
import com.sam.like.Utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wuxianxin on 2017/2/16.
 */

public class CircleCommentAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private TextView commentuser, reply, commenteduser, commentcontent;

    public CircleCommentAdapter(Context context, List<String> list) {
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

    public boolean addItem(String item) {
        list.add(item);
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.circle_commnet_item, parent, false);
            commentuser = (TextView) convertView.findViewById(R.id.commentuser);
            reply = (TextView) convertView.findViewById(R.id.reply);
            commenteduser = (TextView) convertView.findViewById(R.id.commenteduser);
            commentcontent = (TextView) convertView.findViewById(R.id.comment_content);
        }
        String str = list.get(position);
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(str);
            //评论人
            String commentuserstr = dataJson.getString("userName");
            final String commentuserIDstr = dataJson.getString("userId");
            //被评论人
            String commenteduserstr = dataJson.has("replyUserName")?dataJson.getString("replyUserName"):"";
            final String commenteduserIDstr = dataJson.has("replyUserId")?dataJson.getString("replyUserId"):"";
            //评论内容
            String commentcontentstr = dataJson.getString("comment");
            commentuser.setText(commentuserstr);

            if (commenteduserstr.isEmpty()) {
                reply.setVisibility(View.GONE);
                commenteduser.setVisibility(View.GONE);
            } else {
                commenteduser.setText(commenteduserstr);
            }
            commentcontent.setText(commentcontentstr);
            commentuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.showLong(MyApplication.getInstance(), commentuserIDstr);
                }
            });

            commenteduser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.showLong(MyApplication.getInstance(), commenteduserIDstr);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
