package com.fzu.daoyunmobile.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fzu.daoyunmobile.Adapter.MemberAdapter;
import com.fzu.daoyunmobile.Entity.Member;
import com.fzu.daoyunmobile.R;

import java.util.ArrayList;
import java.util.List;

public class FinishSignInActivity extends AppCompatActivity {

    private Button finishSignInBtn;
    private TextView signInNumTV;
    private Button backBtn;
    private ListView listView;
    private List<Member> memberList = new ArrayList<>();
    private MemberAdapter memberAdapter;
    private String signinId;
    private TextView refreshTV;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_sign_in);
    }
}