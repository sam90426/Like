package com.sam.like.View.Like;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam.like.Common.AutoListView.AutoListView;
import com.sam.like.Common.AutoListView.LikeAdapter;
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

public class LikeFragment extends Fragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    private int pageindex = 1;
    private AutoListView lstv;
    private LikeAdapter adapter;
    private List<String> list = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        //region listview设置
        lstv = (AutoListView) view.findViewById(R.id.like_listview);

        //region listview为空时显示
        TextView emptyView = new TextView(MyApplication.getInstance());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("官方文章没有数据！");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)lstv.getParent()).addView(emptyView);
        lstv.setEmptyView(emptyView);
        //endregion

        adapter = new LikeAdapter(MyApplication.getInstance(), list);
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        initData();
        //endregion

        return view;
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
        params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
        params.put("lastId", "");
        params.put("pageIndex", "" + pageindex);

        MyOkHttp myOkHttp = new MyOkHttp();
        myOkHttp.post()
                .url(InterfaceUrl.ArticleListInterface)
                .params(MD5.getMD5(params))
                .tag(MyApplication.getInstance())
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        if (ResultHelp.GetResult(MyApplication.getInstance(), response)) {
                            try {
                                JSONObject resultarray = response.getJSONObject("data");
                                JSONArray listarray = resultarray.getJSONArray("articleList");
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
                                    int total=resultarray.getJSONObject("pageInfo").getInt("pages");
                                    lstv.setResultSize(pageindex, total);
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
}
