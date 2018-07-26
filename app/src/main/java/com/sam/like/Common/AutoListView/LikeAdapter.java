package com.sam.like.Common.AutoListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.MyApplication;
import com.sam.like.R;
import com.sam.like.Utils.L;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wuxianxin on 2017/1/19.
 */

public class LikeAdapter extends BaseAdapter {

    private ViewHolder holder;
    private List<String> list;
    private Context context;

    public LikeAdapter(Context context, List<String> list) {
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
                    R.layout.like_item, parent, false);
            convertView.setTag(holder);

            //region 控件初始化
            holder.likehits = (TextView) convertView.findViewById(R.id.like_hits);
            holder.liketitle = (TextView) convertView.findViewById(R.id.like_title);
            holder.zanbtn = (Button) convertView.findViewById(R.id.like_zan);
            //endregion

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //region 控件绑定值
        String str = list.get(position);
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(str);
            final String articleID = dataJson.getString("id");
            final int ZanCount = dataJson.getInt("zanCount");
            holder.likehits.setText("已阅读"+dataJson.getString("hits")+"次");
            holder.liketitle.setText(dataJson.getString("title"));
            if (ZanCount == 1) {
                holder.zanbtn.setText("已");
            } else {
                holder.zanbtn.setText("赞");
            }


            //region点赞
            holder.zanbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "";
                    int newcount = 0;
                    if (ZanCount == 1) {
                        url = InterfaceUrl.CancelArticleZanInterface;
                    } else {
                        url = InterfaceUrl.ArticleZanInterface;
                        newcount = 1;
                    }
                    LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                    params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
                    params.put("articleId", articleID);
                    MyOkHttp myOkHttp = new MyOkHttp();
                    final int finalNewcount = newcount;
                    myOkHttp.post().url(url).params(MD5.getMD5(params)).tag(context).enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            if (ResultHelp.GetResult(context, response))
                                try {
                                    JSONObject newresult = new JSONObject(list.get(position));
                                    list.set(position, newresult.put("zanCount", finalNewcount).toString());
                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {

                        }
                    });
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
        TextView likehits;
        TextView liketitle;
        Button zanbtn;
    }

}