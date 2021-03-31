package com.fzu.daoyunmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fzu.daoyunmobile.R;


public class LoginActivity extends AppCompatActivity {
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        registerBtn = (Button) findViewById(R.id.bt_login_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // JumpToRegister();
            }
        });
    }

    private void JumpToRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}