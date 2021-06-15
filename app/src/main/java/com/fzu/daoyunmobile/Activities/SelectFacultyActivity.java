package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.fzu.daoyunmobile.Adapter.TreeAdapter;
import com.fzu.daoyunmobile.Bean.TreeBean;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectFacultyActivity extends AppCompatActivity {
    RecyclerView treeRecycleView;
    TreeAdapter treeAdapter;
    List<TreeBean> list;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_faculty);
        //获取树的内容
        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> finish());

        treeRecycleView = findViewById(R.id.tree_recycle);
        //设置层级管理
        treeRecycleView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        initData();
        treeAdapter = new TreeAdapter(this, list);
        treeRecycleView.setAdapter(treeAdapter);
    }

    private void initData() {
        List<String> schoolList = GlobalConfig.getShcoolList();
        //TODO 后续使用院系接口
        for (int i = 0; i < schoolList.size(); i++) {
            TreeBean bean = new TreeBean();
            bean.setTrunk1(schoolList.get(i));
            list.add(bean);
        }

        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_DEPT), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("GET CreateCourse" + e.getMessage());
                AlertDialogUtil.showToastText(e.getMessage(), SelectFacultyActivity.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    System.out.println(responseBodyStr);
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showConfirmClickAlertDialog(e.getMessage(), SelectFacultyActivity.this);
                    finish();
                }
            }
        });

    }
}