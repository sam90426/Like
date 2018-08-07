package com.sam.like.View.Friend;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sam.like.R;
import com.sam.like.Utils.T;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        videoView = (VideoView) findViewById(R.id.play_video);

        String path = getIntent().getStringExtra("videoSavePath");

        Uri uri = Uri.parse(path);
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);
        //videoView.setVideoPath(path);
        videoView.requestFocus();

        //开始播放视频
        videoView.start();

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            T.showLong(VideoActivity.this,"播放完成");
        }
    }
}
