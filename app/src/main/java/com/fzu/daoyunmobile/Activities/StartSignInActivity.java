package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Adapter.SignInHistoryAdapter;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.SignInHistory;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;
import com.fzu.daoyunmobile.Utils.TimeUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StartSignInActivity extends AppCompatActivity {


    public List<SignInHistory> hisList = new ArrayList<>();

    private LinearLayout linearLayout;
    private Button backBtn;
    private ListView listView;
    public SignInHistoryAdapter signAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sign_in);

        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> finish());

        linearLayout = findViewById(R.id.signin_layout);
        linearLayout.setOnClickListener(v -> {
            if (GPSUtil.checkGPSIsOpen(StartSignInActivity.this)) {
                //获取经纬度
                GPSUtil.getTitude(StartSignInActivity.this);
                SignInUtil.checkTeaSignIn(StartSignInActivity.this, ClassTabActivity.classId);
            } else {
                GPSUtil.openGPSSettings(StartSignInActivity.this);
            }

        });

        initHis();

    }


    //初始化成员
    private void initHis() {

        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_ALL_SIGN_IN) + ClassTabActivity.classId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), StartSignInActivity.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    String responseBodyStr = response.body().string();
                    parseHisList(responseBodyStr);
                    afterAction();

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), StartSignInActivity.this);
                }
            }
        });

    }


    public void parseHisList(String JsonArrayData) {
        hisList = new ArrayList<>();
        JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseObject(JsonArrayData).getJSONArray("data");
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String signId = jsonObject.getString("id");
                String type = jsonObject.getString("type");
                String lng = jsonObject.getString("lng");
                String lat = jsonObject.getString("lat");
                String startTime = TimeUtil.covertJsonFormatTime(jsonObject.getString("startTime"));
                String endTime = TimeUtil.covertJsonFormatTime(String.valueOf(jsonObject.getString("endTime")));
                boolean isFinish = Boolean.valueOf(jsonObject.getString("isFinish"));
                if (!isFinish)
                    continue;
                SignInHistory signInHistory = new SignInHistory(signId, startTime, endTime, lng, lat, isFinish);
                String d = startTime.substring(0, startTime.length() - 9);

                if (type.equals("1")) {
                    signInHistory.setDateType(d + "\t一键签到");
                    signInHistory.setConDate(startTime.substring(startTime.length() - 7, startTime.length() - 3));
                } else {
                    signInHistory.setDateType(d + "\t限时签到");
                    signInHistory.setConDate(startTime.substring(startTime.length() - 7, startTime.length() - 3) + "-" + endTime.substring(endTime.length() - 7, endTime.length() - 3) + " 持续" +
                            TimeUtil.figMinute(TimeUtil.strConvertDate(startTime), TimeUtil.strConvertDate(endTime)) + " 分钟");
                }
                hisList.add(signInHistory);
            }
        } catch (Exception e) {
            AlertDialogUtil.showToastText(e.getMessage(), StartSignInActivity.this);
        }

        Collections.reverse(hisList);
    }

    public void afterAction() {
        runOnUiThread(() -> {
            signAdapter = new SignInHistoryAdapter(StartSignInActivity.this, R.layout.item_signinhistory, hisList);
            ListView listView = findViewById(R.id.his_list_view);
            listView.setAdapter(signAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                //TODO 设置
                SignInHistory s = hisList.get(position);
                AlertDialogUtil.showToastText(s.getSignID(), StartSignInActivity.this);

                Intent intent = new Intent(StartSignInActivity.this, SignMemberSetActivity.class).
                        putExtra("signID"
                                , s.getSignID());
                startActivity(intent);
            });

        });
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    @Override

    public void onPause() {

        super.onPause();
        finish();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}