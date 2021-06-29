package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fzu.daoyunmobile.Fragments.DetailFragment;
import com.fzu.daoyunmobile.Fragments.MemberFragment;
import com.fzu.daoyunmobile.R;

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuMember;
    protected LinearLayout mMenuDetail;
    protected ImageView memberImageView;
    protected ImageView mDetailImageView;
    protected MemberFragment mMemberFragment = new MemberFragment();
    protected DetailFragment mDetailFragment = new DetailFragment();

    public static String courseName = "";
    public static String classId = "";
    public static String teacherPhone = "";
    public static String className = "";
    public static String term = "";
    public static String enterType = "Create";//Join
    public static String enableJoin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_tab);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        classId = intent.getStringExtra("classId");
        teacherPhone = intent.getStringExtra("teacherPhone");
        className = intent.getStringExtra("className");
        term = intent.getStringExtra("term");
        enterType = intent.getStringExtra("enterType");
        if (enterType.equals("Create")) {
            enableJoin = intent.getStringExtra("enableJoin");
        }
        initView();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_class_fragment, mMemberFragment)
                .add(R.id.container_class_fragment, mDetailFragment)
                .hide(mDetailFragment)
                .commit();
    }

    public void initView() {
        mMenuMember = findViewById(R.id.menu_member);
        mMenuDetail = findViewById(R.id.menu_more);
        memberImageView = findViewById(R.id.Iv_member);
        mDetailImageView = findViewById(R.id.Iv_more);
        mMenuMember.setOnClickListener(this);
        mMenuDetail.setOnClickListener(this);
        memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_pressed));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_member:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMemberFragment)
                        .hide(mDetailFragment)
                        .commit();
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_pressed));
                mDetailImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_more_normal));
                break;
            case R.id.menu_more:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mDetailFragment)
                        .hide(mMemberFragment)
                        .commit();
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_member_normal));
                mDetailImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_more_pressed));
                break;
        }
    }
}