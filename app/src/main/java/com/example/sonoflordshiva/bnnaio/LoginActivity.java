package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private Button LoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink, ForgotPasword;
    ImageView back;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private TextView Info;
    private int count=3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Info = (TextView) findViewById(R.id.attempt_left);

        mAuth = FirebaseAuth.getInstance();
        NeedNewAccountLink = (TextView) findViewById(R.id.ask_for_new_acc);
        UserEmail = (EditText) findViewById(R.id.login_user_name);
        UserPassword = (EditText) findViewById(R.id.login_user_password);
        LoginButton = (Button) findViewById(R.id.login_button);
        ForgotPasword = (TextView) findViewById(R.id.forgetPwdLog);
        loadingbar = new ProgressDialog(this);
        back = (ImageView) findViewById(R.id.backarrowLogin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wec = new Intent(LoginActivity.this,WelcomeActivity.class);
                startActivity(wec);
            }
        });

        /*Spinner mySpinner = (Spinner) findViewById(R.id.selectuser);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(LoginActivity.this,R.layout.colour_spinner,getResources().getStringArray(R.array.users));
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 1)
                {
                    Intent studIntent = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(studIntent);
                    finish();
                }else
                    if(position == 2)
                {
                    Intent teachIntent = new Intent(LoginActivity.this,TeacherLogin.class);
                    startActivity(teachIntent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });*/



        Info.setText("Attempt Left : 3");

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToRegisterActivity();
            }
        });

        ForgotPasword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AllowUserToLogin();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }


    private void AllowUserToLogin()
    {
        final String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "E-mail is Mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(LoginActivity.this, "Incorrect E-mail", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Pass is Mandatory...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Login Status");
            loadingbar.setMessage("please wait...");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            count--;
            Info.setText("Attempt Left :" + count);
            if(count == 0)
            {
                LoginButton.setEnabled(false);
            }
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Logged In Successfully...", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
