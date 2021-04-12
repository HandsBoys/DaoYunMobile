
package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.StatusBarUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    //验证码按钮
    private Button veriCodeBtn;
    //注册按钮
    private Button registerBtn;
    //返回按钮
    private Button backBtn;
    //生成的验证码
    private int verificationCode;

    private InputFrameItem input_mobilenum;
    private InputFrameItem intput_psd;
    private InputFrameItem intput_confpsd;
    private InputVCodeFrameItem input_vericode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.transparencyBar(RegisterActivity.this);


        registerBtn = findViewById(R.id.bt_register_submit);
        //TODO 注册按钮 后续接入API使用
        registerBtn.setOnClickListener(v -> {
            System.out.println(input_mobilenum.GetEditText());
            System.out.println(intput_psd.GetEditText());
            System.out.println(input_vericode.GetEditText());
            finish();
        });


        backBtn = findViewById(R.id.res_back_button);
        backBtn.setOnClickListener(v -> finish());


        input_mobilenum = new InputFrameItem(getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/邮箱");
        intput_psd = new InputFrameItem(getWindow().getDecorView(), R.id.input_psd, R.drawable.ic_login_password, "密码");
        intput_confpsd = new InputFrameItem(getWindow().getDecorView(), R.id.input_confpsd, R.drawable.ic_login_password, "再次输入密码");
        input_vericode = new InputVCodeFrameItem(getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);
        veriCodeBtn = input_vericode.GetSubBtn();

        veriCodeBtn.setOnClickListener(v -> {
            Random r = new Random();
            verificationCode = r.nextInt(899999) + 100000;
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("验证码")
                    .setMessage("验证码为：" + verificationCode)
                    .setPositiveButton("确定", null);
            builder.show();
            veriCodeBtn.setText("已发送");
            veriCodeBtn.setEnabled(false);

        });

    }
}