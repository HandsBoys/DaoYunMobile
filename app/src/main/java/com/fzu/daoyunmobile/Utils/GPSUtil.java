package com.fzu.daoyunmobile.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.R;

public class GPSUtil {

    //纬度
    private static double latitude = 0;
    //经度
    private static double longitude = 0;

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        GPSUtil.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        GPSUtil.longitude = longitude;
    }

    private static LocationClient mLocationClient = null;
    private static boolean mLocationClinetStart = false;

    public static void getTitude(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
            //声明LocationClient类
            mLocationClient.registerLocationListener(new BDLocationListener());
            mLocationClient.setLocOption(GPSUtil.getBdOp());
        }
        if (!mLocationClinetStart) {
            mLocationClient.start();
            mLocationClinetStart = true;
        }

    }

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

    /**
     * @return 获取百度定位OP
     */
    public static LocationClientOption getBdOp() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
        return option;
    }

    public static class BDLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            mLocationClinetStart = false;
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            //获取后取消
            mLocationClient.stop();
        }
    }
}
