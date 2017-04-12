package com.sam.like.View.UserCenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam.like.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about_us);
    }

}
