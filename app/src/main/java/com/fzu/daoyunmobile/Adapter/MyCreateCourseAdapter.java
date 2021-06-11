package com.fzu.daoyunmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.core.content.ContextCompat;

import com.fzu.daoyunmobile.Activities.OneClickSignInActivity;
import com.fzu.daoyunmobile.Activities.OneClickSignInSettingActivity;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.lxj.xpopup.XPopup;

import java.util.List;

public class MyCreateCourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    private int flag = 1;
    private Context mContext;

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
        final MyCreateCourseViewHolder myCreateCourseViewHolder;
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
            myCreateCourseViewHolder.signInTv.setOnClickListener(v -> {
                showPopupMenu(v);
            });
            myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
                showPopupMenu(v);
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
    private void showPopupMenu(View view) {
        //
        new XPopup.Builder(getContext())
                .isDarkTheme(false)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("签到类型", new String[]{"限时签到", "一键签到"},
                        (position, text) -> {
                            //TODO 这里接入转换接口
                            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                            switch (position) {
                                case 0:

                                    break;
                                case 1:
                                    Intent intent = new Intent(getContext(), OneClickSignInSettingActivity.class);
                                    view.getContext().startActivity(intent);
                                default:
                                    break;
                            }
                        }).show();
    }

    class MyCreateCourseViewHolder {
        ImageView courseImage;
        TextView courseName;
        TextView teacherName;
        TextView className;
        TextView courseDate;
        TextView courseId;

        ImageView signInImg;
        TextView signInTv;
        ImageView codeImg;
        TextView codeTv;
    }
}
