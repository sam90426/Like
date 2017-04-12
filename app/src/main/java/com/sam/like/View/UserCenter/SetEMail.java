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

public class SetEMail extends AppCompatActivity {
    private ImageView setemailback;
    private EditText newemail;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_email);

        setemailback=(ImageView)findViewById(R.id.set_email_back);
        newemail=(EditText)findViewById(R.id.set_newemail);
        submit=(Button)findViewById(R.id.set_email_submit);

        setemailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SetEMail.this, "温馨提示", "确定放弃编辑并离开?", new DialogInterface.OnClickListener() {
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
                final String newemailstr=newemail.getText().toString();
                if(newemailstr.isEmpty()){
                    T.showLong(SetEMail.this,"请输入正确的的邮箱地址");
                }else {
                    MyOkHttp myOkHttp=new MyOkHttp();
                    LinkedHashMap<String,String> params=new LinkedHashMap<>();
                    params.put("userID",(String) SharedPreferencesUtils.getParam(SetEMail.this,"UserID",""));
                    params.put("email",newemailstr);
                    myOkHttp.post()
                            .url(InterfaceUrl.SetEMailInterface)
                            .params(MD5.getMD5(params))
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if(ResultHelp.GetResult(SetEMail.this,response)){
                                        SharedPreferencesUtils.setParam(SetEMail.this,"EMail",newemailstr);
                                        T.showLong(SetEMail.this,"修改成功");
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    T.showLong(SetEMail.this,"网络错误");
                                }
                            });
                }
            }
        });
    }
}
