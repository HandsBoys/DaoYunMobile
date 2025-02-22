package com.fzu.daoyunmobile.Utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Activities.SignInFinishLimitActivity;
import com.fzu.daoyunmobile.Activities.SignInFinishOneBtnActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.util.DateUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignInUtil {

    public static String courseId = "";

    /**
     * 发起签到
     *
     * @param type
     * @param courseID
     * @param limitTime
     * @param act
     */
    public static void startSignIn(String type, String courseID, int limitTime, Activity act) {
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
                    if (responseBodyStr.contains("success")) {
                        JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);
                        String signId = jsonObject.getString("data");
                        //一键签到
                        //signinId
                        Intent intent = null;
                        if (type.equals("1")) {
                            intent = new Intent(act, SignInFinishOneBtnActivity.class);
                            intent.putExtra("signId", signId);
                        }
                        //限时签到
                        else {
                            intent = new Intent(act, SignInFinishLimitActivity.class);

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

    /**
     * 检查是否有在进行的签到
     *
     * @param act
     * @param courseID
     */
    public static void checkTeaSignIn(Activity act, String courseID) {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_DO_SIGN_IN) + courseID, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), act);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.contains("error")) {
                        showSignInPopupMenu(act, courseID);
                        return;
                    }
                    JSONArray jsonArray = JSONObject.parseObject(responseBodyStr).getJSONArray("data");
                    if (jsonArray == null || jsonArray.size() == 0) {
                        showSignInPopupMenu(act, courseID);
                        return;
                    }
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
                                        showSignInPopupMenu(act, courseID);
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
                                Intent intent = new Intent(act, SignInFinishLimitActivity.class);
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
                                            showSignInPopupMenu(act, courseID);
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


    /**
     * 学生检查是否有在进行的签到
     *
     * @param act
     * @param courseID
     */
    public static void checkStuSignIn(Activity act, String courseID) {

        SignInUtil.courseId = courseID;
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_DO_SIGN_IN) + courseID, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), act);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.contains("error")) {
                        AlertDialogUtil.showConfirmClickAlertDialog("暂时没有发起的签到", act);
                        return;
                    }
                    JSONArray jsonArray = JSONObject.parseObject(responseBodyStr).getJSONArray("data");
                    if (jsonArray == null || jsonArray.size() == 0) {
                        AlertDialogUtil.showConfirmClickAlertDialog("暂时没有发起的签到", act);
                        return;
                    }
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String signID = jsonObject.getString("id");
                        String type = jsonObject.getString("type");
                        //获取是否能签到
                        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_SIGNIN_ALL_STUDENT_INFO) + signID, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                AlertDialogUtil.showToastText(e.getMessage(), act);
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) {
                                try {
                                    String responseBodyStr = response.body().string();
                                    if (!checkIsSignIn(responseBodyStr, act)) {
                                        if (type.equals("1")) {
                                            studentSignIn(signID, act);
                                        } else {
                                            String startTime = jsonObject.getString("startTime");
                                            String endTime = jsonObject.getString("endTime");
                                            startTime = startTime.substring(0, startTime.length() - 10).replace('T', ' ');
                                            endTime = endTime.substring(0, endTime.length() - 10).replace('T', ' ');

                                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                            Date d = new Date();
                                            Date e = df.parse(endTime);
                                            if (e.getTime() > d.getTime()) {

                                                if (GPSUtil.getLatitude() == 0 && GPSUtil.getLongitude() == 0)
                                                    AlertDialogUtil.showConfirmClickAlertDialog("GPS信息获取中，稍后重试发起签到", act);
                                                else {
                                                    studentSignIn(signID, act);
                                                }
                                            } else {
                                                //设置签到结束
                                                OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STOP_SIGN_IN) + signID, new JSONObject(), new Callback() {
                                                    @Override
                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                        System.out.println("FUCK CreateCourse" + e.getMessage());
                                                    }

                                                    @Override
                                                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                                                        try {
                                                            String responseBodyStr = response.body().string();
                                                            AlertDialogUtil.showConfirmClickAlertDialog("暂时没有发起的签到", act);

                                                        } catch (Exception e) {
                                                            //获取不到用户信息则取消登陆 需要重新登陆
                                                            AlertDialogUtil.showConfirmClickAlertDialog("暂时没有发起的签到", act);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        AlertDialogUtil.showConfirmClickAlertDialog("请勿重复签到 ", act);
                                    }

                                } catch (Exception e) {
                                    AlertDialogUtil.showToastText(e.getMessage(), act);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    // AlertDialogUtil.showToastText(e.getMessage(), act);
                }
            }
        });
    }


    /**
     * 添加签到选择按钮框
     *
     * @param act
     * @param courseID
     */
    public static void showSignInPopupMenu(Activity act, String courseID) {
        //
        new XPopup.Builder(act)
                .isDarkTheme(false)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
                //          .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
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
                                        startSignIn("1", courseID, 10, act);
                                        break;
                                    case 1:
                                        //发起限时签到
                                        onTimePicker(act, courseID);
                                    default:
                                        break;
                                }
                            }
                        }).show();
    }


    public static void studentSignIn(String signID, Activity act) {
        JSONObject signInObj = new JSONObject();
        signInObj.put("id", "0");
        signInObj.put("checkinId", signID);
        signInObj.put("lng", GPSUtil.getLongitude());
        signInObj.put("lat", GPSUtil.getLatitude());
        //设置签到结束
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_FINISH_SIGN_IN), signInObj, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showConfirmClickAlertDialog("签到成功", act);
                    studentAddScore("2", act);
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showConfirmClickAlertDialog("签到失败请重试" + e.getMessage(), act);
                }
            }
        });
    }

    public static void studentAddScore(String score, Activity act) {
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_ADD_SCORE) + ClassTabActivity.classId + "&studentId=" + GlobalConfig.getUserID() + "&score=" + score, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showConfirmClickAlertDialog("结束签到错误" + e.getMessage(), act);
                }
            }
        });
    }


    /**
     * 限时签到时间选择器
     *
     * @param act
     * @param courseID
     */
    public static void onTimePicker(Activity act, String courseID) {
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
            startSignIn("2", courseID, min, act);
        });
        picker.show();
    }

    public static Boolean checkIsSignIn(String jsonData, Activity act) {
        JSONArray jsonArray = JSONObject.parseObject(jsonData).getJSONArray("data");
        if (jsonArray == null || jsonArray.size() == 0) {
            AlertDialogUtil.showConfirmClickAlertDialog("暂时没有发起的签到", act);
            return false;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            //TODO 发起签到之前先检查下是否签到
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String studentID = jsonObject.getString("studentId");
            if (studentID.equals(GlobalConfig.getUserID())) {
                boolean isFinish = Boolean.valueOf(jsonObject.getString("isFinish"));
                return isFinish;
            }
        }
        return false;
    }

}
