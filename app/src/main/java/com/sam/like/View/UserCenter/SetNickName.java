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

public class SetNickName extends AppCompatActivity {
    private ImageView nicknameback;
    private EditText nickname;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_nick_name);

        nickname=(EditText)findViewById(R.id.set_nickname);
        submit=(Button)findViewById(R.id.set_nickname_submit);
        nicknameback=(ImageView)findViewById(R.id.set_nickname_back);

        nickname.setText((String) SharedPreferencesUtils.getParam(SetNickName.this,"UserName",""));

        nicknameback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SetNickName.this, "温馨提示", "确定放弃编辑并离开?",  new DialogInterface.OnClickListener() {
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
                final String nicknamestr=nickname.getText().toString();
                if(!nicknamestr.isEmpty()){
                    MyOkHttp myOkHttp=new MyOkHttp();
                    LinkedHashMap<String,String> params=new LinkedHashMap<>();
                    params.put("userId",(String)SharedPreferencesUtils.getParam(SetNickName.this,"UserID",""));
                    params.put("nickName",nicknamestr);
                    myOkHttp.post()
                            .url(InterfaceUrl.SetNickNameInterface)
                            .params(MD5.getMD5(params))
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if(ResultHelp.GetResult(SetNickName.this,response)){
                                        SharedPreferencesUtils.setParam(SetNickName.this,"UserName",nicknamestr);
                                        T.showLong(SetNickName.this,"修改成功");
                                        finish();
                                    }
                                }

                                @Override
                        public void onFailure(int statusCode, String error_msg) {

                        }
                    });
                }else{
                    T.showLong(SetNickName.this,"请输入昵称");
                }
            }
        });
    }
}
