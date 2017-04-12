package com.sam.like.Common;

import android.content.Context;

import com.sam.like.Utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sam on 2017/1/17.
 * 保存用户信息公共方法
 */

public class SaveUserInfo {
    public static void SaveUserInfo(Context context,JSONObject response,int index){

        try {
            JSONObject datejson=response.getJSONArray("result").getJSONObject(index);
            SharedPreferencesUtils.setParam(context,"UserID",datejson.getString("ID"));
            SharedPreferencesUtils.setParam(context,"UserName",datejson.getString("UserName"));
            SharedPreferencesUtils.setParam(context,"Sign",datejson.getString("Sign"));
            SharedPreferencesUtils.setParam(context,"Mobile",datejson.getString("Mobile"));
            SharedPreferencesUtils.setParam(context,"EMail",datejson.getString("EMail"));
            SharedPreferencesUtils.setParam(context,"Logo",datejson.getString("Logo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
