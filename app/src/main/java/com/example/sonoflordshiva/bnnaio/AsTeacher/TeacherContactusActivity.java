package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Contact;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TeacherContactusActivity extends AppCompatActivity
{

    private TextView eTeacherMail;
    private TextView pTeacherNumber;
    private TextView wTeacherAddress;
    EditText contactTeacherName, contactTeacherEmail, contactTeacherPhone, contactTeacherSpinnerValue, contactTeacherSpinnerMessage;
    Spinner selectTeacherOptions;
    Button sendTeacherAppointRequestButton;
    String requestType[] = {"-Select a type-","Business Purpose", "Learn and Explore","Others"};
    ArrayAdapter<String> adapter;
    String result, currentUserId, appointmentId;
    FirebaseAuth mAuth;
    DatabaseReference appointmentRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_contactus);

        mAuth = FirebaseAuth.getInstance();
        //currentUserId = mAuth.getCurrentUser().getUid();

        currentUserId = Prevalent.currentOnlineUser.getNamee() + Prevalent.currentOnlineUser.getPhonee();
        appointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentReq").child("Teachers");

        loadingBar = new ProgressDialog(this);

        eTeacherMail = (TextView) findViewById(R.id.contactTeacher_email);
        eTeacherMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                semdMail();
            }
        });
        pTeacherNumber = (TextView) findViewById(R.id.contactTeacher_number);
        pTeacherNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contactBnn();
            }
        });
        wTeacherAddress = (TextView) findViewById(R.id.contactTeacher_web);
        wTeacherAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent bnnLink = new Intent(Intent.ACTION_VIEW);
                bnnLink.setData(Uri.parse("http://bnncollege.org"));
                startActivity(bnnLink);
            }
        });

        contactTeacherName = (EditText) findViewById(R.id.contactTeacherName);
        contactTeacherEmail = (EditText) findViewById(R.id.contactTeacherEmail);
        contactTeacherPhone = (EditText) findViewById(R.id.contactTeacherPhone);
        contactTeacherSpinnerValue = (EditText) findViewById(R.id.contactTeacherSpinnerValue);
        contactTeacherSpinnerMessage = (EditText) findViewById(R.id.contactTeacherSpinnerMessage);

        selectTeacherOptions = (Spinner) findViewById(R.id.selectTeacherOptions);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,requestType);
        selectTeacherOptions.setAdapter(adapter);
        selectTeacherOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select a type-";
                        contactTeacherSpinnerValue.setText(result);
                        break;
                    case 1:
                        result = "Business Purpose";
                        contactTeacherSpinnerValue.setText(result);
                        break;
                    case 2:
                        result = "Learn and Explore";
                        contactTeacherSpinnerValue.setText(result);
                        break;
                    case 3:
                        result = "Others";
                        contactTeacherSpinnerValue.setText(result);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sendTeacherAppointRequestButton = (Button) findViewById(R.id.sendTeacherAppointRequestButton);
        sendTeacherAppointRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForAppointment();
            }
        });
    }

    private void RequestForAppointment()
    {
        String name = contactTeacherName.getText().toString();
        String email = contactTeacherEmail.getText().toString();
        String phone = contactTeacherPhone.getText().toString();
        String spinnerValue = contactTeacherSpinnerValue.getText().toString();
        String message = contactTeacherSpinnerMessage.getText().toString();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        appointmentId = saveCurrentDate + saveCurrentTime;

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(spinnerValue) && TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Field's are empty.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            HashMap<String,Object> appointmentMap = new HashMap<String,Object>();
            appointmentMap.put("fullname", name);
            appointmentMap.put("appointmentType" , spinnerValue);
            appointmentMap.put("appointmentMessage",message);
            appointmentMap.put("uid",currentUserId);
            appointmentMap.put("appointmentId",appointmentId);
            appointmentMap.put("time",saveCurrentTime);
            appointmentMap.put("date",saveCurrentDate);
            appointmentMap.put("phone",Prevalent.currentOnlineUser.getPhonee());
            /*feedbackMap.put("Contact",contact);
            feedbackMap.put("UID",uid);
            feedbackMap.put("Password",password);
            feedbackMap.put("image","default");*/
            appointmentRef.child(spinnerValue).child(currentUserId).child(appointmentId).updateChildren(appointmentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SendUserToMainActivity();
                        Toast.makeText(TeacherContactusActivity.this, "Request for appointment successfully submitted. We'll let you know if confirmed...", Toast.LENGTH_LONG).show();
                        contactTeacherName.setText("");
                        contactTeacherEmail.setText("");
                        contactTeacherPhone.setText("");
                        contactTeacherSpinnerValue.setText("");
                        contactTeacherSpinnerMessage.setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(TeacherContactusActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void contactBnn()
    {
        String number = "02522230879";
        Uri call = Uri.parse("tel:" + number);
        Intent contactIntent = new Intent(Intent.ACTION_CALL,call);
        startActivity(Intent.createChooser(contactIntent,"Choose Calling Client"));
    }

    private void semdMail()
    {
        Uri uri = Uri.parse("mailto:bnncollege1966@gmail.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(intent,"Choose Mailing Client"));
    }

}
