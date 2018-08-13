package com.sam.like.View.Friend;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sam.like.R;
import com.sam.like.Utils.T;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        videoView = (VideoView) findViewById(R.id.play_video);

        String path = getIntent().getStringExtra("videoSavePath");
        //String locaPath= Environment.getExternalStorageDirectory().getPath()+"/Download/F__Web_images_circleVideo_2018_8_6_15_1024253383858830.mp4";

        //Uri uri = Uri.parse(path);
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //region error
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
                    //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                    Toast.makeText(VideoActivity.this,
                            "网络服务错误",
                            Toast.LENGTH_LONG).show();
                }else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN){
                    if(extra==MediaPlayer.MEDIA_ERROR_IO){
                        //文件不存在或错误，或网络不可访问错误
                        Toast.makeText(VideoActivity.this,
                                "网络文件错误",
                                Toast.LENGTH_LONG).show();
                    } else if(extra==MediaPlayer.MEDIA_ERROR_TIMED_OUT){
                        //超时
                        Toast.makeText(VideoActivity.this,
                                "网络超时",
                                Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
        //endregion


        //设置视频路径
        //videoView.setVideoURI(uri);
        //videoView.setVideoPath("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8");
        //videoView.setVideoPath("http://192.168.1.110:8054/h5/img/1024253383858830.mp4");
        //videoView.setVideoPath(file.getAbsolutePath());
        //videoView.setVideoPath(locaPath);
        //videoView.setVideoPath("http://192.168.1.110:8054/readFile.htm?path=F:\\Web\\images\\circleVideo\\2018\\8\\6\\15\\1024253383858830.mp4");
        //videoView.setVideoPath("http://pic.ibaotu.com/00/20/08/96e888piCHck.mp4");
        //videoView.setVideoPath("http://192.168.1.110:8054/readVideo.htm?path=F:\\Web\\images\\circleVideo\\2018\\8\\6\\15\\1024253383858830.mp4");
        videoView.setVideoPath(path);
        //videoView.requestFocus();
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
