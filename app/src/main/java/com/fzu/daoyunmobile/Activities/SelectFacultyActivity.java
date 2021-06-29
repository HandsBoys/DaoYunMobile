package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectFacultyActivity extends AppCompatActivity {
    RecyclerView treeRecycleView;
    TreeAdapter treeAdapter;
    List<TreeBean> list;
    private Button backBtn;
    public static HashMap<String, String> idDict = new HashMap<>();


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

    }

    private void initData() {

        //排序下成员
//            if (!rankDict.containsKey(m.getExperience_score()))
//                rankDict.put(m.getExperience_score(), String.valueOf(nowRank++));

        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_DEPT), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), SelectFacultyActivity.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    idDict.clear();
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.contains("获取成功")) {
                        JSONArray jsonArray = JSONObject.parseObject(responseBodyStr).getJSONArray("data");
                        List<String> schoolList = new ArrayList<>();
                        ArrayList<List<String>> facultList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            final String schoolName = jsonObject.getString("deptName");
                            idDict.put(schoolName, jsonObject.getString("id"));
                            //获取学校信息
                            schoolList.add(schoolName);
                            //TODO 也是需要的 班级信息
                            JSONArray facults = jsonObject.getJSONArray("children");
                            List<String> fList = new ArrayList<String>();
                            for (int j = 0; j < facults.size(); j++) {
                                JSONObject f = facults.getJSONObject(j);
                                String fname = f.getString("deptName");
                                idDict.put(fname, f.getString("id"));

                                //获取学校信息
                                fList.add(fname);
                            }
                            facultList.add(fList);
                        }
                        GlobalConfig.setShcoolList(schoolList);
                        GlobalConfig.setFacultyList(facultList);
                        //TODO 后续使用院系接口
                        for (int i = 0; i < schoolList.size(); i++) {
                            TreeBean bean = new TreeBean();
                            bean.setTrunk1(schoolList.get(i));
                            list.add(bean);
                        }
                        SelectFacultyActivity.this.runOnUiThread(
                                () -> {
                                    treeAdapter = new TreeAdapter(SelectFacultyActivity.this, list);
                                    treeRecycleView.setAdapter(treeAdapter);
                                }
                        );

                    } else {
                        AlertDialogUtil.showConfirmClickAlertDialogWithLister("获取院校信息失败请重试", SelectFacultyActivity.this, (dialog, i) -> {
                            finish();
                        });
                    }

                } catch (Exception e) {
                    AlertDialogUtil.showConfirmClickAlertDialogWithLister(e.getMessage(), SelectFacultyActivity.this, (dialog, i) -> {
                        finish();
                    });
                }
            }
        });
    }
}