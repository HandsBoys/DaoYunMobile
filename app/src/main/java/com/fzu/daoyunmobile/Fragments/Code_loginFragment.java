package com.fzu.daoyunmobile.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.activities.LoginActivity;
import com.fzu.daoyunmobile.activities.RegisterActivity;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Code_loginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Code_loginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //输入账号框
    private InputFrameItem input_mobilenum;
    //输入验证码框
    private InputVCodeFrameItem input_vericode;
    //登录按钮
    private Button loginBtn;
    //生成的验证码
    private int verificationCode;

    public Code_loginFragment() {

        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Code_loginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Code_loginFragment newInstance(String param1, String param2) {
        Code_loginFragment fragment = new Code_loginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    }
}