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
                } else {
                    GPSUtil.openGPSSettings(nowAct);
                }
            });
            myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
                if (GPSUtil.checkGPSIsOpen(nowAct)) {
                    //获取经纬度
                    GPSUtil.getTitude(view.getContext());
                    SignInUtil.checkSignIn((Activity) v.getContext(), course.getClassId());
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

}
