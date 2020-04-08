package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sonoflordshiva.bnnaio.AsTeacher.LoginTeacher;
import com.example.sonoflordshiva.bnnaio.AsTeacher.TeacherMainActivity;
import com.example.sonoflordshiva.bnnaio.Model.UserTeacher;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity
{

    private TextView regis, continueAsTeacher;
    private Button login;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login = (Button) findViewById(R.id.welcomeLogin);
        regis = (TextView) findViewById(R.id.welcomeRegister);
        continueAsTeacher = (TextView) findViewById(R.id.continueAsTeacher);
        continueAsTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logTeach = new Intent(WelcomeActivity.this, LoginTeacher.class);
                startActivity(logTeach);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(log);
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        /*String UserPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccessToAccount(UserPhoneKey,UserPasswordKey);
            }
        }*//**/
    }

    private void AllowAccessToAccount(final String phone,final String password) {
        loadingBar.setMessage("please wait");
        loadingBar.dismiss();
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Teachers").child(phone).exists())
                {
                    UserTeacher usersData = dataSnapshot.child("Teachers").child(phone).getValue(UserTeacher.class);
                    //User usersData = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                    if (usersData.getPhonee().equals(phone))
                    {
                        if (usersData.getPasswordd().equals(password))
                        {
                            /*Toast.makeText(WelcomeActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            SendUserToMainActivity();*/
                            Intent loginIntent = new Intent(WelcomeActivity.this, TeacherMainActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(WelcomeActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(WelcomeActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
