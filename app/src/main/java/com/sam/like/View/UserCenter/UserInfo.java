package com.sam.like.View.UserCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.like.R;
import com.sam.like.Utils.SharedPreferencesUtils;

public class UserInfo extends AppCompatActivity {

    private ImageView userinfoback, userinfomobile, userinfoemail;
    private TextView userinfomobiletext, userinfoemailtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_info);

        userinfoback = (ImageView) findViewById(R.id.userinfo_back);
        userinfomobile = (ImageView) findViewById(R.id.userinfo_mobile);
        userinfoemail = (ImageView) findViewById(R.id.auserinfo_email);
        userinfomobiletext = (TextView) findViewById(R.id.userinfo_mobiletext);
        userinfoemailtext = (TextView) findViewById(R.id.auserinfo_emailtext);

        userinfomobiletext.setText((String) SharedPreferencesUtils.getParam(UserInfo.this, "Mobile", ""));
        userinfoemailtext.setText((String) SharedPreferencesUtils.getParam(UserInfo.this, "EMail", ""));

        //region 返回
        userinfoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

        userinfomobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(UserInfo.this, SetMobile.class));
            }
        });

        userinfoemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(UserInfo.this, SetEMail.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userinfomobiletext.setText((String) SharedPreferencesUtils.getParam(UserInfo.this, "Mobile", ""));
        userinfoemailtext.setText((String) SharedPreferencesUtils.getParam(UserInfo.this, "EMail", ""));
    }
}
