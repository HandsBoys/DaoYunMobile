package com.fzu.daoyunmobile.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.google.zxing.activity.CaptureActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateClassActivity extends AppCompatActivity {

    private ImageView classIconIV;
    private LinearLayout termLayout;
    private TextView termTV;
    private EditText classNameET;
    private EditText schoolET;
    private EditText gradeClassET;
    private EditText classIntroductionET;
    private Button createClassBtn;
    private Button backBtn;
    private final int IMAGE_SELECT = 1;
    private final int IMAGE_CUT = 2;
    private File cropFile = null;
    private String selectedTerm;
    private String school;
    private String academy;
    private final int CREATE_COURSE_OK = 123456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);


        initTermSelect();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        classNameET = findViewById(R.id.class_name_Et);
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
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, IMAGE_SELECT);
        });


        createClassBtn.setOnClickListener(v -> {
            if (classNameET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入班课名！", CreateClassActivity.this);
            } else if (schoolET.getText().toString().equals("")) {
                AlertDialogUtil.showConfirmClickAlertDialog("请输入学校院系！", CreateClassActivity.this);
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
                JSONObject classDto = new JSONObject();
                classDto.put("id", "0");
                classDto.put("className", gradeClassET.getText().toString());
                json.put("classDto", classDto);

                //获取用户信息
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
                            AlertDialogUtil.showToastText(responseBodyStr, CreateClassActivity.this);
                            if (responseBodyStr.contains("操作成功")) {
                                // myJoinFragment.initCourses();
                                AlertDialogUtil.showToastText("创建班课成功!", CreateClassActivity.this);
                                Intent intent = new Intent();
                                // 设置返回码和返回携带的数据
                                setResult(CREATE_COURSE_OK, intent);
                                finish();
                            } else {
                                AlertDialogUtil.showConfirmClickAlertDialog("创建班课失败请重试！" + responseBodyStr, CreateClassActivity.this);
                            }
                        } catch (Exception e) {
                            //获取不到用户信息则取消登陆 需要重新登陆
                            AlertDialogUtil.showToastText(e.getMessage(), CreateClassActivity.this);
                            AlertDialogUtil.showConfirmClickAlertDialog("创建班课失败请重试", CreateClassActivity.this);
                            System.out.println(e.getMessage());
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
                if (requestCode == IMAGE_CUT) {
                    Log.i("UserInfoInfo", Environment.getExternalStorageDirectory().toString());
//                    userIconIV.setImageURI(cropImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath());
                    classIconIV.setImageBitmap(bitmap);
                    Log.i("UserInfoInfo", "cropFile.toString()");
                } else if (requestCode == IMAGE_SELECT) {
                    Uri iconUri = data.getData();
                    startCropImage(iconUri);
                } else if (requestCode == RequestCodeConfig.getSelectFaculty()) {//院系选择
                    school = data.getStringExtra("school");
                    academy = data.getStringExtra("academy");
                    schoolET.setText(school + academy);
                }
                break;
            default:
                break;
        }
    }

    private void startCropImage(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            intent.setDataAndType(uri, "image/*");
            // 使图片处于可裁剪状态
            intent.putExtra("crop", "true");
            // 裁剪框的比例（根据需要显示的图片比例进行设置）
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // 让裁剪框支持缩放
            intent.putExtra("scale", true);
            // 裁剪后图片的大小（注意和上面的裁剪比例保持一致）
            intent.putExtra("outputX", 1000);
            intent.putExtra("outputY", 1000);
            // 传递原图路径
            cropFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + timeStamp + ".jpg");
//            cropFile = new File(path + File.separator + MainActivity.userName + ".jpg");
            if (cropFile.exists()) {
                cropFile.delete();
            }
            Uri cropImageUri = Uri.fromFile(cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            //intent.putExtra("circleCrop", true);
            // 设置图片的输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // return-data=true传递的为缩略图，小米手机默认传递大图，所以会导致onActivityResult调用失败
            intent.putExtra("return-data", false);
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, IMAGE_CUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTermSelect() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
//        int day = t.monthDay;
//        int hour = t.hour; // 0-23
//        int minute = t.minute;
//        int second = t.second;
//        System.out.println("Calendar获取当前日期" + year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second);
//
//        final String[] term = new String[]{"2020-2021-1", "2020-2021-2",
//                "2021-2022-1", "2021-2022-2", "2022-2023-1", "2022-2023-2", "2023-2024-1", "2023-2024-2",
//                "2024-2025-1", "2024-2025-2", "2025-2026-1", "2025-2026-2", "2026-2027-1", "2026-2027-2",};
        int preYear, termID;
        //        if (month < 8 && month > 2) {
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
                                //TODO 这里接入转换接口
                                Toast.makeText(CreateClassActivity.this, text, Toast.LENGTH_SHORT).show();
                                termTV.setText(text);
                            }).show();

        });
    }

}