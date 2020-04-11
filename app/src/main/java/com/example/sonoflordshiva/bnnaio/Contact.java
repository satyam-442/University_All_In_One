package com.example.sonoflordshiva.bnnaio;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Contact extends AppCompatActivity
{
    private TextView eMail;
    private TextView pNumber;
    private TextView wAddress;
    EditText contactName, contactEmail, contactPhone, contactSpinnerValue, contactSpinnerMessage;
    Spinner selectOptions;
    Button sendAppointRequestButton;
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
        setContentView(R.layout.activity_contact);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        appointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentReq");

        loadingBar = new ProgressDialog(this);

        eMail = (TextView) findViewById(R.id.contact_email);
        eMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                semdMail();
            }
        });
        pNumber = (TextView) findViewById(R.id.contact_number);
        pNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contactBnn();
            }
        });
        wAddress = (TextView) findViewById(R.id.contact_web);
        wAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent bnnLink = new Intent(Intent.ACTION_VIEW);
                bnnLink.setData(Uri.parse("http://bnncollege.org"));
                startActivity(bnnLink);
            }
        });

        contactName = (EditText) findViewById(R.id.contactName);
        contactEmail = (EditText) findViewById(R.id.contactEmail);
        contactPhone = (EditText) findViewById(R.id.contactPhone);
        contactSpinnerValue = (EditText) findViewById(R.id.contactSpinnerValue);
        contactSpinnerMessage = (EditText) findViewById(R.id.contactSpinnerMessage);

        selectOptions = (Spinner) findViewById(R.id.selectOptions);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,requestType);
        selectOptions.setAdapter(adapter);
        selectOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select a type-";
                        contactSpinnerValue.setText(result);
                        break;
                    case 1:
                        result = "Business Purpose";
                        contactSpinnerValue.setText(result);
                        break;
                    case 2:
                        result = "Learn and Explore";
                        contactSpinnerValue.setText(result);
                        break;
                    case 3:
                        result = "Others";
                        contactSpinnerValue.setText(result);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sendAppointRequestButton = (Button) findViewById(R.id.sendAppointRequestButton);
        sendAppointRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForAppointment();
            }
        });

    }

    private void RequestForAppointment()
    {
        String name = contactName.getText().toString();
        String email = contactEmail.getText().toString();
        String phone = contactPhone.getText().toString();
        String spinnerValue = contactSpinnerValue.getText().toString();
        String message = contactSpinnerMessage.getText().toString();

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
                        Toast.makeText(Contact.this, "Request for appointment successfully submitted. We'll let you know if confirmed...", Toast.LENGTH_LONG).show();
                        contactName.setText("");
                        contactEmail.setText("");
                        contactPhone.setText("");
                        contactSpinnerValue.setText("");
                        contactSpinnerMessage.setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(Contact.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
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
