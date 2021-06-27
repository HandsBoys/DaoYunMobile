package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Adapter.SignIngMemberAdapter;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FinishLimitSignInActivity extends AppCompatActivity {

    private Button finishSignInBtn;

    private TextView signInNumTV;
    private TextView startTimeTV;
    private TextView endTimeTV;

    private Button backBtn;
    private ListView listView;
    private List<Member> memberList = new ArrayList<>();
    private SignIngMemberAdapter signIngMemberAdapter;
    private String signId;
    private TextView refreshTV;
    private ProgressDialog progressDialog;


    private int seconds = 20;
    private int oriSeconds = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_limit_time_sign_in);
        Intent intent = getIntent();
        String signMode = intent.getStringExtra("startMode");
        //签到模式
        signId = getIntent().getStringExtra("signId");
        String startTime = "", endTime = "";
        //如果是创建的话有俩个参数
        if (signMode.equals("createSign")) {
            startTime = intent.getStringExtra("startTime");
            endTime = intent.getStringExtra("endTime");
        } else {
            //TODO 截字符串
            startTime = intent.getStringExtra("startTime");
            startTime = startTime.substring(0, startTime.length() - 10).replace('T', ' ');
            endTime = intent.getStringExtra("endTime");
            endTime = endTime.substring(0, endTime.length() - 10).replace('T', ' ');
        }

        startTimeTV = findViewById(R.id.signIn_start_time_Tv);
        startTimeTV.setText("开始时间:" + startTime);
        endTimeTV = findViewById(R.id.signIn_end_time_Tv);
        endTimeTV.setText("结束时间:" + endTime);
        listView = findViewById(R.id.signedIn_listview);

        initMember(false);


        //成员信息点击
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Member member = memberList.get(position);
            Toast.makeText(FinishLimitSignInActivity.this, member.getMemberName(), Toast.LENGTH_SHORT).show();
        });

        refreshTV = findViewById(R.id.toolbar_right_tv);
        //刷新按钮
        refreshTV.setOnClickListener(v -> {
            memberList.clear();
            //设置刷新作用
            initMember(true);
            seconds = oriSeconds;//复位

        });
        startBtnDownTime(20);

        //设置签到人数
        signInNumTV = findViewById(R.id.signIn_num_Tv);

        backBtn = findViewById(R.id.toolbar_left_btn);
        //返回
        backBtn.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            setResult(RESULT_OK, intent2);
            finish();
        });

        signInNumTV = findViewById(R.id.signIn_num_Tv);

        finishSignInBtn = findViewById(R.id.finish_sign_in_btn);
        finishSignInBtn.setOnClickListener(v -> {
            //设置签到结束
            OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STOP_SIGN_IN) + signId, new JSONObject(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("FUCK CreateCourse" + e.getMessage());
                    AlertDialogUtil.showConfirmClickAlertDialog("结束签到错误" + e.getMessage(), FinishLimitSignInActivity.this);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try {
                        String responseBodyStr = response.body().string();
                        AlertDialogUtil.showConfirmClickAlertDialog("限时签到结束成功", FinishLimitSignInActivity.this);
                        finish();
                    } catch (Exception e) {
                        //获取不到用户信息则取消登陆 需要重新登陆
                        AlertDialogUtil.showConfirmClickAlertDialog("结束签到错误" + e.getMessage(), FinishLimitSignInActivity.this);

                    }
                }
            });
            AlertDialogUtil.showConfirmClickAlertDialog("结束签到", FinishLimitSignInActivity.this);
        });
    }

    //初始化成员
    public void initMember(final boolean isRe) {
        if (isRe) {
            progressDialog = new ProgressDialog(FinishLimitSignInActivity.this);
            progressDialog.setMessage("刷新中...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.dismiss();
        }
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_SIGNIN_ALL_STUDENT_INFO) + signId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), FinishLimitSignInActivity.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    parseJoinedList(responseBodyStr);
                    runOnUiThread(
                            () -> {
                                signIngMemberAdapter = new SignIngMemberAdapter(FinishLimitSignInActivity.this, R.layout.item_member, memberList);
                                listView.setAdapter(signIngMemberAdapter);
                                signInNumTV.setText(memberList.size() + "人");
                            }
                    );

                } catch (Exception e) {
                    AlertDialogUtil.showToastText(e.getMessage(), FinishLimitSignInActivity.this);
                }
            }
        });
    }


    public void parseJoinedList(String JsonArrayData) {
        JSONArray jsonArray = JSONObject.parseObject(JsonArrayData).getJSONArray("data");
        if (jsonArray.size() == 0) {
            AlertDialogUtil.showToastText("暂时没有人签到", FinishLimitSignInActivity.this);
        }
        memberList.clear();
        int num = 1;
        for (int i = 0; i < jsonArray.size(); i++) {
            //TODO 发起签到之前先检查下是否签到
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            boolean isFinish = Boolean.valueOf(jsonObject.getString("isFinish"));
            if (isFinish) {
                final String studentID = jsonObject.getString("studentId");
                final String name = jsonObject.getString("nickName");
                final String experienceScore = "2";
                String checkTime = jsonObject.getString("checkinTime");
                if (checkTime.length() > 10)
                    checkTime = checkTime.substring(0, checkTime.length() - 10).replace('T', ' ');
                else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    Date d = new Date();
                    checkTime = df.format(d);
                }

                //TODO 设置能不能签到在历史签到设置
                Member member;
                member = new Member(String.valueOf(num++), "", name, studentID, experienceScore, checkTime);
                memberList.add(member);
            }
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