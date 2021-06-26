package com.fzu.daoyunmobile.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.Fragments.HpMainFragment;
import com.fzu.daoyunmobile.Fragments.MyInfoFragment;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.ActivityUtil;
import com.fzu.daoyunmobile.Utils.StatusBarUtil;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuMain;
    protected LinearLayout mMenuInfo;
    protected ImageView mainImageView;
    protected ImageView myInfoImageView;
    protected TextView mainTV;
    protected TextView myInfoTV;
    protected HpMainFragment mMainFragment = new HpMainFragment();//首页
    protected MyInfoFragment mInfoFragment = new MyInfoFragment();//我的
    public static String userName;
    public static String icon = "";

    public static String loginType;
    public static String name = null;
    public static String phoneNumber = "106666655";
    public int BUFFER_SIZE = 8192;

    //添加权限
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> permissionsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityUtil.finishAll();
        ActivityUtil.addActivity(this);

        initView();
        //将状态栏设置透明
        StatusBarUtil.transparencyBar(MainActivity.this);
        initPermissions();
        //获取管理类
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content, mMainFragment)
                .add(R.id.container_content, mInfoFragment)
                .hide(mInfoFragment)
                //事物添加  默认：显示首页  其他页面：隐藏
                //提交
                .commit();
    }


    /**
     * 请求权限
     */
    private void initPermissions() {
        permissionsList.clear();

        //判断哪些权限未授予
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }

        //请求权限
        if (!permissionsList.isEmpty()) {
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, RequestCodeConfig.getPermissionRequest());
        }
    }

    /**
     * 权限回调,
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 10004:
                break;
            default:
                break;
        }
    }

    /**
     * 初始化视图
     */
    public void initView() {
        mMenuMain = this.findViewById(R.id.hpmenu_main);
        mMenuInfo = this.findViewById(R.id.hpmenu_my);
        mainImageView = this.findViewById(R.id.hpmenu_main_im);
        myInfoImageView = this.findViewById(R.id.hpmenu_my_im);
        mainTV = this.findViewById(R.id.hpmenu_main_txv);
        myInfoTV = this.findViewById(R.id.hpmenu_my_txv);
        mMenuMain.setOnClickListener(this);
        mMenuInfo.setOnClickListener(this);
        mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_click));
        mainTV.setTextColor(Color.parseColor("#008CC9"));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hpmenu_main://首页
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMainFragment)
                        .hide(mInfoFragment)
                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_click));
                myInfoImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_me_normal));
                mainTV.setTextColor(Color.parseColor("#008CC9"));
                myInfoTV.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.hpmenu_my://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mInfoFragment)
                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_normal));
                myInfoImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_me_click));
                mainTV.setTextColor(Color.parseColor("#000000"));
                myInfoTV.setTextColor(Color.parseColor("#008CC9"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取扫描二维码的点击事件
        if (requestCode == RequestCodeConfig.getScanqrCode()) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            mMainFragment.onScanQRCode(result);
        } else if (requestCode == RequestCodeConfig.getCreateCourse()) {
            mMainFragment.onCreateCourese();
        }
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return false;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
    }
}