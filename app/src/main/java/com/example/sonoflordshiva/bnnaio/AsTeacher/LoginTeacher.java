package com.example.sonoflordshiva.bnnaio.AsTeacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sonoflordshiva.bnnaio.Model.UserTeacher;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.WelcomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginTeacher extends AppCompatActivity
{

    private TextView NeedNewAccountLink, ForgotPasword;
    ImageView back;
    private ProgressDialog loadingBar;
    private Button loginButtonTeacher;
    EditText Phone, Password;
    CheckBox chkBoxRememMe;
    String parentDBName = "Teachers";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        NeedNewAccountLink = (TextView) findViewById(R.id.ask_for_new_accTeacher);
        ForgotPasword = (TextView) findViewById(R.id.forgetPwdLog);
        loadingBar = new ProgressDialog(this);

        chkBoxRememMe = (CheckBox) findViewById(R.id.loginTeacher_user_rememberMe);
        Paper.init(this);

        Phone = (EditText) findViewById(R.id.loginTeacher_user_name);
        Password = (EditText) findViewById(R.id.loginTeacher_user_password);

        loginButtonTeacher = (Button) findViewById(R.id.login_buttonTeacher);
        loginButtonTeacher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logUserWithPhone();
            }
        });

        back = (ImageView) findViewById(R.id.backarrowLoginTeacher);
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

    private void logUserWithPhone() {
        String phone = Phone.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {

        if (chkBoxRememMe.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(phone).exists())
                {
                    UserTeacher usersData = dataSnapshot.child(parentDBName).child(phone).getValue(UserTeacher.class);
                    //User usersData = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                    if (usersData.getPhonee().equals(phone))
                    {
                        if (usersData.getPasswordd().equals(password))
                        {
                            if (parentDBName.equals("Teachers"))
                            {
                                Toast.makeText(LoginTeacher.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent loginIntent = new Intent(LoginTeacher.this,TeacherMainActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginTeacher.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginTeacher.this, "No record found", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
