package com.fzu.daoyunmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Activities.CreateClassActivity;
import com.fzu.daoyunmobile.Activities.FinishLimitSignInActivity;
import com.fzu.daoyunmobile.Activities.FinishOneBtnSignInActivity;
import com.fzu.daoyunmobile.Activities.OneClickSignInSettingActivity;
import com.fzu.daoyunmobile.Activities.SelectFacultyActivity;
import com.fzu.daoyunmobile.Bean.TreeBean;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.Holder.MyCreateCourseViewHolder;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.util.DateUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyCreateCourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    private int flag = 1;
    private Context mContext;
    private MyCreateCourseViewHolder myCreateCourseViewHolder;


    public MyCreateCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
    }

    public MyCreateCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects, int flag) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.flag = flag;
        mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Course course = getItem(position);
        final View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            myCreateCourseViewHolder = new MyCreateCourseViewHolder();
            myCreateCourseViewHolder.courseImage = view.findViewById(R.id.course_image);
            myCreateCourseViewHolder.courseName = view.findViewById(R.id.course_name);
            myCreateCourseViewHolder.courseId = view.findViewById(R.id.course_id);
            myCreateCourseViewHolder.className = view.findViewById(R.id.class_name);
            //viewHolder.teacherName = view.findViewById(R.id.teacher_name);
            //viewHolder.courseDate = view.findViewById(R.id.course_date);
            // viewHolder.className = view.findViewById(R.id.class_name);
            myCreateCourseViewHolder.signInImg = view.findViewById(R.id.crsignIn_Iv);
            myCreateCourseViewHolder.signInTv = view.findViewById(R.id.crsignIn_Tv);

            myCreateCourseViewHolder.codeImg = view.findViewById(R.id.crqrCode_Iv);
            myCreateCourseViewHolder.codeTv = view.findViewById(R.id.crqrCode_Tv);

            myCreateCourseViewHolder.codeImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.qc_code));


            view.setTag(myCreateCourseViewHolder);
        } else {
            view = convertView;
            myCreateCourseViewHolder = (MyCreateCourseViewHolder) view.getTag();
        }

        //设置内容 暂时不用上传图片文件
        if (course.getImgFilePath().equals("")) {
            myCreateCourseViewHolder.courseImage.setImageResource(course.getImageId());
            myCreateCourseViewHolder.courseName.setText(course.getCourseName());
            myCreateCourseViewHolder.courseId.setText(course.getClassId());
            myCreateCourseViewHolder.className.setText(course.getClassName());
            //viewHolder.teacherName.setText(course.getTeacherName());
            // viewHolder.className.setText(course.getClassName());
//            viewHolder.courseDate.setText(course.getCourseDate());
        } else if (course.getImageId() == -1) {
            myCreateCourseViewHolder.courseImage.setImageBitmap(BitmapFactory.decodeFile(course.getImgFilePath()));
//            viewHolder.courseName.setText(course.getCourseName());
//            viewHolder.teacherName.setText(course.getTeacherName());
//            viewHolder.className.setText(course.getClassName());
        }

        if (flag == 2) {
            Activity nowAct = (Activity) view.getContext();
            myCreateCourseViewHolder.signInTv.setOnClickListener(v -> {
                if (GPSUtil.checkGPSIsOpen(nowAct)) {
                    //获取经纬度
                    GPSUtil.getTitude(view.getContext());
                    SignInUtil.checkSignIn((Activity) v.getContext(), course.getClassId());
                    //checkSignIn((Activity) v.getContext(), course);
                    //showPopupMenu(v, course);
                } else {
                    GPSUtil.openGPSSettings(nowAct);
                }
            });
            myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
                if (GPSUtil.checkGPSIsOpen(nowAct)) {
                    //获取经纬度
                    GPSUtil.getTitude(view.getContext());
                    SignInUtil.checkSignIn((Activity) v.getContext(), course.getClassId());

                    //checkSignIn((Activity) v.getContext(), course);
                } else {
                    GPSUtil.openGPSSettings(nowAct);
                }
            });

            myCreateCourseViewHolder.codeTv.setOnClickListener(v -> {
                        AlertDialogUtil.alertQRCode(myCreateCourseViewHolder.courseId.getText().toString(), (Activity) v.getContext());
                    }
            );
            myCreateCourseViewHolder.codeImg.setOnClickListener(v -> {
                        AlertDialogUtil.alertQRCode(myCreateCourseViewHolder.courseId.getText().toString(), (Activity) v.getContext());
                    }
            );
        }
        return view;
    }


    //添加选择按钮框
    private void showPopupMenu(Activity act, Course course) {
        //
        new XPopup.Builder(getContext())
                .isDarkTheme(false)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("签到类型", new String[]{"一键签到", "限时签到"},
                        (position, text) -> {
                            if (GPSUtil.getLatitude() == 0 && GPSUtil.getLongitude() == 0) {
                                AlertDialogUtil.showConfirmClickAlertDialog("GPS信息获取中，稍后重试发起签到", act);
                            } else {
                                //TODO 这里接入转换接口
                                switch (position) {
                                    case 0:
                                        //发起一键签到
//                                        Intent intent = new Intent(getContext(), OneClickSignInSettingActivity.class);
//                                        view.getContext().startActivity(intent);
                                        startSignIn("1", course.getClassId(), 10, act);
                                        break;
                                    case 1:
                                        //发起限时签到
                                        onTimePicker(act, course);
                                    default:
                                        break;
                                }
                            }
                        }).show();
    }

    public void onTimePicker(Activity act, Course course) {
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i = 0; i <= 23; i++) {
                    String str = DateUtils.fillZero(i);
                    firstList.add(str);
                }
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i = 0; i <= 59; i++) {
                    String str = DateUtils.fillZero(i);
                    secondList.add(str);
                }
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(act, provider);
        picker.setTitleText("限时签到时间设置");
        picker.setCanLoop(false);
        picker.setGravity(Gravity.BOTTOM);
        picker.setLabel("<-时 分->", "");
        picker.setLineVisible(true);
        picker.setHeight(700);
        picker.setSelectedIndex(0, 5);
        // picker.setAnimationStyle(R.style.Animation_CustomPopup);
        // picker.setSelectedItem("12", "9");
        picker.setOnMoreItemPickListener((OnMoreItemPickListener<String>) (first, second, third) -> {
            int min = Integer.valueOf(first) * 60 + Integer.valueOf(second);
            startSignIn("2", course.getClassId(), min, act);
        });
        picker.show();
    }

    private void startSignIn(String type, String courseID, int limitTime, Activity act) {
        JSONObject json = new JSONObject();
        json.put("id", "0");
        json.put("type", type);
        json.put("courseId", courseID);
        json.put("lng", GPSUtil.getLongitude());
        json.put("lat", GPSUtil.getLatitude());
        json.put("limitTime", limitTime);
        //获取用户信息
        OkHttpUtil.getInstance().PutWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.START_SIGN_IN), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), act);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showToastText(responseBodyStr, act);

                    if (responseBodyStr.contains("success")) {
                        JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);
                        String signId = jsonObject.getString("data");
                        //一键签到
                        //signinId
                        Intent intent = null;
                        if (type.equals("1")) {
                            intent = new Intent(act, FinishOneBtnSignInActivity.class);
                            intent.putExtra("signId", signId);
                        }
                        //限时签到
                        else {
                            intent = new Intent(act, FinishLimitSignInActivity.class);

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            Date d = new Date();
                            intent.putExtra("startTime", df.format(d));
                            d.setTime(d.getTime() + limitTime * 60 * 1000);
                            intent.putExtra("endTime", df.format(d));
                            intent.putExtra("startMode", "createSign");
                            intent.putExtra("signId", signId);
                        }

                        Intent finalIntent = intent;
                        new Thread(
                                () -> {
                                    act.startActivity(finalIntent);
                                }
                        ).start();
                    } else {
                        AlertDialogUtil.showConfirmClickAlertDialog("发起签到失败,请重试", act);
                    }
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), act);
                    AlertDialogUtil.showConfirmClickAlertDialog("发起签到失败", act);
                }
            }
        });
    }

    //检查是否有在进行的签到
    private void checkSignIn(Activity act, Course course) {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_DO_SIGN_IN) + course.getClassId(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), act);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    JSONArray jsonArray = JSONObject.parseObject(responseBodyStr).getJSONArray("data");
                    if (jsonArray.size() == 0)
                        showPopupMenu(act, course);
                    //TODO 暂时认为只有限时签到会有未结束 一键签到会留着
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String signID = jsonObject.getString("id");
                        String type = jsonObject.getString("type");
                        if (type.equals("1")) {
                            //设置签到结束 并且show pop
                            OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STOP_SIGN_IN) + signID, new JSONObject(), new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    System.out.println("FUCK CreateCourse" + e.getMessage());
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) {
                                    try {
                                        String responseBodyStr = response.body().string();
                                        showPopupMenu(act, course);
                                    } catch (Exception e) {
                                        //获取不到用户信息则取消登陆 需要重新登陆
                                        System.out.println(e.getMessage());
                                    }
                                }
                            });
                        } else {
                            String startTime = jsonObject.getString("startTime");
                            String endTime = jsonObject.getString("endTime");
                            startTime = startTime.substring(0, startTime.length() - 10).replace('T', ' ');
                            endTime = endTime.substring(0, endTime.length() - 10).replace('T', ' ');

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            Date d = new Date();
                            Date e = df.parse(endTime);
                            if (e.getTime() > d.getTime()) {
                                AlertDialogUtil.showToastText("签到未结束", act);
                                Intent intent = new Intent(act, FinishLimitSignInActivity.class);
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("startMode", "createSign");
                                intent.putExtra("signId", signID);
                                new Thread(
                                        () -> {
                                            act.startActivity(intent);
                                        }
                                ).start();

                            } else {
                                //设置签到结束 并且show pop
                                OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STOP_SIGN_IN) + signID, new JSONObject(), new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        System.out.println("FUCK CreateCourse" + e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                                        try {
                                            String responseBodyStr = response.body().string();
                                            showPopupMenu(act, course);
                                        } catch (Exception e) {
                                            //获取不到用户信息则取消登陆 需要重新登陆
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });


                            }
                        }
                    }
                } catch (Exception e) {
                    AlertDialogUtil.showToastText(e.getMessage(), act);
                }
            }
        });
    }

}
