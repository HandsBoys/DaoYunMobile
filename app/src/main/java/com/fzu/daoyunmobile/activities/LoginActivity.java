package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import com.fzu.daoyunmobile.Fragments.CodeLoginFragment;
import com.fzu.daoyunmobile.Fragments.PsdLoginFragment;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.ActivityUtil;
import com.fzu.daoyunmobile.Utils.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.tencent.connect.common.Constants;

public class LoginActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager vp;
    //注册按钮
    private Button registerBtn;
    private CodeLoginFragment code_loginFragment;
    private PsdLoginFragment psdLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //设置顶部状态栏为透明
        StatusBarUtil.transparencyBar(LoginActivity.this);

        ActivityUtil.finishAll();
        ActivityUtil.addActivity(this);

        // 设置权限
        int checkStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int checkInternetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int checkLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int checkReadStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int checkReadMountPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        int checkCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int checkPhonePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (checkStorePermission != PackageManager.PERMISSION_GRANTED
                || checkInternetPermission != PackageManager.PERMISSION_GRANTED
                || checkLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkFineLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkReadStorePermission != PackageManager.PERMISSION_GRANTED
                || checkReadMountPermission != PackageManager.PERMISSION_GRANTED
                || checkCameraPermission != PackageManager.PERMISSION_GRANTED
                || checkPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }


//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initView() {
        vp = findViewById(R.id.vp_login);
        tabLayout = findViewById(R.id.lg_tabs);
        code_loginFragment = new CodeLoginFragment();
        psdLoginFragment = new PsdLoginFragment();
        Fragment[] fragments = {code_loginFragment, psdLoginFragment};
        String[] titles = {"验证码登录", "密码登录"};
        //每项只进入一次
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tabLayout.setupWithViewPager(vp);
        tabLayout.getTabAt(0).select();//设置第一个为选中
        tabLayout.setTabTextColors(getResources().getColor(R.color.fragement_tab_comtitle), getResources().getColor(R.color.fragement_tab_selecttitle));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            code_loginFragment.onTencentCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
    }
}