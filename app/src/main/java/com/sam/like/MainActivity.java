package com.sam.like;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam.like.Common.AlertDialog;
import com.sam.like.Utils.NetUtils;
import com.sam.like.View.Account.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        checknet();
    }

    public void checknet() {
        if (!NetUtils.isConnected(MainActivity.this)) {
            AlertDialog.showDialog(MainActivity.this, "温馨提醒", "无网络连接,是否打开网络设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    NetUtils.openSetting(MainActivity.this);
                }
            });
        }
        else {
            new Thread() {

                @Override
                public void run() {
                    try {
                        sleep(3000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }.start();

        }
    }
}
