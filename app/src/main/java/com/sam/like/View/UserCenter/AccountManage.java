package com.sam.like.View.UserCenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.like.Common.InterfaceUrl;
import com.sam.like.R;
import com.sam.like.Utils.L;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class AccountManage extends AppCompatActivity implements View.OnClickListener {

    private TextView accountnicknametext, accountsigntext;
    private ImageView accountlogoimg, accountback;
    private ImageView accountnickname, accountlogo, accountsign, accountpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_manage);

        accountnicknametext = (TextView) findViewById(R.id.account_nicknametext);
        accountsigntext = (TextView) findViewById(R.id.account_signtext);
        accountlogoimg = (ImageView) findViewById(R.id.account_logoimg);

        accountlogo = (ImageView) findViewById(R.id.account_logo);
        accountnickname = (ImageView) findViewById(R.id.account_nickname);
        accountsign = (ImageView) findViewById(R.id.account_sign);
        accountpwd = (ImageView) findViewById(R.id.account_pwd);
        accountback = (ImageView) findViewById(R.id.account_back);

        accountnicknametext.setText((String) SharedPreferencesUtils.getParam(AccountManage.this, "UserName", ""));
        accountsigntext.setText((String) SharedPreferencesUtils.getParam(AccountManage.this, "Sign", ""));
        Picasso.with(AccountManage.this).load(InterfaceUrl.interfaceurl + SharedPreferencesUtils.getParam(AccountManage.this, "Logo", "")).into(accountlogoimg);


        accountnickname.setOnClickListener(this);
        accountsign.setOnClickListener(this);
        accountpwd.setOnClickListener(this);
        accountback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //region 修改头像
        accountlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setEnableCamera(true)
                        .setEnableEdit(true)
                        .setEnableCrop(true)
                        .setEnableRotate(true)
                        .setCropSquare(true)
                        .setEnablePreview(true)
                        .setForceCrop(true)
                        .build();
                GalleryFinal.openGallerySingle(1006, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(final int reqeustCode, List<PhotoInfo> resultList) {
                        if (!resultList.isEmpty()) {
                            final String localpath = resultList.get(0).getPhotoPath();
                            Bitmap bitmap = BitmapFactory.decodeFile(localpath);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                            MyOkHttp myOkHttp = new MyOkHttp();
                            LinkedHashMap<String, String> params = new LinkedHashMap<>();
                            params.put("userId", (String) SharedPreferencesUtils.getParam(AccountManage.this, "UserID", ""));
                            myOkHttp.upload()
                                    .addFile("image", "logo.jpg", baos.toByteArray())
                                    .params(MD5.getMD5(params))
                                    .url(InterfaceUrl.UpdateLogoInterface)
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            if (ResultHelp.GetResult(AccountManage.this, response)) {
                                                String logopath = "";
                                                try {
                                                    logopath = InterfaceUrl.interfaceurl+response.getJSONObject("data").getString("fileUrl");
                                                    L.i(localpath);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if (!logopath.isEmpty()) {
                                                    SharedPreferencesUtils.setParam(AccountManage.this, "Logo", logopath);
                                                    Picasso.with(AccountManage.this).load(localpath).into(accountlogoimg);
                                                }
                                                T.showLong(AccountManage.this, "修改成功");
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, String error_msg) {
                                            T.showLong(AccountManage.this, "网络错误");
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
            }
        });
        //endregion
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.account_nickname:
                intent.setClass(AccountManage.this, SetNickName.class);
                break;
            case R.id.account_sign:
                intent.setClass(AccountManage.this, SetSign.class);
                break;
            case R.id.account_pwd:
                intent.setClass(AccountManage.this, SetPwd.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountnicknametext.setText((String) SharedPreferencesUtils.getParam(AccountManage.this, "UserName", ""));
        accountsigntext.setText((String) SharedPreferencesUtils.getParam(AccountManage.this, "Sign", ""));
    }
}
