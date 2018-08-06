package com.sam.like.View.Friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SendCircleByVideo extends AppCompatActivity {
    private EditText sendcirclecontent;
    private Button sendcirclesubmit;
    private CheckBox sendcircleisout;
    private ArrayList<Bitmap> mDatas;
    private ArrayList<byte[]> uploadlist;
    private ArrayList<String> pathlist;
    private String isoutstr = "0";
    private VideoView videoView;
    private ImageView sendcircleback;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_send_circle_by_video);

        sendcirclecontent = (EditText) findViewById(R.id.send_circle_content_by_video);
        sendcircleback = (ImageView) findViewById(R.id.send_circle_back_by_video);
        sendcirclesubmit = (Button) findViewById(R.id.send_circle_submit_by_video);
        sendcircleisout = (CheckBox) findViewById(R.id.send_circle_isout_by_video);
        videoView = (VideoView) findViewById(R.id.libPlayVideo_videoView_by_video);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (isFirst) {
                    isFirst = false;
                    Toast.makeText(SendCircleByVideo.this, "播放该视频异常", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mDatas = new ArrayList<>();
        uploadlist = new ArrayList<>();
        pathlist = new ArrayList<>();

        final String mVideoPath = getIntent().getStringExtra("path");
        //final String mVideoPath = "/storage/emulated/0/customVideo/15330200475693964165240197898200.mp4";
        final File file = new File(mVideoPath);
        if (file.exists()) {
            videoView.setVideoPath(file.getAbsolutePath());
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);

                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.setVideoPath(file.getAbsolutePath());
                    videoView.start();
                }
            });
        } else {
            Log.e("tag", "not found video " + mVideoPath);
        }

        //region 对外公开
        sendcircleisout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isoutstr = isChecked ? "1" : "0";
            }
        });
        //endregion

        //region 顶部返回
        sendcircleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.showDialog(SendCircleByVideo.this, "温馨提醒", "是否放弃编辑并离开?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
        //endregion

        //region 提交
        sendcirclesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = sendcirclecontent.getText().toString();
                MyOkHttp myokhttp = new MyOkHttp();
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("userId", (String) SharedPreferencesUtils.getParam(SendCircleByVideo.this, "UserID", ""));
                params.put("content", content);
                params.put("label", "" + 1);
                params.put("country", "");
                params.put("isout", isoutstr);

                //region 获取视频第一帧
                MediaMetadataRetriever mmr=new MediaMetadataRetriever();
                mmr.setDataSource(file.getAbsolutePath());
                Bitmap bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
                final File videoimg=new File("/storage/emulated/0/1234.jpg");//将要保存图片的路径
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(videoimg));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //endregion

                myokhttp.upload().addFile("video", file).addFile("image",videoimg)
                        .params(MD5.getMD5(params))
                        .url(InterfaceUrl.SendCircleInterface)
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                if (ResultHelp.GetResult(SendCircleByVideo.this, response)) {
                                    file.delete();
                                    Intent data = new Intent();
                                    setResult(1, data);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                T.showLong(SendCircleByVideo.this, "网络错误");
                            }
                        });
            }
        });
        //endregion
    }
}
