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

public class SetSign extends AppCompatActivity {
    private ImageView signback;
    private Button submit;
    private EditText setsign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_sign);

        signback = (ImageView) findViewById(R.id.set_sign_back);
        submit = (Button) findViewById(R.id.set_sign_submit);
        setsign = (EditText) findViewById(R.id.set_sign);

        setsign.setText((String) SharedPreferencesUtils.getParam(SetSign.this, "Sign", ""));
        signback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SetSign.this, "温馨提示", "确定放弃编辑并离开?", new DialogInterface.OnClickListener() {
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
                final String signstr = setsign.getText().toString();
                if (!signstr.isEmpty()) {
                    MyOkHttp myOkHttp = new MyOkHttp();
                    LinkedHashMap<String, String> params = new LinkedHashMap<>();
                    params.put("userID", (String) SharedPreferencesUtils.getParam(SetSign.this, "UserID", ""));
                    params.put("content", signstr);
                    myOkHttp.post()
                            .url(InterfaceUrl.SetSignInterface)
                            .params(MD5.getMD5(params))
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if (ResultHelp.GetResult(SetSign.this, response)) {
                                        SharedPreferencesUtils.setParam(SetSign.this, "Sign", signstr);
                                        T.showLong(SetSign.this, "修改成功");
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    T.showLong(SetSign.this, "网络错误");
                                }
                            });
                } else {
                    T.showLong(SetSign.this, "请输入个性签名");
                }
            }
        });
    }
}
