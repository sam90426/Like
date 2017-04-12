package com.sam.like.View.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam.like.R;

public class CircleDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_circle_detail);

        String CircleID= getIntent().getStringExtra("CircleID");

    }
}
