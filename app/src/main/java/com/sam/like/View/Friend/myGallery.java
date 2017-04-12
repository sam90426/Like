package com.sam.like.View.Friend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.Toast;

import com.sam.like.Common.AutoListView.CirCleGridViewAdapter;

/**
 * Created by wuxianxin on 2017/2/10.
 */

public class myGallery extends Gallery {
    boolean isFirst = false;
    boolean isLast = false;

    public myGallery(Context context) {
        super(context);
    }

    public myGallery(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    /**
     * 是否向左滑动（true - 向左滑动； false - 向右滑动）
     */
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        GalleryActivity.ImageAdapter ia = (GalleryActivity.ImageAdapter) this.getAdapter();
        int p = ia.getOwnposition();    // 获取当前图片的position
        int count = ia.getCount();        // 获取全部图片的总数count
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            if (p == 0) {
                isFirst = true;
            } else {
                isLast = false;
            }

            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            if (p == count - 1) {
                isLast = true;
            } else {
                isFirst = false;
            }

            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return true;
    }
}
