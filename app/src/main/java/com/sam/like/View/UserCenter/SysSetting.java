package com.sam.like.View.UserCenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sam.like.Common.AlertDialog;
import com.sam.like.R;
import com.sam.like.View.Account.LoginActivity;

public class SysSetting extends AppCompatActivity {
    private Button loginoutbtn;
    private ImageView syssettingbackbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sys_setting);

        loginoutbtn = (Button) findViewById(R.id.setting_loginout);
        syssettingbackbtn = (ImageView) findViewById(R.id.syssetting_back);

        //region 退出登录
        loginoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SysSetting.this, "温馨提示", "确定退出该账号?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(SysSetting.this, LoginActivity.class);
                        //intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        //endregion

        //region 返回
        syssettingbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion
    }

}
