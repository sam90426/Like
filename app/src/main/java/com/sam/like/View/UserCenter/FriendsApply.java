package com.sam.like.View.UserCenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.like.Common.AutoListView.AutoListView;
import com.sam.like.Common.AutoListView.FriendsAdapter;
import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.MyApplication;
import com.sam.like.R;
import com.sam.like.Utils.L;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FriendsApply extends AppCompatActivity implements AutoListView.OnRefreshListener,AutoListView.OnLoadListener {
    private ImageView friendsback;

    private int pageindex = 1;
    private AutoListView lstv;
    private FriendsAdapter adapter;
    private List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friends_apply);

        friendsback = (ImageView) findViewById(R.id.friends_apply_back);

        //region listview设置
        lstv = (AutoListView) findViewById(R.id.friends_apply_listview);

        //region listview为空时显示
        TextView emptyView = new TextView(MyApplication.getInstance());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("好友动态没有数据！");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)lstv.getParent()).addView(emptyView);
        lstv.setEmptyView(emptyView);
        //endregion

        adapter = new FriendsAdapter(FriendsApply.this, list);
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        initData();

        //region 返回按钮
        friendsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

    }
    //region 首次加载listview
    private void initData() {
        loadData(AutoListView.REFRESH);
    }
    //endregion

    //region 获取数据
    private void loadData(final int what) {
        if (what == AutoListView.REFRESH) {
            pageindex = 1;
        }
        final List<String> result = new ArrayList<String>();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userID", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
        params.put("lastID", "");
        params.put("pageindex", "" + pageindex);

        MyOkHttp myOkHttp = new MyOkHttp();
        myOkHttp.post()
                .url(InterfaceUrl.FriendsApplyListInterface)
                .params(MD5.getMD5(params))
                .tag(MyApplication.getInstance())
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        if (ResultHelp.GetResult(MyApplication.getInstance(), response)) {
                            try {
                                JSONObject resultarray = response.getJSONArray("result").getJSONObject(0);
                                JSONArray listarray = resultarray.getJSONArray("Data");
                                L.i("DATE::::",listarray.toString());
                                if (listarray.length() > 0) {
                                    for (int i = 0; i < listarray.length(); i++) {
                                        result.add(listarray.getString(i));
                                    }
                                    switch (what) {
                                        case AutoListView.REFRESH:
                                            lstv.onRefreshComplete();
                                            list.clear();
                                            list.addAll(result);
                                            break;
                                        case AutoListView.LOAD:
                                            lstv.onLoadComplete();
                                            list.addAll(result);
                                            break;
                                    }
                                    lstv.setResultSize(pageindex, resultarray.getInt("DataPageCount"));
                                    adapter.notifyDataSetChanged();
                                    if (pageindex < resultarray.getInt("DataPageCount")) {
                                        pageindex = pageindex + 1;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        T.showLong(MyApplication.getInstance(), "网络错误");
                    }
                });
    }
    //endregion

    //region 刷新
    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }
    //endregion

    //region 加载更多
    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }
    //endregion


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}