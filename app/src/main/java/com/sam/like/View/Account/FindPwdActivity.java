package com.sam.like.View.Account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam.like.R;

public class FindPwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_find_pwd);
    }
}
