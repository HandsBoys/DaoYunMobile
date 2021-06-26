package com.fzu.daoyunmobile.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Activities.FinishLimitSignInActivity;
import com.fzu.daoyunmobile.Activities.OneClickSignInSettingActivity;
import com.fzu.daoyunmobile.Adapter.ClassMemberAdapter;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment {

    public List<Member> memberList = new ArrayList<>();
    private LinearLayout linearLayout;
    private LinearLayout affectionCardLayout;
    private LinearLayout groupPlanLayout;
    private TextView signInTV;
    private TextView memberSumTV;
    private Button backBtn;
    private ListView listView;
    public ClassMemberAdapter memberAdapter;
    private List<Integer> experienceList = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();
    private int userMark;
    private Dialog bottomDialog;
    private String userExperienceScore;
    private TextView rankTV;
    public TextView experTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Member> tempMemberList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        backBtn = view.findViewById(R.id.toolbar_left_btn);
        memberSumTV = view.findViewById(R.id.member_sum_Tv);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> refresh());

        backBtn.setOnClickListener(v -> getActivity().finish());

        rankTV = getActivity().findViewById(R.id.ranking_Tv);
        experTV = getActivity().findViewById(R.id.experience_temp_Tv);
        signInTV = getActivity().findViewById(R.id.signin_Tv);

        //TODO 待做
        if (GlobalConfig.getIsTeacher()) {
            rankTV.setVisibility(View.GONE);
            experTV.setVisibility(View.GONE);
            signInTV.setText("发起签到");
        }
        initMember();


        linearLayout = getActivity().findViewById(R.id.signin_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalConfig.getIsTeacher()) {

                    if (GPSUtil.checkGPSIsOpen(getActivity())) {
                        //获取经纬度
                        GPSUtil.getTitude(getActivity());
                        SignInUtil.checkSignIn(getActivity(), ClassTabActivity.classId);
                    } else {
                        GPSUtil.openGPSSettings(getActivity());
                    }
                } else {
                    AlertDialogUtil.showToastText("我是学生", getActivity());
                }

            }
        });

        affectionCardLayout = getActivity().findViewById(R.id.affection_card_layout);
        affectionCardLayout.setOnClickListener(v -> AlertDialogUtil.showConfirmClickAlertDialog("暂不支持\"心意卡片\"功能", getActivity()));
        groupPlanLayout = getActivity().findViewById(R.id.group_plan_layout);
        groupPlanLayout.setOnClickListener(v -> AlertDialogUtil.showConfirmClickAlertDialog("暂不支持\"小组方案\"功能", getActivity()));

    }

    //TODO 刷新作用
    public void refresh() {
        initMember();
//        memberAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Looper.prepare();
//        bottomDialog.dismiss();
//        Looper.loop();
//        initMember();
    }


    private void initMember() {
        int rank = 0;
        for (int i = 0; i < 5; i++) {
            rank++;
            // JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String phoneNumber = "18373371896";
            final String name = "池老二";
            final String IDNumber = "200327006";
            final String experienceScore = "2";
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            final String dateT = sdf.format(date);

            Member member;
            member = new Member(String.valueOf(rank), "", name, IDNumber, experienceScore + "经验值", dateT);
            memberList.add(member);
        }

        //TODO 后续放入在After中使用
        memberAdapter = new ClassMemberAdapter(getContext(), R.layout.item_class_member, memberList);
        ListView listView = getActivity().findViewById(R.id.member_list_view);
        listView.setAdapter(memberAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = memberList.get(position);
                Toast.makeText(getContext(), member.getMemberName(), Toast.LENGTH_SHORT).show();
            }
        });
        //设置人数
        getActivity().runOnUiThread(() -> memberSumTV.setText(String.valueOf(memberList.size()) + "人"));
    }

    public void parseJoinedList(String JsonArrayData) throws JSONException {
//        JSONArray jsonArray = new JSONArray(JsonArrayData);
//        int rank = 0;
//        for(int i = 0 ; i < jsonArray.length() ; i++){
//            rank++;
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            final String phoneNumber = jsonObject.getString("phoneNumber");
//            final String name = jsonObject.getString("name");
//            final String IDNumber = jsonObject.getString("IDNumber");
//            final String experienceScore = jsonObject.getString("experienceScore");
//            final String icon = jsonObject.getString("icon");
//            if(phoneNumber.equals(MainActivity.phoneNumber)){
//                userMark = rank - 1;
//                userExperienceScore = experienceScore;
//            }
//            experienceList.add(Integer.valueOf(experienceScore));
//            indexList.add(rank-1);
//            if(icon.equals("")){
//                Member member;
//                if(name.equals("")){
//                    member = new Member(String.valueOf(rank), R.drawable.course_img_1, phoneNumber, IDNumber, experienceScore+"经验值");
//                }else{
//                    member = new Member(String.valueOf(rank), R.drawable.course_img_1, name, IDNumber, experienceScore+"经验值");
//                }
//                memberList.add(member);
//                tempMemberList.add(member);
////                memberAdapter.notifyDataSetChanged();
//            }else{
//                final File userIconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" +icon);
//                if(!userIconFile.exists()){
//                    final int finalRank = rank;
//                    final int finalRank1 = rank;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            RequestBody requestBody = new FormBody.Builder()
//                                    .add("icon", icon)
//                                    .add("type", "usericon")
//                                    .build();
//                            Request request = new Request.Builder()
//                                    .url("http://47.98.236.0:8080/downloadicon")
//                                    .post(requestBody)
//                                    .build();
//                            okHttpClient.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                    FileOutputStream os = new FileOutputStream(userIconFile);
//                                    byte[] BytesArray = response.body().bytes();
//                                    os.write(BytesArray);
//                                    os.flush();
//                                    os.close();
////                                    afterAction(finalRank, userIconFile.getAbsolutePath(), name, IDNumber, experienceScore, phoneNumber);
//                                    Member member;
//                                    if(name.equals("")){
//                                        member = new Member(String.valueOf(finalRank1), userIconFile.getAbsolutePath(), phoneNumber, IDNumber, experienceScore+"经验值");
//                                    }else{
//                                        member = new Member(String.valueOf(finalRank1), userIconFile.getAbsolutePath(), name, IDNumber, experienceScore+"经验值");
//                                    }
//                                    memberList.add(member);
//                                    tempMemberList.add(member);
////                                    memberAdapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    }).start();
//                }else{
//                    Member member;
//                    if(name.equals("")){
//                        member = new Member(String.valueOf(rank), userIconFile.getAbsolutePath(), phoneNumber, IDNumber, experienceScore+"经验值");
//                    }else{
//                        member = new Member(String.valueOf(rank), userIconFile.getAbsolutePath(), name, IDNumber, experienceScore+"经验值");
//                    }
//                    memberList.add(member);
//                    tempMemberList.add(member);
////                    memberAdapter.notifyDataSetChanged();
//                }
//
//            }
//        }
//        final int finalRank2 = rank;
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                memberSumTV.setText(finalRank2 + "人");
//            }
//        });
    }

    public void afterAction() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = experienceList.size() - 1; i >= 0; i--) {
                    for (int m = 0; m < i; m++) {
                        if (experienceList.get(m) < experienceList.get(m + 1)) {
                            int temp = experienceList.get(m);
                            experienceList.set(m, experienceList.get(m + 1));
                            experienceList.set(m + 1, temp);
                            temp = indexList.get(m);
                            indexList.set(m, indexList.get(m + 1));
                            indexList.set(m + 1, temp);
                        }
                    }
                }
                int flag = -1;
                Log.i("MemberFragInfo", "tempSize:" + memberList.size());
                try {
                    for (int i = 0; i < experienceList.size(); i++) {
                        tempMemberList.set(i, memberList.get(indexList.get(i)));
                        if (indexList.get(i) == userMark) {
                            userMark = i;
                        }
                        flag = i;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("MemberFragInfo", flag + "");
                }

//                memberList = tempMemberList;
                for (int i = 0; i < experienceList.size(); i++) {
                    memberList.set(i, tempMemberList.get(i));
                }
                int index = 1;
                for (int i = 0; i < experienceList.size(); i++) {
                    if (i == 0) {
                        indexList.set(i, 1);
                    } else if (experienceList.get(i) == experienceList.get(i - 1)) {
                        indexList.set(i, index);
                    } else if (experienceList.get(i) != experienceList.get(i - 1)) {
                        index++;
                        indexList.set(i, index);
                    }
                    memberList.get(i).setRanking(String.valueOf(indexList.get(i)));
//                    if(userMark == i && !ClassTabActivity.enterType.equals("create")){
//                        rankTV.setText("第" + index + "名");
//                        experTV.setText("当前获得" + userExperienceScore + "经验值");
//                    }
                }
//                memberAdapter = new MemberAdapter(getContext(), R.layout.member_item, memberList);
////                View view = getLayoutInflater().inflate(R.layout.fragment_member, null);
//                listView = getActivity().findViewById(R.id.member_list_view);
//                listView.setAdapter(memberAdapter);
//                Utility.setListViewHeightBasedOnChildren(listView);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Member member = memberList.get(position);
//                        Toast.makeText(getContext(), member.getMemberName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    //添加选择按钮框
    private void showPopupMenu() {
        //
        new XPopup.Builder(getContext())
                .isDarkTheme(false)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("签到类型", new String[]{"一键签到", "限时签到"},
                        (position, text) -> {
                            if (GPSUtil.getLatitude() == 0 && GPSUtil.getLongitude() == 0) {
                                AlertDialogUtil.showConfirmClickAlertDialog("GPS信息获取中，稍后重试发起签到", getActivity());
                            } else {
                                //TODO 这里接入转换接口
                                switch (position) {
                                    case 0:
                                        //发起一键签到
                                        Intent intent = new Intent(getContext(), OneClickSignInSettingActivity.class);
                                        getActivity().startActivity(intent);
                                        break;
                                    case 1:
                                        //发起限时签到
                                        Toast.makeText(getContext(), ClassTabActivity.classId, Toast.LENGTH_SHORT).show();
                                        onTimePicker();
                                    default:
                                        break;
                                }
                            }
                        }).show();
    }

    public void onTimePicker() {
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
        LinkagePicker picker = new LinkagePicker(getActivity(), provider);
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
                Intent intent = new Intent(getActivity(), FinishLimitSignInActivity.class);
                intent.putExtra("startTime", df.format(d));
                d.setTime(d.getTime() + min * 60 * 1000);
                intent.putExtra("endTime", df.format(d));
                intent.putExtra("startMode", "createSign");
                new Thread(
                        () -> {
                            startActivity(intent);
                        }
                ).start();
            }
        });
        picker.show();
    }

}