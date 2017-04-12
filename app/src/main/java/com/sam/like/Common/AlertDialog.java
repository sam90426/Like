package com.sam.like.Common;

import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog {


    public static void showDialog(Context context, String title, String msg, DialogInterface.OnClickListener yes) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", yes);
        builder.show();
    }

    public static void showoneDialog(Context context, String title, String msg, DialogInterface.OnClickListener yes) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", yes);
        builder.show();
    }
}



