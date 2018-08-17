package com.sam.like.View;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.sam.like.Common.AutoListView.CircleAdapter;
import com.sam.like.R;
import com.sam.like.Utils.T;
import com.sam.like.View.Friend.AttentionFragment;
import com.sam.like.View.Index.IndexFragment;
import com.sam.like.View.Like.LikeFragment;
import com.sam.like.View.UserCenter.UserCenterFragment;

public class MyFragment extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mIndexbtn,mUserCenterbtn,mLikebtn,mFriendbtn;

    private IndexFragment mindex;
    private UserCenterFragment musercenter;
    private LikeFragment mlike;
    private AttentionFragment mfriend;

    private long firstTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_fragment);

        mIndexbtn=(ImageButton)findViewById(R.id.btn1);
        mLikebtn=(ImageButton)findViewById(R.id.btn2);
        mFriendbtn=(ImageButton)findViewById(R.id.btn3);
        mUserCenterbtn=(ImageButton)findViewById(R.id.btn4);
        mIndexbtn.setOnClickListener(this);
        mLikebtn.setOnClickListener(this);
        mFriendbtn.setOnClickListener(this);
        mUserCenterbtn.setOnClickListener(this);
        setDefaultFragment();
        mIndexbtn.setImageResource(R.drawable.indexed);
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mindex = new IndexFragment();
        transaction.replace(R.id.fragment_content, mindex);
        transaction.commit();
    }
    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        changeView(v);
        switch (v.getId())
        {
            case R.id.btn1:
                if (mindex == null)
                {
                    mindex = new IndexFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fragment_content, mindex);
                break;
            case R.id.btn4:
                if (musercenter == null)
                {
                    musercenter = new UserCenterFragment();
                }
                transaction.replace(R.id.fragment_content, musercenter);
                break;
            case R.id.btn2:
                if(mlike==null){

                    mlike=new LikeFragment();
                }
                transaction.replace(R.id.fragment_content,mlike);
                break;
            case R.id.btn3:
                if(mfriend==null){

                    mfriend=new AttentionFragment();
                }
                transaction.replace(R.id.fragment_content,mfriend);
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }
    //region 双击返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                T.showLong(this, "再按一次退出程序");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion

    private void changeView(View v){
        mIndexbtn.setImageResource(R.drawable.index);
        mLikebtn.setImageResource(R.drawable.like);
        mFriendbtn.setImageResource(R.drawable.firend);
        mUserCenterbtn.setImageResource(R.drawable.user);
        switch (v.getId())
        {
            case R.id.btn1:
                mIndexbtn.setImageResource(R.drawable.indexed);
                break;
            case R.id.btn4:
                mUserCenterbtn.setImageResource(R.drawable.usered);
                break;
            case R.id.btn2:
                mLikebtn.setImageResource(R.drawable.likeed);
                break;
            case R.id.btn3:
                mFriendbtn.setImageResource(R.drawable.friended);
                break;
        }
    }
}
