package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.ActivityUtil;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;

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
        ActivityUtil.finishAll();
        ActivityUtil.addActivity(this);

        checkGPS();

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
        mLocationClient.setLocOption(GPSUtil.getBdOp());


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
                //TODO 需要签到接口
                startActivity(new Intent(this, ResultSignInActivity.class));

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取扫描二维码的点击事件
        if (requestCode == RequestCodeConfig.getGpsRequestCode()) {
            checkGPS();
        }
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

            longitudeTV.setText(String.valueOf(longitude));
            latitudeTV.setText(String.valueOf(latitude));
            progressDialog.dismiss();

        }
    }

    //检查GPS是否启动
    private void checkGPS() {
        if (!GPSUtil.checkGPSIsOpen(this)) {
            GPSUtil.openGPSSettings(this
            );
        } else {
            System.out.println("GPS FUCK");
        }
    }
}