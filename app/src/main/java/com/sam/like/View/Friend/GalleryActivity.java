package com.sam.like.View.Friend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.like.Common.AlertDialog;
import com.sam.like.Common.InterfaceUrl;
import com.sam.like.Common.MyApplication;
import com.sam.like.R;
import com.sam.like.Utils.L;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.DownloadResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

public class GalleryActivity extends AppCompatActivity {
    public int position = 0;
    private JSONArray picurl;
    String imgdownpath = "";
    private ImageAdapter imgAdapter;
    private myGallery galllery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        String PicUrl = intent.getStringExtra("PicUrl");
        picurl = MyApplication.picurl;
        position = intent.getIntExtra("position", 0);

        galllery = (myGallery) findViewById(R.id.mygallery);
        imgAdapter = new ImageAdapter(GalleryActivity.this, picurl);
        galllery.setAdapter(imgAdapter);        // 设置图片ImageAdapter
        galllery.setSelection(position);        // 设置当前显示图片

        /*Animation an = AnimationUtils.loadAnimation(this, R.anim.scale);        // Gallery动画
        galllery.setAnimation(an);*/

        galllery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });

        galllery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String imgpath = "";
                imgdownpath = "";
                try {
                    imgpath = (new JSONObject(picurl.getString(position))).getString("picUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imgdownpath = imgpath;
                AlertDialog.showDialog(GalleryActivity.this, "温馨提示", "确定下载这张图片?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar c = Calendar.getInstance();
                        String imgname = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + c.get(Calendar.MILLISECOND);
                        MyOkHttp myOkHttp = new MyOkHttp();
                        myOkHttp.download()
                                .url(InterfaceUrl.interfaceurl + imgdownpath)
                                .filePath(Environment.getExternalStorageDirectory() + "/com.sam.like/circleimg/" + System.currentTimeMillis() + ".jpg")
                                .tag(this)
                                .enqueue(new DownloadResponseHandler() {
                                    @Override
                                    public void onFinish(File downloadFile) {
                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri uri = Uri.fromFile(downloadFile);
                                        intent.setData(uri);
                                        sendBroadcast(intent);
                                        T.showLong(GalleryActivity.this, "图片保存成功");
                                    }

                                    @Override
                                    public void onProgress(long currentBytes, long totalBytes) {
                                        L.i("DownImgTime:::", currentBytes + "");
                                    }

                                    @Override
                                    public void onFailure(String error_msg) {
                                        T.showLong(GalleryActivity.this, "图片保存失败");
                                    }
                                });
                    }
                });
                return false;
            }
        });
    }

    //region 适配器
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int mPos;
        private JSONArray picurllist;
        private ImageView imageview;
        private TextView counttext;


        public ImageAdapter(Context context, JSONArray picurllist) {
            mContext = context;
            this.picurllist = picurllist;
        }

        public void setOwnposition(int ownposition) {
            this.mPos = ownposition;
        }

        public int getOwnposition() {
            return mPos;
        }

        @Override
        public int getCount() {
            return picurl.length();
        }

        @Override
        public Object getItem(int position) {
            mPos = position;
            return position;
        }

        @Override
        public long getItemId(int position) {
            mPos = position;
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(GalleryActivity.this).inflate(
                        R.layout.mygallery_item, parent, false);
                mPos = position;
                imageview = (ImageView) convertView.findViewById(R.id.mygallery_img);
                imageview.setBackgroundColor(0xFF000000);
                imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);

                counttext = (TextView) convertView.findViewById(R.id.mygallery_count);
                counttext.setAlpha(0.5f);
            }
            counttext.setText(position + 1 + "/" + picurl.length());
            String picurlstr = "";
            try {
                JSONObject sss = new JSONObject(picurllist.getString(position));
                picurlstr = sss.getString("picUrl");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!picurlstr.isEmpty()) {
                Picasso.with(GalleryActivity.this).load(InterfaceUrl.interfaceurl + picurlstr).into(imageview);
            } else {
                imageview.setImageResource(R.mipmap.ic_launcher);
            }
            return convertView;
        }
    }
    //endregion
}
