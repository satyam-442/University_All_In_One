package com.example.sonoflordshiva.bnnaio.AsTeacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sonoflordshiva.bnnaio.ForgotPassword;
import com.example.sonoflordshiva.bnnaio.LoginActivity;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.RegisterActivity;
import com.example.sonoflordshiva.bnnaio.WelcomeActivity;

public class LoginTeacher extends AppCompatActivity
{

    private TextView NeedNewAccountLink, ForgotPasword;
    ImageView back;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        NeedNewAccountLink = (TextView) findViewById(R.id.ask_for_new_accTeacher);
        ForgotPasword = (TextView) findViewById(R.id.forgetPwdLog);
        loadingbar = new ProgressDialog(this);
        back = (ImageView) findViewById(R.id.backarrowLogin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wec = new Intent(LoginTeacher.this, WelcomeActivity.class);
                startActivity(wec);
            }
        });

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginTeacher.this, RegisterTeacher.class);
                startActivity(registerIntent);
                finish();
            }
        });

        /*ForgotPasword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });*/

    }
}
