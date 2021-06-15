package com.fzu.daoyunmobile.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.R;

public class GPSUtil {

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    public static boolean checkGPSIsOpen(Activity activity) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    public static void openGPSSettings(Activity activity) {
        //没有打开则弹出对话框
        activity.runOnUiThread(
                () -> {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            // 拒绝, 退出应用
                            .setNegativeButton(R.string.cancel,
                                    (dialog, which) -> activity.finish())
                            .setPositiveButton(R.string.setting,
                                    (dialog, which) -> {
                                        //跳转GPS设置界面
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        activity.startActivityForResult(intent, RequestCodeConfig.getGpsRequestCode());
                                    })
                            .setCancelable(false)
                            .show();
                }
        );
    }
}
