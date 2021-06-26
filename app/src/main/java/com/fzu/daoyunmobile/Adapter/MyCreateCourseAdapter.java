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

import com.fzu.daoyunmobile.Activities.FinishLimitSignInActivity;
import com.fzu.daoyunmobile.Activities.OneClickSignInSettingActivity;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.Holder.MyCreateCourseViewHolder;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.lxj.xpopup.XPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.util.DateUtils;

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
                    showPopupMenu(v, course);
                } else {
                    GPSUtil.openGPSSettings(nowAct);
                }
            });
            myCreateCourseViewHolder.signInImg.setOnClickListener(v -> {
                if (GPSUtil.checkGPSIsOpen(nowAct)) {
                    //获取经纬度
                    GPSUtil.getTitude(view.getContext());
                    showPopupMenu(v, course);
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
    private void showPopupMenu(View view, Course course) {
        //
        new XPopup.Builder(getContext())
                .isDarkTheme(false)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("签到类型", new String[]{"一键签到", "限时签到"},
                        (position, text) -> {
                            if (GPSUtil.getLatitude() == 0 && GPSUtil.getLongitude() == 0) {
                                AlertDialogUtil.showConfirmClickAlertDialog("GPS信息获取中，稍后重试发起签到", (Activity) view.getContext());
                            } else {
                                //TODO 这里接入转换接口
                                switch (position) {
                                    case 0:

                                        Intent intent = new Intent(getContext(), FinishLimitSignInActivity.class);
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                        Date d = new Date();
                                        intent.putExtra("startTime", df.format(d));
                                        intent.putExtra("limitTime", "10");
                                        intent.putExtra("startMode", "createSign");

                                        //发起一键签到
//                                        Intent intent = new Intent(getContext(), OneClickSignInSettingActivity.class);
                                        view.getContext().startActivity(intent);
                                        break;
                                    case 1:
                                        //发起限时签到
                                        Toast.makeText(getContext(), course.getClassId(), Toast.LENGTH_SHORT).show();
                                        onTimePicker(view);
                                    default:
                                        break;
                                }
                            }
                        }).show();
    }

    public void onTimePicker(View view) {
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
//                    if (firstIndex == 0) {
//                        str += "￥";
//                    } else {
//                        str += "$";
//                    }
                    firstList.add(str);
                }
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i = 0; i <= 59; i++) {
                    String str = DateUtils.fillZero(i);
//                    if (firstIndex == 0) {
//                        str += "￥";
//                    } else {
//                        str += "$";
//                    }

                    secondList.add(str);
                }
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker((Activity) view.getContext(), provider);
        picker.setTitleText("限时签到时间设置");
        picker.setCanLoop(false);
        picker.setGravity(Gravity.BOTTOM);
        picker.setLabel("<-时 分->", "");
        picker.setLineVisible(true);
        picker.setHeight(700);
        picker.setSelectedIndex(0, 5);
        // picker.setAnimationStyle(R.style.Animation_CustomPopup);
        // picker.setSelectedItem("12", "9");
        picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {

            @Override
            public void onItemPicked(String first, String second, String third) {
//                String str = "2021-06-25T21:02:15.000+08:00";
//                String str2 = str.substring(0, str.length() - 10).replace('T', ' ');
//
//
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                Date d = new Date();
//                Date e = null;
//                try {
//                    e = df.parse(str2);
//                } catch (ParseException parseException) {
//                    parseException.printStackTrace();
//                }
//                e.setTime(e.getTime() + 1000 * 60 * 10);
//                Log.v("fuck", df.format(d));
                //Log.v("fuck",df.format(e));
                int min = Integer.valueOf(first) * 60 + Integer.valueOf(second);
                Intent intent = new Intent(view.getContext(), FinishLimitSignInActivity.class);

                intent.putExtra("startTime", df.format(d));
                intent.putExtra("limitTime", min);
                intent.putExtra("startMode", "createSign");
                try{
                    view.getContext().startActivity(intent);

                }
                catch (Exception e){
                    AlertDialogUtil.showConfirmClickAlertDialog(e.getMessage() , (Activity) view.getContext());

                }
                //AlertDialogUtil.showConfirmClickAlertDialog(first + "-" + second + "-" + df.format(d) , (Activity) view.getContext());
            }
        });
        picker.show();
    }

}
