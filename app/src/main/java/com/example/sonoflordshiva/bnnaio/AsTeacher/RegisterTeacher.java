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

import com.example.sonoflordshiva.bnnaio.LoginActivity;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.RegisterActivity;
import com.example.sonoflordshiva.bnnaio.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterTeacher extends AppCompatActivity
{

    TextView hasAccountTeach;
    ImageView backRegTeach;

    FirebaseAuth mAuth;
    DatabaseReference userRef;
    ProgressDialog loadingBar;
    Button registerPhoneAcc, registerAdminButton,loginNotAdminButton;
    EditText Name, Phone, Qualification, Email, Password;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        mAuth = FirebaseAuth.getInstance();
        //currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Teachers");

        Name = (EditText) findViewById(R.id.signTeacher_studName);
        Phone = (EditText) findViewById(R.id.signTeacher_studPhone);
        Email = (EditText) findViewById(R.id.signTeacher_studEmail);
        Qualification = (EditText) findViewById(R.id.signTeacher_studQualification);
        Password = (EditText) findViewById(R.id.signTeacher_studPassword);

        loadingBar = new ProgressDialog(this);

        registerPhoneAcc = (Button) findViewById(R.id.signTeacher_studButton);
        registerPhoneAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CreateTeacherAccount();
            }
        });

        hasAccountTeach = (TextView) findViewById(R.id.already_have_accountTeacher);
        hasAccountTeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterTeacher.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        backRegTeach = (ImageView) findViewById(R.id.backarrowRegisTeacher);
        backRegTeach.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent wec = new Intent(RegisterTeacher.this, WelcomeActivity.class);
                startActivity(wec);
            }
        });

        loadingBar = new ProgressDialog(this);
    }

    private void CreateTeacherAccount()
    {
        final String name = Name.getText().toString();
        final String phone = Phone.getText().toString();
        final String email = Email.getText().toString();
        final String qualification = Qualification.getText().toString();
        final String password = Password.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)  && TextUtils.isEmpty(qualification) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Fields are empty...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,email,qualification,password);

        }
    }

    private void ValidatePhoneNumber(final String name,final String phone,final String email,final String qualification,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Teachers").child(phone).exists()))
                {
                    HashMap<String, Object> userMap = new HashMap<String, Object>();
                    userMap.put("Name",name);
                    userMap.put("Phone",phone);
                    userMap.put("Email",email);
                    userMap.put("Qualification",qualification);
                    userMap.put("Password",password);
                    userMap.put("image","default");
                    RootRef.child("Teachers").child(phone).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(RegisterTeacher.this, "Account Created...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String msg = task.getException().getMessage();
                                Toast.makeText(RegisterTeacher.this, "Error Occurred :" + msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterTeacher.this, "The number already exist...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent welIntent = new Intent(RegisterTeacher.this,LoginTeacher.class);
                    startActivity(welIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(this,LoginTeacher.class);
        startActivity(loginIntent);
        finish();
    }
}
