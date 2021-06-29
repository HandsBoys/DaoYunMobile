package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Adapter.SignIngMemberAdapter;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.TimeUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignInMemberResultActivity extends AppCompatActivity {
    private Button backBtn;
    //未签到列表
    private ListView usListView;
    private List<Member> usMemberList = new ArrayList<>();
    private SignIngMemberAdapter usSignIngMemberAdapter;
    //已签到列表
    private ListView sListView;
    private List<Member> sMemberList = new ArrayList<>();
    private SignIngMemberAdapter signIngMemberAdapter;

    private String signID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_member_result);
        backBtn = findViewById(R.id.toolbar_left_btn);
        //返回
        backBtn.setOnClickListener(v -> {
            finish();
        });
        usListView = findViewById(R.id.unsignedIn_listview);
        sListView = findViewById(R.id.signedIn_listview);
        signID = getIntent().getStringExtra("signID");
        //初始化member
        initMember();
    }

    public void initMember() {
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_SIGNIN_ALL_STUDENT_INFO) + signID, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), SignInMemberResultActivity.this);
            }


            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    praseJsonToList(responseBodyStr);
                    afterAction();
                } catch (Exception e) {
                   // AlertDialogUtil.showToastText(e.getMessage(), SignInMemberResultActivity.this);
                }
            }
        });

    }

    private void praseJsonToList(String jsonData) {
        JSONArray jsonArray = JSONObject.parseObject(jsonData).getJSONArray("data");
        if(jsonArray==null)
            return;
        int usNum = 1, sNum = 1;
        for (int i = 0; i < jsonArray.size(); i++) {
            //TODO 发起签到之前先检查下是否签到
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String studentID = jsonObject.getString("studentId");
            final String name = jsonObject.getString("nickName");
            final String experienceScore = "2";
//            String lat=jsonObject.getString("");
//            String lnt=jsonObject.getString("");

            boolean isFinish = Boolean.valueOf(jsonObject.getString("isFinish"));
            String checkTime = String.valueOf(jsonObject.getString("checkinTime"));

            if (checkTime.length() > 10)
                checkTime = TimeUtil.covertJsonFormatTime(checkTime);
            else
                checkTime = "";
            Member member;
            if (isFinish) {
                member = new Member(String.valueOf(sNum++), name, studentID, experienceScore, checkTime);
                sMemberList.add(member);
            } else {
                member = new Member(String.valueOf(usNum++), name, studentID, experienceScore, checkTime);
                usMemberList.add(member);
            }
        }
    }

    private void afterAction() {
        runOnUiThread(
                () -> {
                    signIngMemberAdapter = new SignIngMemberAdapter(SignInMemberResultActivity.this, R.layout.item_member, sMemberList);
                    sListView.setAdapter(signIngMemberAdapter);
                    signIngMemberAdapter.notifyDataSetChanged();
                    sListView.setOnItemClickListener((parent, view, position, id) -> {
                        //TODO 设置
                        Member m = sMemberList.get(position);
                        AlertDialogUtil.showToastText(m.getMemberName(), SignInMemberResultActivity.this);
                    });

                    usSignIngMemberAdapter = new SignIngMemberAdapter(SignInMemberResultActivity.this, R.layout.item_member, usMemberList);
                    usListView.setAdapter(usSignIngMemberAdapter);
                    usSignIngMemberAdapter.notifyDataSetChanged();
                    usListView.setOnItemClickListener((parent, view, position, id) -> {
                        //TODO 设置
                        Member m = usMemberList.get(position);
                        AlertDialogUtil.showToastText(m.getMemberName(), SignInMemberResultActivity.this);

                    });
                }
        );
    }

    private void showPopUp() {

    }
}