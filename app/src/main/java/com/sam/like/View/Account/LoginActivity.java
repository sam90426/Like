package com.sam.like.View.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.SaveUserInfo;
import com.sam.like.R;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;
import com.sam.like.View.MyFragment;

import org.json.JSONObject;

import java.util.LinkedHashMap;


public class LoginActivity extends AppCompatActivity {
    private EditText loginname, password;
    private Button loginbtn;
    private TextView registerbtn, findbtn;
    private String loginnamestr = "", passwordstr = "";
    private long firstTime = 0;
    private boolean iserror = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        Intent getintent = getIntent();
        iserror = getintent.getBooleanExtra("iserror", false);


        //region 绑定控件
        loginname = (EditText) findViewById(R.id.Login_loginname);
        password = (EditText) findViewById(R.id.Login_password);
        loginbtn = (Button) findViewById(R.id.Login_submit);
        registerbtn = (TextView) findViewById(R.id.Login_register);
        findbtn = (TextView) findViewById(R.id.Login_findpwd);
        //endregion

        //region 登录
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginnamestr = loginname.getText().toString();
                passwordstr = password.getText().toString();
                if (loginnamestr.isEmpty()) {
                    T.showLong(LoginActivity.this, "请输入正确的账号");
                } else if (passwordstr.isEmpty() || passwordstr.length() < 6) {
                    T.showLong(LoginActivity.this, "请输入正确的密码");
                } else {
                    //参数
                    LinkedHashMap<String, String> params = new LinkedHashMap<>();
                    params.put("mobile", loginnamestr);
                    params.put("password", passwordstr);


                    //网络请求
                    MyOkHttp mMyOkhttp = new MyOkHttp();
                    mMyOkhttp.post()
                            .url(InterfaceUrl.logininterface)
                            .params(MD5.getMD5(params))
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("请求成功返回消息", "doPost onSuccess:" + response);
                                    if (ResultHelp.GetResult(LoginActivity.this, response)) {
                                        SaveUserInfo.SaveUserInfo(LoginActivity.this, response,0);

                                        if (!iserror)
                                            startActivity(new Intent().setClass(LoginActivity.this, MyFragment.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    Log.d("请求失败返回消息", "doPost onFailure:" + error_msg);
                                    T.showLong(LoginActivity.this, "网络错误");
                                }
                            });
                }
            }
        });
        //endregion

        //region 注册
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(LoginActivity.this, RegisterActivity.class));
            }
        });
        //endregion

        //region 找回密码
        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(LoginActivity.this, FindPwdActivity.class));
            }
        });
        //endregion

    }

    //region 双击返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                T.showLong(this, "再按一次退出程序");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion

}
