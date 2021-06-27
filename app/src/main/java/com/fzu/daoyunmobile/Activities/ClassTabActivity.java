package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fzu.daoyunmobile.Fragments.DetailFragment;
import com.fzu.daoyunmobile.Fragments.MemberFragment;
import com.fzu.daoyunmobile.R;

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuMember;
    protected LinearLayout mMenuMore;
    protected ImageView memberImageView;
    protected ImageView activityImageView;
    protected ImageView moreImageView;
    protected MemberFragment mMemberFragment = new MemberFragment();
    protected DetailFragment mMoreFragment = new DetailFragment();

    public static String courseName = "";
    public static String classId = "";
    public static String teacherPhone = "";
    public static String className = "";
    public static String term = "";
    public static String studentID = "";
    public static String enterType = "Create";//Join

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_tab);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        classId = intent.getStringExtra("classId");
        //输出班课ID
        Log.i("classTabID", classId);
        // enterType = intent.getStringExtra("enterType");
        teacherPhone = intent.getStringExtra("teacherPhone");
        className = intent.getStringExtra("className");
        term = intent.getStringExtra("term");
        initView();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_class_fragment, mMemberFragment)
                .add(R.id.container_class_fragment, mMoreFragment)
                //.hide(mMemberFragment)
                .hide(mMoreFragment)
                //事物添加  默认：显示首页  其他页面：隐藏
                //提交
                .commit();
    }

    public void initView() {
        mMenuMember = findViewById(R.id.menu_member);
        mMenuMore = findViewById(R.id.menu_more);

        memberImageView = findViewById(R.id.Iv_member);
        moreImageView = findViewById(R.id.Iv_more);

        mMenuMember.setOnClickListener(this);
        mMenuMore.setOnClickListener(this);
        memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_pressed));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menu_member:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMemberFragment)
                        .hide(mMoreFragment)
                        .commit();
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_pressed));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_more_normal));
                break;

            case R.id.menu_more:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMoreFragment)
                        .hide(mMemberFragment)
                        .commit();
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_normal));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_more_pressed));
                break;
        }
    }
}