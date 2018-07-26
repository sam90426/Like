package com.sam.like.View.Friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.sam.like.Common.AlertDialog;
import com.sam.like.Common.InterfaceUrl;
import com.sam.like.R;
import com.sam.like.Utils.ImageUtils;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class SendCircle extends AppCompatActivity implements View.OnClickListener {
    private EditText sendcirclecontent;
    private ImageView sendcircleback, sendimg1, sendimg2, sendimg3, sendimg4;
    private Button sendcirclesubmit;
    private CheckBox sendcircleisout;
    private ArrayList<Bitmap> mDatas;
    private ArrayList<byte[]> uploadlist;
    private ArrayList<String> pathlist;
    private String isoutstr = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_send_circle);

        sendcirclecontent = (EditText) findViewById(R.id.send_circle_content);
        sendcircleback = (ImageView) findViewById(R.id.send_circle_back);
        sendcirclesubmit = (Button) findViewById(R.id.send_circle_submit);
        sendcircleisout = (CheckBox) findViewById(R.id.send_circle_isout);
        sendimg1 = (ImageView) findViewById(R.id.send_circle_img1);
        sendimg2 = (ImageView) findViewById(R.id.send_circle_img2);
        sendimg3 = (ImageView) findViewById(R.id.send_circle_img3);
        sendimg4 = (ImageView) findViewById(R.id.send_circle_img4);

        sendimg1.setOnClickListener(this);
        sendimg2.setOnClickListener(this);
        sendimg3.setOnClickListener(this);
        sendimg4.setOnClickListener(this);


        mDatas = new ArrayList<>();
        uploadlist = new ArrayList<>();
        pathlist = new ArrayList<>();

        setimgViewBitmap(mDatas);

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
                AlertDialog.showDialog(SendCircle.this, "温馨提醒", "是否放弃编辑并离开?",  new DialogInterface.OnClickListener() {
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
                params.put("userId", (String) SharedPreferencesUtils.getParam(SendCircle.this, "UserID", ""));
                params.put("content", content);
                params.put("label", "" + 1);
                params.put("country", "");
                params.put("isout", isoutstr);
                myokhttp.upload()
                        .addMoreFile("image", "circle", uploadlist)
                        .params(MD5.getMD5(params))
                        .url(InterfaceUrl.SendCircleInterface)
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                if (ResultHelp.GetResult(SendCircle.this, response)) {

                                    Intent data=new Intent();
                                    setResult(1,data);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                T.showLong(SendCircle.this, "网络错误");
                            }
                        });
            }
        });
        //endregion

    }

    //region 图片赋值

    public void setimgViewBitmap(ArrayList<Bitmap> list) {
        switch (list.size()) {
            case 0:
                sendimg1.setImageResource(R.mipmap.ic_launcher);
                sendimg2.setVisibility(View.INVISIBLE);
                sendimg3.setVisibility(View.GONE);
                sendimg4.setVisibility(View.GONE);
                break;
            case 1:
                sendimg1.setImageBitmap(list.get(0));
                sendimg2.setVisibility(View.VISIBLE);
                sendimg2.setImageResource(R.mipmap.ic_launcher);
                sendimg3.setVisibility(View.GONE);
                sendimg4.setVisibility(View.GONE);
                break;
            case 2:
                sendimg1.setImageBitmap(list.get(0));
                sendimg2.setVisibility(View.VISIBLE);
                sendimg2.setImageBitmap(list.get(1));
                sendimg3.setVisibility(View.VISIBLE);
                sendimg3.setImageResource(R.mipmap.ic_launcher);
                sendimg4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                sendimg1.setImageBitmap(list.get(0));
                sendimg2.setVisibility(View.VISIBLE);
                sendimg2.setImageBitmap(list.get(1));
                sendimg3.setVisibility(View.VISIBLE);
                sendimg3.setImageBitmap(list.get(2));
                sendimg4.setVisibility(View.VISIBLE);
                sendimg4.setImageResource(R.mipmap.ic_launcher);
                break;
            case 4:
                sendimg1.setImageBitmap(list.get(0));
                sendimg2.setVisibility(View.VISIBLE);
                sendimg2.setImageBitmap(list.get(1));
                sendimg3.setVisibility(View.VISIBLE);
                sendimg3.setImageBitmap(list.get(2));
                sendimg4.setVisibility(View.VISIBLE);
                sendimg4.setImageBitmap(list.get(3));
                break;
        }
    }
    //endregion

    //region 图片选择
    @Override
    public void onClick(View v) {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setSelected(pathlist)
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(4)
                .build();
        GalleryFinal.openGalleryMuti(1006, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList.size() > 0) {
                    mDatas.clear();
                    uploadlist.clear();
                    pathlist.clear();

                    for (int i = 0; i < resultList.size(); i++) {
                        List<Bitmap> bitmaplist = ImageUtils.ImageFromFile(resultList.get(i).getPhotoPath());
                        mDatas.add(bitmaplist.get(1));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmaplist.get(0).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        uploadlist.add(baos.toByteArray());
                        pathlist.add(resultList.get(i).getPhotoPath());
                    }
                    setimgViewBitmap(mDatas);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }
    //endregion
}
