package com.fzu.daoyunmobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fzu.daoyunmobile.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabTestActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    private FrameLayout lg_tabs_layout;
    private ArrayList<View> viewList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);
        initView();
    }

    private void initView() {

        vp = (ViewPager) findViewById(R.id.vp_login);
        lg_tabs_layout = (FrameLayout) findViewById(R.id.lg_tabs_layout);

        LayoutInflater inflater = LayoutInflater.from(TabTestActivity.this);

        //添加view
        viewList.add(inflater.inflate(R.layout.login_layout, null, false));
        viewList.add(inflater.inflate(R.layout.activity_register, null, false));

//        vp.setAdapter(new MyAdapter());


    }
}



