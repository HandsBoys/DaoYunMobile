package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.fzu.daoyunmobile.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoSetActivity extends AppCompatActivity {

    protected RadioButton teaRadioButton;
    protected RadioButton stuRadioButton;
    protected Button backButton;
    protected Button saveBtn;
    protected EditText nameET;
    protected EditText userNameET;
    protected EditText emailET;
    protected EditText telET;
    protected String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_set);
        teaRadioButton = findViewById(R.id.rbtn_tea);
        stuRadioButton = findViewById(R.id.rbtn_stu);

        teaRadioButton.setOnCheckedChangeListener(mChangeListener);
        stuRadioButton.setOnCheckedChangeListener(mChangeListener);

        backButton = findViewById(R.id.toolbar_left_btn);
        backButton.setOnClickListener(v -> {
            finish();
        });

        //TODO SavaBtn
        saveBtn = findViewById(R.id.save_info_btn);

        userNameET = findViewById(R.id.name_Et);
        telET = findViewById(R.id.telName_Et);

        emailET = findViewById(R.id.email_Et);
        emailET.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                boolean flag = false;
                try {
                    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                    Pattern regex = Pattern.compile(check);
                    Matcher matcher = regex.matcher(emailET.getText().toString());
                    flag = matcher.matches();
                } catch (Exception e) {
                    flag = false;
                }
                if (!flag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoSetActivity.this)
                            .setMessage("邮箱名不合法，请重新输入！")
                            .setPositiveButton("确定", null);
                    builder.show();
                    emailET.setText(null);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    CompoundButton.OnCheckedChangeListener mChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.rbtn_tea && isChecked) {
                // TODO根据接口获取即可
                role = "10";
                stuRadioButton.setChecked(false);
            } else if (buttonView.getId() == R.id.rbtn_stu && isChecked) {
                role = "2";
                teaRadioButton.setChecked(false);
            }
        }
    };

}