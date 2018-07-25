package com.sam.like.Common.AutoListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.MyApplication;
import com.sam.like.Common.SodukuGridView;
import com.sam.like.R;
import com.sam.like.Utils.KeyBoardUtils;
import com.sam.like.Utils.L;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.ScreenUtils;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;
import com.sam.like.View.Friend.GalleryActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wuxianxin on 2017/1/16.
 */

public class CircleAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> list;
    private Context context;
    private CirCleGridViewAdapter adapter;
    private List<String> CommentList;
    private List<String> ZanList;
    private EditText commentEdit;

    public CircleAdapter(Context context, List<String> list, EditText commentedit) {
        this.list = list;
        this.context = context;
        commentEdit = commentedit;
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
                    R.layout.circle_item, parent, false);
            //region 控件初始化
            holder.usernametext = (TextView) convertView.findViewById(R.id.circleitem_username);
            holder.circlecontenttext = (TextView) convertView.findViewById(R.id.circleitem_content);
            holder.carebtn = (Button) convertView.findViewById(R.id.circleitem_care);
            holder.zanbtn = (ImageButton) convertView.findViewById(R.id.circleitem_zan);
            holder.logo = (ImageView) convertView.findViewById(R.id.circleitem_logo);
            holder.mgridview = (SodukuGridView) convertView.findViewById(R.id.circle_item_gridview);
            holder.commentlist = (ListView) convertView.findViewById(R.id.circle_comment_list);
            holder.commentbtn = (ImageButton) convertView.findViewById(R.id.circleitem_comment);
            holder.zantext = (TextView) convertView.findViewById(R.id.zantext);
            holder.zancomment = (LinearLayout) convertView.findViewById(R.id.circle_item_zancomment);
            holder.zancommnetline = convertView.findViewById(R.id.circle_item_line);
            //endregion

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //region 控件绑定值
        String str = list.get(position);
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(str);
            final String userID = dataJson.getString("userId");
            final String circleID = dataJson.getString("id");
            final int ZanCount = dataJson.getInt("zanCount");
            final String Logo = dataJson.getString("logo");

            //region 用户名
            holder.usernametext.setText(dataJson.getString("userName"));
            //endregion

            //region 动态内容
            holder.circlecontenttext.setText(dataJson.getString("content"));
            //endregion

            //region 关注按钮显示隐藏 1=已关注 2=未关注
            if (dataJson.getInt("sex") == 2) {
                holder.carebtn.setVisibility(View.VISIBLE);
            } else {
                holder.carebtn.setVisibility(View.GONE);
            }
            //endregion

            //region 点赞 1=已点赞 2=未点赞
            if (ZanCount == 1) {
                holder.zanbtn.setImageResource(R.drawable.good);
            } else {
                holder.zanbtn.setImageResource(R.drawable.ungood);
            }
            //endregion

            //region 头像
            int size = ScreenUtils.getScreenWidth(context) / 6;
            if (!Logo.isEmpty()) {
                Picasso.with(context).load(InterfaceUrl.interfaceurl + Logo).resize(size, size).error(R.mipmap.ic_launcher).into(holder.logo);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).resize(size, size).into(holder.logo);
            }
            L.i("Logo:::::", size + "");
            //endregion

            //region 关注按钮监听事件
            holder.carebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                    params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
                    params.put("friendsId", userID);
                    MyOkHttp myOkHttp = new MyOkHttp();
                    myOkHttp.post().url(InterfaceUrl.ApplyFriendInterface).params(MD5.getMD5(params)).tag(context).enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            if (ResultHelp.GetResult(context, response)) {
                                try {
                                    T.showShort(context, response.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {

                        }
                    });
                }
            });
            //endregion

            //region 点赞按钮监听事件
            holder.zanbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "";
                    int newcount = 0;
                    if (ZanCount == 1) {
                        url = InterfaceUrl.CancelCircleZanInterface;
                    } else {
                        url = InterfaceUrl.CircleZanInterface;
                        newcount = 1;
                    }
                    LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                    params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
                    params.put("circleId", circleID);
                    MyOkHttp myOkHttp = new MyOkHttp();
                    final int finalNewcount = newcount;
                    myOkHttp.post()
                            .url(url)
                            .params(MD5.getMD5(params))
                            .tag(context)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if (ResultHelp.GetResult(context, response)) {
                                        try {
                                            JSONObject newresult = new JSONObject(list.get(position));
                                            JSONArray zanarray = newresult.getJSONArray("ZanList");
                                            JSONArray newzanarray = new JSONArray();
                                            String userid = SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", "").toString();
                                            if (ZanCount == 1) {
                                                if (zanarray.length() > 0) {
                                                    for (int i = 0; i < zanarray.length(); i++) {
                                                        if (!userid.equals(zanarray.getJSONObject(i).getString("UserID"))) {
                                                            newzanarray.put(zanarray.getJSONObject(i));
                                                        }
                                                    }
                                                }
                                            } else {
                                                newzanarray = zanarray;
                                                JSONObject json = new JSONObject();
                                                json.put("UserID", userid);
                                                json.put("UserName", SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserName", ""));
                                                newzanarray.put(json);
                                            }
                                            list.set(position, newresult.put("ZanCount", finalNewcount).put("ZanList", newzanarray).toString());
                                            notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String error_msg) {

                                }
                            });
                }
            });
            //endregion

            //region 评论按钮监听事件
            holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.commentcircleID = circleID;
                    MyApplication.commentreplyuserID = "";
                    MyApplication.checkposition = position;
                    commentEdit.setVisibility(View.VISIBLE);
                    commentEdit.setHint("评论");

                }
            });
            //endregion

            //region 动态图片(最多四张)
            holder.mgridview.setVisibility(View.GONE);
            final String[] myJsonArray = dataJson.getString("picUrl").split(",");

            if (myJsonArray.length> 0) {
                List<String> picurlstr = new ArrayList<>();
                for (int i = 0; i < myJsonArray.length; i++) {
                    //JSONObject sss = new JSONObject(myJsonArray[i]);
                    //String samllurl = sss.getString("smallPicUrl");
                    //picurlstr.add(InterfaceUrl.interfaceurl + samllurl);
                    picurlstr.add(InterfaceUrl.interfaceurl + myJsonArray[i]);
                    L.i(dataJson.getString("content"), myJsonArray[i]);
                }
                holder.mgridview.setVisibility(View.VISIBLE);
                adapter = new CirCleGridViewAdapter(context, picurlstr);
                holder.mgridview.setAdapter(adapter);
                /*holder.mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //MyApplication.picurl = dataJson.getString("picUrl");
                        Intent intent = new Intent();
                        intent.setClass(context, GalleryActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("PicUrl", myJsonArray.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });*/
            }
            //endregion

            //region 点赞列表
            holder.zantext.setVisibility(View.GONE);
            holder.zancommnetline.setVisibility(View.GONE);
            holder.zancomment.setVisibility(View.GONE);
            if (!dataJson.isNull("zanList")) {
                JSONArray zanlist = dataJson.getJSONArray("zanList");
                if (zanlist.length() > 0) {
                    holder.zantext.setVisibility(View.VISIBLE);
                    holder.zancommnetline.setVisibility(View.VISIBLE);
                    holder.zancomment.setVisibility(View.VISIBLE);
                    holder.zantext.setText("");
                    holder.zantext.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.zantext.setText(addClickPart(zanlist), TextView.BufferType.SPANNABLE);
                }
            }
            //endregion

            //region 评论列表
            holder.commentlist.setVisibility(View.GONE);
            CommentList = new ArrayList<String>();
            if (!dataJson.isNull("commentList")) {
                JSONArray commenlist = dataJson.getJSONArray("commentList");
                if (commenlist.length() > 0) {
                    holder.zancomment.setVisibility(View.VISIBLE);
                    for (int i = 0; i < commenlist.length(); i++) {
                        CommentList.add(commenlist.get(i).toString());
                    }
                } else {
                    holder.zancommnetline.setVisibility(View.GONE);
                }
            }
            holder.commentlist.setVisibility(View.VISIBLE);
            holder.commentadapter = new CircleCommentAdapter(context, CommentList);
            holder.commentlist.setAdapter(holder.commentadapter);
            setListViewHeightBasedOnChildren(holder.commentlist);
            //endregion

            //region 评论回复
            final JSONObject finalDataJson = dataJson;
            holder.commentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int itemposition, long id) {
                    try {
                        JSONArray commenlist = finalDataJson.getJSONArray("commentList");
                        JSONObject commentdate = commenlist.getJSONObject(itemposition);
                        MyApplication.commentcircleID = circleID;
                        MyApplication.commentreplyuserID = commentdate.get("userID").toString();
                        MyApplication.checkposition=position;
                        commentEdit.setVisibility(View.VISIBLE);
                        commentEdit.setHint("回复" + commentdate.get("userName").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            //endregion

            //region 评论发送
            commentEdit.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == event.KEYCODE_ENTER &&
                            event.getAction() == KeyEvent.ACTION_UP) {
                        String commentstr = "";
                        commentstr = commentEdit.getText().toString();
                        if (commentstr.isEmpty()) {
                            T.showLong(MyApplication.getInstance(), "请输入评论内容");
                        } else {
                            MyOkHttp myOkHttp = new MyOkHttp();
                            LinkedHashMap<String, String> params = new LinkedHashMap<>();
                            params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
                            params.put("circleId", MyApplication.commentcircleID);
                            params.put("replyUserId", !MyApplication.commentreplyuserID.isEmpty()?MyApplication.commentreplyuserID:"0");
                            params.put("comment", commentstr);
                            myOkHttp.post()
                                    .url(InterfaceUrl.circlecommentInterface)
                                    .params(MD5.getMD5(params))
                                    .tag(MyApplication.getInstance())
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            if (ResultHelp.GetResult(MyApplication.getInstance(), response)) {
                                                commentEdit.setText("");
                                                commentEdit.setVisibility(View.GONE);

                                                KeyBoardUtils.closeKeybord(commentEdit, MyApplication.getInstance());
                                                //region 插入数据
                                                String commentdate = "";
                                                try {
                                                    int newposition = MyApplication.checkposition;
                                                    JSONObject newresult = new JSONObject(list.get(newposition));
                                                    JSONArray commentarray = newresult.getJSONArray("commentList");
                                                    JSONArray result = response.getJSONArray("result");
                                                    commentarray = commentarray.put(result.getJSONObject(0));
                                                    list.set(newposition, newresult.put("commentList", commentarray).toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                notifyDataSetChanged();
                                                T.showLong(MyApplication.getInstance(), "评论成功");
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, String error_msg) {

                                        }
                                    });
                        }
                        return true;
                    }
                    return false;
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
        private TextView usernametext;
        private TextView circlecontenttext;
        private Button carebtn;
        private ImageButton zanbtn;
        private ImageButton commentbtn;
        private ImageView logo;
        private SodukuGridView mgridview;
        private ListView commentlist;
        private CircleCommentAdapter commentadapter;
        private TextView zantext;
        private LinearLayout zancomment;
        private View zancommnetline;
    }

    //region 计算listview高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    //endregion

    //region 点赞人员列表处理
    private SpannableStringBuilder addClickPart(JSONArray list) {

        ImageSpan imgspan = new ImageSpan(MyApplication.getInstance(), R.drawable.favorites);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(imgspan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        String str = "", userid = "";
        for (int i = 0; i < list.length(); i++) {
            try {
                str += "," + list.getJSONObject(i).getString("userName");
                userid += "," + list.getJSONObject(i).getString("userId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        str = str.substring(1);
        userid = userid.substring(1);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);

        ssb.append(str);
        String[] likeUsers = str.split(",");
        String[] likeUserIDs = userid.split(",");
        if (likeUsers.length > 0) {
            for (int i = 0; i < likeUsers.length; i++) {
                //用户姓名
                final String name = likeUsers[i];
                //用户ID
                final String userID = likeUserIDs[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        T.showLong(MyApplication.getInstance(), name + "--" + userID);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色为蓝色
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }
        return ssb;
    }
    //endregion
}
