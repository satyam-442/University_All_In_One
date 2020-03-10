package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity
{
    private EditText FullName, FatherName, RollNo, Email, ContactNo,UID, Password;
    private Button SaveInformationBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String currentUserID;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Students").child(currentUserID);

        loadingBar = new ProgressDialog(this);

        FullName = (EditText) findViewById(R.id.setup_fullname);
        FatherName = (EditText) findViewById(R.id.setup_fathersname);
        RollNo = (EditText) findViewById(R.id.setup_rollno);
        Email = (EditText) findViewById(R.id.setup_email);
        ContactNo = (EditText) findViewById(R.id.setup_contact);
        UID = (EditText) findViewById(R.id.setup_uid);
        Password = (EditText) findViewById(R.id.setup_password);

        SaveInformationBtn = (Button) findViewById(R.id.setup_saveInfo);

        SaveInformationBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveAccountInfo();
            }
        });
    }

    private void SaveAccountInfo()
    {
        String fullname = FullName.getText().toString();
        String fathername = FatherName.getText().toString();
        String rollno = RollNo.getText().toString();
        String email = Email.getText().toString();
        String contact = ContactNo.getText().toString();
        String uid = UID.getText().toString();
        String password = Password.getText().toString();
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Name is Empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fathername))
        {
            Toast.makeText(this, "Father Name is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(rollno))
        {
            Toast.makeText(this, "Roll No is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email Is Empty", Toast.LENGTH_SHORT).show();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Unknown Pattern Occurred", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(contact))
        {
            Toast.makeText(this, "Contact Required", Toast.LENGTH_SHORT).show();
        }
        if(contact.length()!=10)
        {
            Toast.makeText(this, "Enter Correct Contact no", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(uid))
        {
            Toast.makeText(this, "UID Required", Toast.LENGTH_SHORT).show();
        }
        if(uid.length()!=10)
        {
            Toast.makeText(this, "Enter Correct UID", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password Should not be empty.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Info");
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            HashMap<String,Object> studentMap = new HashMap<String,Object>();
            studentMap.put("Fullname", fullname);
            studentMap.put("Fathers Name" , fathername);
            studentMap.put("Roll No",rollno);
            studentMap.put("Email",email);
            studentMap.put("Contact",contact);
            studentMap.put("UID",uid);
            studentMap.put("Password",password);
            studentMap.put("image","default");
            UserRef.updateChildren(studentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Your Account Created Successfully...", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
