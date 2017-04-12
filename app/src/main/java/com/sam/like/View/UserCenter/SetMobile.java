package com.sam.like.View.UserCenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SetMobile extends AppCompatActivity {

    private ImageView setmobileback;
    private TextView oldmobile,newmobile;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_mobile);

        setmobileback=(ImageView)findViewById(R.id.set_mobile_back);
        oldmobile=(EditText)findViewById(R.id.set_oldmobile);
        newmobile=(EditText)findViewById(R.id.set_newmobile);
        submit=(Button)findViewById(R.id.set_mobile_submit);

        setmobileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SetMobile.this, "温馨提示", "确定放弃编辑并离开?", new DialogInterface.OnClickListener() {
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
                String oldmobilestr=oldmobile.getText().toString();
                final String newmobilestr=newmobile.getText().toString();
                if(oldmobilestr.isEmpty()||oldmobilestr.length()!=11){
                    T.showLong(SetMobile.this,"请输入正确的旧号码");
                }else{
                    if(oldmobilestr.equals((String) SharedPreferencesUtils.getParam(SetMobile.this,"Mobile",""))){
                        if(newmobilestr.isEmpty()||newmobilestr.length()!=11){
                            T.showLong(SetMobile.this,"请输入正确的新号码");
                        }else{
                            MyOkHttp myOkHttp=new MyOkHttp();
                            LinkedHashMap<String,String> params=new LinkedHashMap<>();
                            params.put("userID",(String)SharedPreferencesUtils.getParam(SetMobile.this,"UserID",""));
                            params.put("mobile",newmobilestr);
                            myOkHttp.post()
                                    .url(InterfaceUrl.SetMobileInterface)
                                    .params(MD5.getMD5(params))
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            if(ResultHelp.GetResult(SetMobile.this,response)){
                                                SharedPreferencesUtils.setParam(SetMobile.this,"Mobile",newmobilestr);
                                                T.showLong(SetMobile.this,"修改成功");
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, String error_msg) {
                                            T.showLong(SetMobile.this,"网络错误");
                                        }
                                    });
                        }
                    }else {
                        T.showLong(SetMobile.this,"请输入正确的旧号码");
                    }
                }
            }
        });
    }
}
