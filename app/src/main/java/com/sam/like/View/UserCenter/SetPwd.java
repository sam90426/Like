package com.sam.like.View.UserCenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sam.like.Common.AlertDialog;
import com.sam.like.Common.InterfaceUrl;
import com.sam.like.R;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public class SetPwd extends AppCompatActivity {
    private ImageView setpwdback;
    private EditText oldpwd, newpwd;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_pwd);

        setpwdback = (ImageView) findViewById(R.id.set_pwd_back);
        oldpwd = (EditText) findViewById(R.id.set_pwd_old);
        newpwd = (EditText) findViewById(R.id.set_pwd_new);
        submit = (Button) findViewById(R.id.set_pwd_submit);

        setpwdback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SetPwd.this, "温馨提示", "确定放弃修改并离开?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpwdstr = oldpwd.getText().toString();
                String newpwdstr = newpwd.getText().toString();
                if (!oldpwdstr.isEmpty()) {
                    if (!newpwdstr.isEmpty()) {
                        MyOkHttp myOkHttp = new MyOkHttp();
                        LinkedHashMap<String, String> params = new LinkedHashMap<>();
                        params.put("userId", (String) SharedPreferencesUtils.getParam(SetPwd.this, "UserID", ""));
                        params.put("oldPwd", oldpwdstr);
                        params.put("newPwd", newpwdstr);
                        myOkHttp.post().url(InterfaceUrl.UpdatePwdInterface).params(MD5.getMD5(params)).tag(this).enqueue(new JsonResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                if (ResultHelp.GetResult(SetPwd.this, response)) {
                                    T.showLong(SetPwd.this,"修改成功");
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                T.showLong(SetPwd.this, "网络错误");
                            }
                        });
                    } else {
                        T.showLong(SetPwd.this, "请输入新密码");
                    }
                } else {
                    T.showLong(SetPwd.this, "请输入旧密码");
                }
            }
        });
    }
}
