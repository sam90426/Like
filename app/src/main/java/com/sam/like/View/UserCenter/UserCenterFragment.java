package com.sam.like.View.UserCenter;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.MyApplication;
import com.sam.like.Common.SaveUserInfo;
import com.sam.like.R;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.LinkedHashMap;

public class UserCenterFragment extends Fragment implements View.OnClickListener {

    private LinearLayout friendmanage, accountmanage, userinfo, syssetting, aboutus;
    private ImageView userlogo;
    private TextView username, usersign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);

        //region 控件初始化
        userlogo = (ImageView) view.findViewById(R.id.usercenter_userlogo);
        username = (TextView) view.findViewById(R.id.usercenter_username);
        usersign = (TextView) view.findViewById(R.id.usercenter_usersign);

        friendmanage = (LinearLayout) view.findViewById(R.id.usercenter_friendmanage);
        accountmanage = (LinearLayout) view.findViewById(R.id.usercenter_accountmanage);
        userinfo = (LinearLayout) view.findViewById(R.id.usercenter_userinfo);
        syssetting = (LinearLayout) view.findViewById(R.id.usercenter_syssetting);
        aboutus = (LinearLayout) view.findViewById(R.id.usercenter_aboutus);
        //endregion

        //region 控件绑定
        friendmanage.setOnClickListener(this);
        accountmanage.setOnClickListener(this);
        userinfo.setOnClickListener(this);
        syssetting.setOnClickListener(this);
        aboutus.setOnClickListener(this);

        username.setText((String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserName", ""));
        usersign.setText((String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "Sign", ""));
        userlogo.setImageURI(Uri.parse((String)SharedPreferencesUtils.getParam(MyApplication.getInstance(), "Logo", "")));
        Picasso.with(MyApplication.getInstance()).load(InterfaceUrl.interfaceurl+SharedPreferencesUtils.getParam(MyApplication.getInstance(), "Logo", "")).into(userlogo);
        //endregion

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", (String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserID", ""));
        MyOkHttp myOkHttp = new MyOkHttp();
        myOkHttp.post()
                .url(InterfaceUrl.GetUserInfointerface)
                .params(MD5.getMD5(params))
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        if (ResultHelp.GetResult(MyApplication.getInstance(), response)) {
                            SaveUserInfo.SaveUserInfo(MyApplication.getInstance(), response, 0);

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        T.showLong(MyApplication.getInstance(), "网络错误");
                    }
                });
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usercenter_friendmanage:
                startActivity(new Intent().setClass(MyApplication.getInstance(), FriendsManage.class));
                break;
            case R.id.usercenter_accountmanage:
                startActivity(new Intent().setClass(MyApplication.getInstance(), AccountManage.class));
                break;
            case R.id.usercenter_userinfo:
                startActivity(new Intent().setClass(MyApplication.getInstance(), UserInfo.class));
                break;
            case R.id.usercenter_syssetting:
                startActivity(new Intent().setClass(MyApplication.getInstance(), SysSetting.class));
                break;
            case R.id.usercenter_aboutus:
                startActivity(new Intent().setClass(MyApplication.getInstance(), AboutUs.class));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        username.setText((String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "UserName", ""));
        usersign.setText((String) SharedPreferencesUtils.getParam(MyApplication.getInstance(), "Sign", ""));
        Picasso.with(MyApplication.getInstance()).load(InterfaceUrl.interfaceurl+SharedPreferencesUtils.getParam(MyApplication.getInstance(), "Logo", "")).into(userlogo);
    }
}
