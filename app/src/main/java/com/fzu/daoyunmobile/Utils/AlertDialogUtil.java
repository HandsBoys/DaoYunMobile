package com.fzu.daoyunmobile.Utils;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * 常见的弹框工具类
 */
public class AlertDialogUtil {

    /**
     * 添加确认框
     *
     * @param msg      提示消息
     * @param activity 创建提示的框
     */
    public static void showConfirmClickAlertDialog(final String msg, Activity activity) {
        activity.runOnUiThread(
                () -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setPositiveButton("确定", null);
                    builder.show();
                });
    }

    /**
     * 添加确认框带有监听事件
     *
     * @param msg      提示消息
     * @param activity 创建提示的框
     */
    public static void showConfirmClickAlertDialogWithLister(final String msg, Activity activity, DialogInterface.OnClickListener listener) {
        activity.runOnUiThread(
                () -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setPositiveButton("确定", listener);
                    builder.show();
                });
    }

}
