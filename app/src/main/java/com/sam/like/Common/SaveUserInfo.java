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
            JSONObject datejson=response.getJSONObject("data");
            SharedPreferencesUtils.setParam(context,"UserID",datejson.getString("id"));
            SharedPreferencesUtils.setParam(context,"UserName",datejson.getString("userName"));
            SharedPreferencesUtils.setParam(context,"Sign",datejson.getString("sign"));
            SharedPreferencesUtils.setParam(context,"Mobile",datejson.getString("mobile"));
            SharedPreferencesUtils.setParam(context,"EMail",datejson.getString("email"));
            SharedPreferencesUtils.setParam(context,"Logo",datejson.getString("logo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
