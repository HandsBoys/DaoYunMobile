package com.fzu.daoyunmobile.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateClassActivity extends AppCompatActivity {

    private ImageView classIconIV;
    private LinearLayout termLayout;
    private TextView termTV;
    private AutoCompleteTextView classNameET;
    private EditText schoolET;
    private EditText gradeClassET;
    private Button createClassBtn;
    private Button backBtn;
    private String school;
    private String academy;
    private final int CREATE_COURSE_OK = 123456;
    private String schoolID;
    private String academyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);


        initTermSelect();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        classNameET = findViewById(R.id.class_name_Et);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GlobalConfig.getCourseList());
        classNameET.setAdapter(adapter);
        classNameET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                classNameET.showDropDown();
        });

        classNameET.setOnTouchListener((v, event) -> {
            classNameET.showDropDown();
            return false;
        });


        schoolET = findViewById(R.id.school_Et);

        schoolET.setFocusable(false);//让EditText失去焦点，然后获取点击事件

        schoolET.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, SelectFacultyActivity.class), RequestCodeConfig.getSelectFaculty());
        });

        gradeClassET = findViewById(R.id.grade_class_Et);
        createClassBtn = findViewById(R.id.create_class_btn);
        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> finish());

        classIconIV = findViewById(R.id.class_icon_Iv);
        classIconIV.setOnClickListener(v -> {

        });


        createClassBtn.setOnClickListener(v -> {
            if (classNameET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入班课名！", CreateClassActivity.this);
            } else if (schoolET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入学校院系！", CreateClassActivity.this);
                schoolET.setText("福州大学数计学院");
            } else if (gradeClassET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入班级！", CreateClassActivity.this);
            } else if (termTV.getText().toString().equals("班课学期未选择")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请选择班课学期！", CreateClassActivity.this);
            } else {

                JSONObject json = new JSONObject();
                json.put("id", "0");
                json.put("courseName", classNameET.getText().toString());
                json.put("term", termTV.getText().toString());
                json.put("teacherId", "0");
                json.put("enableJoin", "true");
                json.put("finish", "false");

                JSONObject classDto = new JSONObject();
                classDto.put("id", "0");
                classDto.put("className", gradeClassET.getText().toString());

                json.put("classDto", classDto);
                json.put("dept", schoolET.getText().toString());
                JSONArray jsonArray = new JSONArray(); //保存数组数据的JSONArray对象
                jsonArray.add(Integer.valueOf(schoolID));
                jsonArray.add(Integer.valueOf(academyID));
                json.put("deptIds", jsonArray);
                AlertDialogUtil.showConfirmClickAlertDialog(json.toString(), CreateClassActivity.this);
                //创建班课
                OkHttpUtil.getInstance().PutWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.CREATE_COURSE), json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("FUCK CreateCourse" + e.getMessage());
                        AlertDialogUtil.showToastText(e.getMessage(), CreateClassActivity.this);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try {
                            String responseBodyStr = response.body().string();
                            if (responseBodyStr.contains("操作成功")) {
                                // myJoinFragment.initCourses();
                                AlertDialogUtil.showConfirmClickAlertDialogWithLister("创建班课成功!", CreateClassActivity.this, (dialog, i) -> {
                                    Intent intent = new Intent();
                                    // 设置返回码和返回携带的数据
                                    setResult(CREATE_COURSE_OK, intent);
                                    finish();
                                });
                            } else {
                                AlertDialogUtil.showConfirmClickAlertDialog("创建班课失败请重试！" + responseBodyStr, CreateClassActivity.this);
                            }
                        } catch (Exception e) {
                            //获取不到用户信息则取消登陆 需要重新登陆
                            AlertDialogUtil.showToastText(e.getMessage(), CreateClassActivity.this);
                            AlertDialogUtil.showConfirmClickAlertDialog("创建班课失败请重试", CreateClassActivity.this);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == RequestCodeConfig.getSelectFaculty()) {//院系选择
                    school = data.getStringExtra("school");
                    academy = data.getStringExtra("academy");
                    schoolID = data.getStringExtra("schoolID");
                    academyID = data.getStringExtra("academyID");
                    schoolET.setText(school + academy);
                }
                break;
            default:
                break;
        }
    }


    private void initTermSelect() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;

        int preYear, termID;
        if (month <= 6) {
            preYear = year - 1;
            termID = 2;
        } else {
            preYear = year;
            termID = 1;
        }
        int sizeNum = termID == 2 ? 11 : 10;
        final String[] terms = new String[sizeNum];
        for (int i = 0; i < sizeNum; i++) {
            String term = String.format("%d-%d-%d", preYear, preYear + 1, termID);
            if (termID == 2) {
                preYear++;
                termID = 1;
            } else {
                termID++;
            }
            terms[i] = term;
        }

        termTV = findViewById(R.id.term_Tv);
        termTV.setText(terms[0]);
        termLayout = findViewById(R.id.term_layout);
        termLayout.setOnClickListener(v -> {

            new XPopup.Builder(CreateClassActivity.this)
                    .isDarkTheme(false)
                    .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asBottomList("班课学期选择", terms,
                            (position, text) -> {
                                termTV.setText(text);
                            }).show();

        });
    }

}