package com.fzu.daoyunmobile.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.fzu.daoyunmobile.Activities.OneClickSignInActivity;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.lxj.xpopup.XPopup;

import java.util.List;

public class MyCreateCourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    private int flag = 1;

    public MyCreateCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public MyCreateCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects, int flag) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.flag = flag;
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
            //viewHolder.teacherName = view.findViewById(R.id.teacher_name);
            //viewHolder.courseDate = view.findViewById(R.id.course_date);
            // viewHolder.className = view.findViewById(R.id.class_name);
            myCreateCourseViewHolder.signInImg = view.findViewById(R.id.crsignIn_Iv);
            myCreateCourseViewHolder.signInTv = view.findViewById(R.id.crsignIn_Tv);

            myCreateCourseViewHolder.codeImg = view.findViewById(R.id.crqrCode_Iv);
            myCreateCourseViewHolder.codeTv = view.findViewById(R.id.crqrCode_Tv);
//            if(flag != 1){
            //viewHolder.courseImage.setImageBitmap(BitmapFactory.decodeFile(course.getImgFilePath()));
            myCreateCourseViewHolder.signInImg.setVisibility(View.VISIBLE);
            myCreateCourseViewHolder.signInTv.setVisibility(View.VISIBLE);
//            }
            view.setTag(myCreateCourseViewHolder);
        } else {
            view = convertView;
            myCreateCourseViewHolder = (MyCreateCourseViewHolder) view.getTag();
        }


        if (course.getImgFilePath().equals("")) {
            myCreateCourseViewHolder.courseImage.setImageResource(course.getImageId());
            myCreateCourseViewHolder.courseName.setText(course.getCourseName());
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
            myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
                showPopupMenu(v);

//                    Toast.makeText(getContext(), "viewHolder.courseName.getText()", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(v.getContext(), GestureSettingActivity.class);
//                    v.getContext().startActivity(intent);
//                    SharedPreferences preferences = getSharedPreferences("sigin", Context.MODE_PRIVATE);
//                    GraphicLockView.mPassword = preferences.getString("gestureSignIn", null);
//                    OneBtnSignInSettingActivity.startOrNot = preferences.getBoolean("oneBtnSignIn", false);
//                    OneBtnSignInSettingActivity.distanceLimit = preferences.getInt("distanceLimit", -1);
//                    if(MainActivity.userName.equals("teacher")){
//
//                        if(GraphicLockView.mPassword != null){
//                            startActivity(new Intent(getContext(), FinishSignInActivity.class)
//                                    .putExtra("signin_mode","gesture_signin_mode"));
//                        }else if(OneBtnSignInSettingActivity.startOrNot == true){
//                            startActivity(new Intent(getContext(), FinishSignInActivity.class)
//                                    .putExtra("signin_mode","one_btn_mode"));
//                        }else{
//                            Intent intent = new Intent(getContext(), SignInTypeActivity.class);
//                            startActivity(intent);
//                        }
//                    }else if(GraphicLockView.mPassword != null){
//                        Intent intent = new Intent(getContext(), GestureUnlockActivity.class);
//                        startActivity(intent);
//                    }else if(OneBtnSignInSettingActivity.startOrNot == true){
//                        Intent intent = new Intent(getContext(), OneBtnSignInActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Log.i("memberInfo", PropertiesUtill.getProperties(getContext(), "gesturePassword"));
//                        Toast.makeText(getContext(), "教师尚未发起签到或签到已结束", Toast.LENGTH_SHORT).show();
//                    }
            });
            myCreateCourseViewHolder.signInTv.setOnClickListener(v -> {
                showPopupMenu(v);

                Toast.makeText(getContext(), "FUCKUO", Toast.LENGTH_SHORT).show();

            });
            myCreateCourseViewHolder.codeTv.setOnClickListener(v -> Toast.makeText(getContext(), "FUCKUOTO", Toast.LENGTH_SHORT).show()
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
                                    Intent intent = new Intent(getContext(), OneClickSignInActivity.class);
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
        ImageView signInImg;
        TextView signInTv;
        ImageView codeImg;
        TextView codeTv;
    }
}
