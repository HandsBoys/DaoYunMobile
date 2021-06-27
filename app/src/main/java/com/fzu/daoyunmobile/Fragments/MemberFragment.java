package com.fzu.daoyunmobile.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Adapter.ClassMemberAdapter;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.GPSUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.SignInUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
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
        if (modeCheck()) {
            rankTV.setVisibility(View.GONE);
            experTV.setVisibility(View.GONE);
            signInTV.setText("发起签到");
        }
        initMember();


        linearLayout = getActivity().findViewById(R.id.signin_layout);
        linearLayout.setOnClickListener(v -> {
            if (GPSUtil.checkGPSIsOpen(getActivity())) {
                //获取经纬度
                GPSUtil.getTitude(getActivity());
                if (modeCheck()) {
                    SignInUtil.checkTeaSignIn(getActivity(), ClassTabActivity.classId);
                } else {
                    SignInUtil.checkStuSignIn(getActivity(),ClassTabActivity.classId);
                }
            } else {
                GPSUtil.openGPSSettings(getActivity());
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


    //初始化成员
    private void initMember() {

        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_COURSE_ALL_STUDENT) + ClassTabActivity.classId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    String responseBodyStr = response.body().string();
                    if (!responseBodyStr.contains("Forbidden"))
                        parseStudentList(responseBodyStr);
                    afterAction();

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                }
            }
        });

    }


    public void parseStudentList(String JsonArrayData) throws JSONException {
        memberList = new ArrayList<>();


        JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseObject(JsonArrayData).getJSONArray("data");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String studentID = jsonObject.getString("studentId");
            String studentName = jsonObject.getString("nickName");
            String experienceScore = jsonObject.getString("score");
            Member member;
            member = new Member("1", studentName, studentID, experienceScore);
            memberList.add(member);
        }
        Collections.sort(memberList);
        // Collections.reverse(memberList);
        List<Integer> i1 = new ArrayList();
        i1.add(5);
        i1.add(2);
        Collections.sort(i1);

        //排序下成员
        HashMap<String, String> rankDict = new HashMap<>();
        int nowRank = 1;
        for (Member m : memberList) {
            if (!rankDict.containsKey(m.getExperience_score()))
                rankDict.put(m.getExperience_score(), String.valueOf(nowRank++));
        }
        for (int i = 0; i < memberList.size(); i++) {
            memberList.get(i).setRanking(rankDict.get(memberList.get(i).getExperience_score()));
            if (!modeCheck() && GlobalConfig.getUserID().equals(memberList.get(i).getStu_id())) {
                rankTV.setText("第" + memberList.get(i).getRanking() + "名");
                experTV.setText("获得" + memberList.get(i).getExperience_score() + "经验值");
            }
        }
    }

    public void afterAction() {
        getActivity().runOnUiThread(() -> {
            memberAdapter = new ClassMemberAdapter(getContext(), R.layout.item_class_member, memberList);
            ListView listView = getActivity().findViewById(R.id.member_list_view);
            listView.setAdapter(memberAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                //TODO 设置
                Member member = memberList.get(position);
                Toast.makeText(getContext(), member.getMemberName(), Toast.LENGTH_SHORT).show();
            });
            //设置人数
            memberSumTV.setText(String.valueOf(memberList.size()) + "人");
        });
    }

    private boolean modeCheck() {
        return ClassTabActivity.enterType.equals("Create");
    }

}