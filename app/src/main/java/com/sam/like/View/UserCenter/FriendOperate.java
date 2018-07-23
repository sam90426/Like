package com.sam.like.View.UserCenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sam.like.Common.AlertDialog;
import com.sam.like.Common.InterfaceUrl;
import com.sam.like.R;
import com.sam.like.Utils.L;
import com.sam.like.Utils.MD5;
import com.sam.like.Utils.ResultHelp;
import com.sam.like.Utils.SharedPreferencesUtils;
import com.sam.like.Utils.T;
import com.sam.like.Utils.myokhttp.MyOkHttp;
import com.sam.like.Utils.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class FriendOperate extends AppCompatActivity implements View.OnClickListener {
    private TextView friendname, friendsex;
    private Button agreebtn, delbtn, disagreebtn, offbtn;
    private String listID;
    private String dialogmsg = "";
    private int optype = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friend_operate);

        Intent intent = getIntent();
        String friendinfo = intent.getStringExtra("FriendInfo");
        L.i("好友信息::::", friendinfo);

        friendname = (TextView) findViewById(R.id.op_friend_name);
        friendsex = (TextView) findViewById(R.id.op_friend_sex);
        agreebtn = (Button) findViewById(R.id.op_friend_agree);
        delbtn = (Button) findViewById(R.id.op_friend_del);
        disagreebtn = (Button) findViewById(R.id.op_friend_disagree);
        offbtn = (Button) findViewById(R.id.op_friend_off);

        JSONObject friendinfojson = null;
        int state = 1;
        try {
            friendinfojson = new JSONObject(friendinfo);
            friendname.setText(friendinfojson.getString("friendUserName"));
            friendsex.setText(friendinfojson.getInt("friendSex") == 1 ? "男" : "女");
            state = friendinfojson.getInt("state");
            listID = friendinfojson.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //region 关闭对话框
        offbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

        agreebtn.setOnClickListener(this);
        delbtn.setOnClickListener(this);
        disagreebtn.setOnClickListener(this);

        if (state == 1) {
            delbtn.setVisibility(View.INVISIBLE);
        } else if (state == 2) {
            agreebtn.setVisibility(View.INVISIBLE);
            disagreebtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.op_friend_agree:
                //region 同意申请
                dialogmsg = "确定同意该用户的好友申请?";
                optype = 2;
                //endregion
                break;
            case R.id.op_friend_del:
                //region 删除好友
                dialogmsg = "确定删除该好友?";
                optype = 4;
                //endregion
                break;
            case R.id.op_friend_disagree:
                //region 不同意申请
                dialogmsg = "确定拒绝该好友申请?";
                optype = 3;
                //endregion
                break;
        }
        AlertDialog.showDialog(FriendOperate.this, "温馨提示", dialogmsg,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyOkHttp myokHttp = new MyOkHttp();
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("userId", (String) SharedPreferencesUtils.getParam(FriendOperate.this, "UserID", ""));
                params.put("friendsListId", listID);
                params.put("type", ""+optype);
                myokHttp.post()
                        .url(InterfaceUrl.FriendsOperationInterface)
                        .params(MD5.getMD5(params))
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                if (ResultHelp.GetResult(FriendOperate.this, response)) {
                                    T.showLong(FriendOperate.this, "操作成功");
                                    finish();

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                T.showLong(FriendOperate.this, "网络错误");
                                finish();
                            }
                        });
            }
        });
    }
}
