package com.sam.like.View.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.R;
import com.sam.like.Utils.CodeUtils;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText mobile, password, code;
    private Button  submit;
    private ImageButton refcodeimg;
    private ImageView codeimg,backbtn;

    String codenumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        //region 控件初始化
        mobile = (EditText) findViewById(R.id.register_mobile);
        password = (EditText) findViewById(R.id.register_password);
        code = (EditText) findViewById(R.id.register_code);
        codeimg = (ImageView) findViewById(R.id.register_codeimg);
        refcodeimg = (ImageButton) findViewById(R.id.register_ref);
        submit = (Button) findViewById(R.id.register_submit);
        backbtn=(ImageView)findViewById(R.id.register_back);
        //endregion
        refreshcode();

        //region 刷新验证码图片
        refcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshcode();
            }
        });
        //endregion

        //region 提交注册
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobilestr = mobile.getText().toString();
                String passwordstr = password.getText().toString();
                String codestr = code.getText().toString();
                if (codestr.equals(codenumber)) {
                    if (mobilestr.length() == 11) {
                        if (!passwordstr.isEmpty()) {
                            MyOkHttp mMyOkhttp = new MyOkHttp();
                            //参数
                            LinkedHashMap<String, String> params = new LinkedHashMap<>();
                            params.put("mobile", mobilestr);
                            params.put("password", passwordstr);
                            mMyOkhttp
                                    .post()
                                    .url(InterfaceUrl.Registerinterface)
                                    .params(MD5.getMD5(params))
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            if (ResultHelp.GetResult(RegisterActivity.this, response)) {
                                                startActivity(new Intent().setClass(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                refreshcode();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, String error_msg) {
                                            refreshcode();
                                            T.showLong(RegisterActivity.this, "网络错误");
                                        }
                                    });
                        } else {
                            T.showLong(RegisterActivity.this, "请输入密码");
                        }
                    } else {
                        T.showLong(RegisterActivity.this, "请输入正确的手机号码");
                    }
                } else {
                    T.showLong(RegisterActivity.this, "验证码错误");
                }
            }
        });
        //endregion

        //region 返回
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //region 刷新验证码
    private void refreshcode() {
        codenumber = CodeUtils.createCode();
        codeimg.setImageBitmap(CodeUtils.getInstance().createBitmap(codenumber));
    }
    //endregion


}
