package com.fzu.daoyunmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.Holder.MyJoinCourseViewHolder;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;

import java.util.List;

public class MyJoinCourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;

    public MyJoinCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Course course = getItem(position);
        final View view;
        final MyJoinCourseViewHolder myJoinCourseViewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            myJoinCourseViewHolder = new MyJoinCourseViewHolder();
            myJoinCourseViewHolder.courseName = view.findViewById(R.id.course_name);
            myJoinCourseViewHolder.teacherName = view.findViewById(R.id.teacher_name);
            myJoinCourseViewHolder.courseDate = view.findViewById(R.id.course_date);
            // viewHolder.className = view.findViewById(R.id.class_name);
            myJoinCourseViewHolder.signInImg = view.findViewById(R.id.signIn_Iv);
            myJoinCourseViewHolder.signInTv = view.findViewById(R.id.signIn_Tv);
            myJoinCourseViewHolder.signInImg.setVisibility(View.VISIBLE);
            myJoinCourseViewHolder.signInTv.setVisibility(View.VISIBLE);
//            }
            view.setTag(myJoinCourseViewHolder);
        } else {
            view = convertView;
            myJoinCourseViewHolder = (MyJoinCourseViewHolder) view.getTag();
        }


        if (course.getImgFilePath().equals("")) {
            myJoinCourseViewHolder.courseName.setText(course.getCourseName());
            myJoinCourseViewHolder.teacherName.setText(course.getTeacherName());
            // viewHolder.className.setText(course.getClassName());
            myJoinCourseViewHolder.courseDate.setText(course.getCourseDate());
        } else if (course.getImageId() == -1) {
//            viewHolder.courseName.setText(course.getCourseName());
//            viewHolder.teacherName.setText(course.getTeacherName());
//            viewHolder.className.setText(course.getClassName());
        }
        Activity act = (Activity) view.getContext();

        myJoinCourseViewHolder.signInImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GPSUtil.checkGPSIsOpen(act)) {
                    //获取经纬度
                    GPSUtil.getTitude(act);
                    SignInUtil.checkStuSignIn((Activity) v.getContext(), course.getClassId());
                } else {
                    GPSUtil.openGPSSettings(act);
                }
            }
        });
        myJoinCourseViewHolder.signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GPSUtil.checkGPSIsOpen(act)) {
                    //获取经纬度
                    GPSUtil.getTitude(act);
                    SignInUtil.checkStuSignIn((Activity) v.getContext(), course.getClassId());
                } else {
                    GPSUtil.openGPSSettings(act);
                }
            }
        });
        return view;
    }
}
