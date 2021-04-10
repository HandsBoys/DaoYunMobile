package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.Random;

/**
 * 验证码登录视图
 */
public class CodeLoginFragment extends Fragment {

    //输入账号框
    private InputFrameItem input_mobilenum;
    //输入验证码框
    private InputVCodeFrameItem input_vericode;
    //登录按钮
    private Button loginBtn;
    //生成的验证码
    private int verificationCode;

    public CodeLoginFragment() {

        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 登录按钮设置
        loginBtn = (Button) getActivity().findViewById(R.id.bt_login_submit);
        loginBtn.setOnClickListener(v -> Login());

        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/邮箱");
        input_vericode = new InputVCodeFrameItem(getActivity().getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);
        input_vericode.GetSubBtn().setOnClickListener(v -> {
            Random r = new Random();
            verificationCode = r.nextInt(899999) + 100000;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("验证码")
                    .setMessage("验证码为：" + verificationCode)
                    .setPositiveButton("确定", null);
            builder.show();
//            veriCodeBtn.setText("已发送");
//            veriCodeBtn.setEnabled(false);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_login, container, false);
    }

    //TODO 登录接口待做登录
    private void Login() {
        System.out.println("Login ");
        new XPopup.Builder(getContext())
                .isDarkTheme(true)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                //TODO 这里接入转换接口
                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

                                if (position == 1) {
                                    System.out.println("FUCK");
                                }
                            }
                        }).show();
        // startActivity(new Intent(getActivity(), MainActivity.class));


    }
}