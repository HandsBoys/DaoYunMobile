package com.fzu.daoyunmobile.Utils;

import android.app.Activity;

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
}
