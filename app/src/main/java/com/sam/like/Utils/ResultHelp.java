package com.sam.like.Utils;

import android.content.Context;
import android.content.Intent;

import com.sam.like.View.Account.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxianxin on 2017/1/13.
 */

public class ResultHelp {
    public static boolean GetResult(Context context, JSONObject response){
        boolean result=true;
        try {
            int state=response.getInt("code");
            switch (state){
                case 200:
                    //region 成功

                    //endregion
                    break;
                case 400:
                    //region 请求返回失败
                    T.showLong(context,response.getString("msg"));
                    result=false;
                    //endregion
                    break;
                case 1:
                    //region 非法操作
                    T.showLong(context,response.getString("message"));
                    result=false;
                    //endregion
                    break;
                case 2:
                    //region 验证错误
                    T.showLong(context,response.getString("message"));
                    result=false;
                    //endregion
                    break;
                case 3:
                    //region 异常错误
                    T.showLong(context,response.getString("message"));
                    result=false;
                    //endregion
                    break;
                case 4:
                    //region 登录异常
                    T.showLong(context,response.getString("message"));
                    Intent intent=new Intent(context, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("iserror",true);
                    context.startActivity(intent);
                    result=false;
                    //endregion
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
