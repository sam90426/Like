package com.sam.like.Common;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.sam.like.R;

/**
 * Created by wuxianxin on 2017/1/18.
 */

public class ShowFragment {

    public static void showFragment(FragmentTransaction transaction,Fragment fragment1, Fragment fragment2) {

        //如果fragment2没有被添加过，就添加它替换当前的fragment1
        if (!fragment2.isAdded()) {
            transaction.add(R.id.fragment_content, fragment2)
                    //加入返回栈，这样你点击返回键的时候就会回退到fragment1了
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();

        } else { //如果已经添加过了的话就隐藏fragment1，显示fragment2
            transaction
                    // 隐藏fragment1，即当前碎片
                    .hide(fragment1)
                    // 显示已经添加过的碎片，即fragment2
                    .show(fragment2)
                    // 加入返回栈
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();
        }
    }
}
