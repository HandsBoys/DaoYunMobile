package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OneClickSignInSettingActivity extends AppCompatActivity {
    private EditText distanceLimitET;
    private Button startOneBtn;
    private TextView experienceSettingTV;
    private LinearLayout latitudeLayout;
    private LinearLayout longitudeLayout;
    private TextView latitudeTV;
    private TextView longitudeTV;
    private String selectExperience;
    //纬度
    private double latitude;
    //经度
    private double longitude;

    public static boolean startOrNot = false;
    public static int distanceLimit = 0;
    public LocationClient mLocationClient;
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_click_sign_in_setting);

        experienceSettingTV = findViewById(R.id.signin_experience_Tv);
        final String[] experience = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        //经验值选择弹框
        experienceSettingTV.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(OneClickSignInSettingActivity.this)
                    .setTitle("选择经验值")
                    .setSingleChoiceItems(experience, 0, (dialog, which) -> selectExperience = experience[which]);
            builder.setPositiveButton("确定", (dialog, which) -> experienceSettingTV.setText(selectExperience));
            builder.show();
        });
        //设置经纬度
        latitudeLayout = findViewById(R.id.latitude_layout);
        latitudeLayout.setOnClickListener(v -> getLongitudeLatitude());
        longitudeLayout = findViewById(R.id.longitude_layout);
        longitudeLayout.setOnClickListener(v -> getLongitudeLatitude());

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.setLocOption(getBdOp());


        latitudeTV = findViewById(R.id.latitude_Tv);
        longitudeTV = findViewById(R.id.longitude_Tv);
        distanceLimitET = findViewById(R.id.distance_limit_Et);
        startOneBtn = findViewById(R.id.start_one_btn);
        startOneBtn.setOnClickListener(v -> {
            if (distanceLimitET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入签到极限距离！", OneClickSignInSettingActivity.this);
            } else if (experienceSettingTV.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请选择签到经验值！", OneClickSignInSettingActivity.this);
            } else {
                if (longitudeTV.getText().toString().equals("") && latitudeTV.getText().toString().equals("")) {
                    getLongitudeLatitude();
                }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            RequestBody requestBody = new FormBody.Builder()
//                                    .add("classId", ClassTabActivity.classId)
//                                    .add("className", ClassTabActivity.courseName)
//                                    .add("signinType", "oneButton")
//                                    .add("experience", experienceSettingTV.getText().toString())
//                                    .add("limitDistance", distanceLimitET.getText().toString())
//                                    .add("latitude", latitudeTV.getText().toString())
//                                    .add("longitude", longitudeTV.getText().toString())
//                                    .build();
//                            Request request = new Request.Builder()
//                                    .url("http://47.98.236.0:8080/setsignin")
//                                    .post(requestBody)
//                                    .build();
//                            okHttpClient.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                    final String responseBodyStr = response.body().string();
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(OneClickSignInSettingActivity.this)
//                                                    .setMessage("一键签到设置成功！")
//                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            startActivityForResult(new Intent(OneBtnSignInSettingActivity.this, FinishSignInActivity.class)
//                                                                    .putExtra("signin_mode", "one_btn_mode")
//                                                                    .putExtra("signinId", responseBodyStr), 1);
//                                                        }
//                                                    });
//                                            builder.show();
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }).start();
            }
        });
    }

    public void getLongitudeLatitude() {
        progressDialog = new ProgressDialog(OneClickSignInSettingActivity.this);
        progressDialog.setMessage("获取定位信息中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            System.out.println("FUCK U");
            System.out.println(latitude);
            System.out.println(longitude);
            longitudeTV.setText(String.valueOf(longitude));
            latitudeTV.setText(String.valueOf(latitude));
            progressDialog.dismiss();


        }
    }

    private LocationClientOption getBdOp() {
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

}