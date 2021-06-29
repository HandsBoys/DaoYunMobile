package com.fzu.daoyunmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.Holder.MyCreateCourseViewHolder;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;

import java.util.List;

public class MyCreateCourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    private Context mContext;
    private MyCreateCourseViewHolder myCreateCourseViewHolder;


    public MyCreateCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
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

        // myCreateCourseViewHolder.courseImage.setImageResource(course.getImageId());
        myCreateCourseViewHolder.courseName.setText(course.getCourseName());
        myCreateCourseViewHolder.courseId.setText(course.getClassId());
        myCreateCourseViewHolder.className.setText(course.getClassName());

        Activity nowAct = (Activity) view.getContext();
        myCreateCourseViewHolder.signInTv.setOnClickListener(v -> {
            if (GPSUtil.checkGPSIsOpen(nowAct)) {
                //获取经纬度
                GPSUtil.getTitude(view.getContext());
                SignInUtil.checkTeaSignIn((Activity) v.getContext(), course.getClassId());
            } else {
                GPSUtil.openGPSSettings(nowAct);
            }
        });
        myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
            if (GPSUtil.checkGPSIsOpen(nowAct)) {
                //获取经纬度
                GPSUtil.getTitude(view.getContext());
                SignInUtil.checkTeaSignIn((Activity) v.getContext(), course.getClassId());
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

        return view;
    }

}
