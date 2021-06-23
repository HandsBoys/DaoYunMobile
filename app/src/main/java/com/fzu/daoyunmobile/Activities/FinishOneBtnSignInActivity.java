package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fzu.daoyunmobile.Adapter.SignIngMemberAdapter;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 结束一键签到页面
 */
public class FinishOneBtnSignInActivity extends AppCompatActivity {

    private Button finishSignInBtn;
    private Button giveupSignInBtn;

    private TextView signInNumTV;
    private Button backBtn;
    private ListView listView;
    private List<Member> memberList = new ArrayList<>();
    private SignIngMemberAdapter signIngMemberAdapter;
    private String signinId;
    private TextView refreshTV;
    private ProgressDialog progressDialog;

    private int seconds = 20;
    private int oriSeconds = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishonebtn_sign_in);

        //签到模式
        //final String signin_mode = getIntent().getStringExtra("signin_mode");
        final String signin_mode = "signin_mode";
        //signinId = getIntent().getStringExtra("signinId");
        signinId = "555";
        initMember(0);
        signIngMemberAdapter = new SignIngMemberAdapter(FinishOneBtnSignInActivity.this, R.layout.item_member, memberList);
        listView = findViewById(R.id.signedIn_listview);
        listView.setAdapter(signIngMemberAdapter);

        //成员信息点击
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Member member = memberList.get(position);
            Toast.makeText(FinishOneBtnSignInActivity.this, member.getMemberName(), Toast.LENGTH_SHORT).show();
        });

        refreshTV = findViewById(R.id.toolbar_right_tv);
        //刷新按钮
        refreshTV.setOnClickListener(v -> {
            memberList.clear();
            //设置刷新作用
            AlertDialogUtil.showConfirmClickAlertDialog("刷新FUCK", FinishOneBtnSignInActivity.this);
            initMember(1);
            seconds = oriSeconds;//复位

        });
        startBtnDownTime(20);

        backBtn = findViewById(R.id.toolbar_left_btn);
        //返回
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });

        signInNumTV = findViewById(R.id.signIn_num_Tv);

        //TODO 签到接口待做
        giveupSignInBtn = findViewById(R.id.finish_giveup_btn);
        giveupSignInBtn.setOnClickListener(v ->
                AlertDialogUtil.showConfirmClickAlertDialog("放弃签到", FinishOneBtnSignInActivity.this));
        finishSignInBtn = findViewById(R.id.finish_signin_btn);
        finishSignInBtn.setOnClickListener(v ->
                AlertDialogUtil.showConfirmClickAlertDialog("结束签到", FinishOneBtnSignInActivity.this));
    }

    //初始化成员
    public void initMember(final int i) {
        if (i == 1) {
            progressDialog = new ProgressDialog(FinishOneBtnSignInActivity.this);
            progressDialog.setMessage("刷新中...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.dismiss();
        }
        try {
            parseJoinedList("str", 100);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void parseJoinedList(String JsonArrayData, final int m) throws JSONException {
        // JSONArray jsonArray = new JSONArray(JsonArrayData);
        int rank = 0;
        for (int i = 0; i < 5; i++) {
            rank++;
            // JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String phoneNumber = "18373371896";
            final String name = "池老二";
            final String IDNumber = "200327006";
            final String experienceScore = "2";
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            final String dateT = sdf.format(date);

            Member member;
            member = new Member(String.valueOf(rank), "", name, IDNumber, experienceScore + "经验值", dateT);

            memberList.add(member);
//                memberAdapter.notifyDataSetChanged();
        }


    }


    //倒计时
    public void startBtnDownTime(int seconds) {
        this.seconds = seconds;
        this.oriSeconds = seconds;
        refreshTV.post(downTimrRunnable);
    }


    /**
     * 倒计时线程
     */
    private Runnable downTimrRunnable = new Runnable() {
        @Override
        public void run() {
            refreshTV.setText(String.format(Locale.CHINA, "刷新%ds", seconds));
            seconds--;
            if (seconds > 0) {
                refreshTV.postDelayed(this, 1000);//递归执行
            } else {
                //TODO自动刷新
                seconds = oriSeconds;//复位
                refreshTV.postDelayed(this, 1000);//递归执行
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}