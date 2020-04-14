package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Feedback;
import com.example.sonoflordshiva.bnnaio.MyReciever;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherFeedbackActivity extends AppCompatActivity {

    private FirebaseUser fUser;
    private FirebaseAuth mAuth;
    DatabaseReference UserRef,FeedbackRef;
    private String currentUserId,result, feedbackId;

    MyReciever myReciever;

    EditText feedbackTeacherName, feedbackTeacherSpinnerValue, feedbackTeacherSpinnerOtherOption;
    Spinner selectTeacherOptions;
    Button sendTeacherButton;
    String options[] = {"-Select Options-","Material Problem","User Interface","Service Problem","Others"};
    ArrayAdapter<String> adapter;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feedback);

        myReciever = new MyReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReciever = new MyReciever();
        registerReceiver(myReciever,intentFilter);

        mAuth = FirebaseAuth.getInstance();

        currentUserId = Prevalent.currentOnlineUser.getNamee() + Prevalent.currentOnlineUser.getPhonee();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students");
        FeedbackRef= FirebaseDatabase.getInstance().getReference().child("Feedback").child("Teachers");

        feedbackTeacherName = findViewById(R.id.feedbackTeacherName);
        feedbackTeacherSpinnerValue = findViewById(R.id.feedbackTeacherSpinnerValue);
        feedbackTeacherSpinnerOtherOption = findViewById(R.id.feedbackTeacherSpinnerOtherOption);
        selectTeacherOptions = findViewById(R.id.selectTeacherOptions);
        loadingBar = new ProgressDialog(this);

        sendTeacherButton = findViewById(R.id.sendTeacherButton);
        sendTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFeedbackToDatabase();
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,options);
        selectTeacherOptions.setAdapter(adapter);
        selectTeacherOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select Options-";
                        feedbackTeacherSpinnerValue.setText(result);
                        break;
                    case 1:
                        result = "Material Problem";
                        feedbackTeacherSpinnerValue.setText(result);
                        break;
                    case 2:
                        result = "User Interface";
                        feedbackTeacherSpinnerValue.setText(result);
                        break;
                    case 3:
                        result = "Service Problem";
                        feedbackTeacherSpinnerValue.setText(result);
                        break;
                    case 4:
                        result = "Others";
                        feedbackTeacherSpinnerValue.setText(result);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void SaveFeedbackToDatabase()
    {
        String name = feedbackTeacherName.getText().toString();
        String spinnerValue = feedbackTeacherSpinnerValue.getText().toString();
        String suggestion = feedbackTeacherSpinnerOtherOption.getText().toString();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        feedbackId = saveCurrentDate + saveCurrentTime;

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(spinnerValue) && TextUtils.isEmpty(suggestion))
        {
            Toast.makeText(this, "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            HashMap<String,Object> feedbackMap = new HashMap<String,Object>();
            feedbackMap.put("fullname", name);
            feedbackMap.put("feedbackType" , spinnerValue);
            feedbackMap.put("feedbackSuggestion",suggestion);
            feedbackMap.put("uid",currentUserId);
            feedbackMap.put("fbId",feedbackId);
            feedbackMap.put("time",saveCurrentTime);
            feedbackMap.put("date",saveCurrentDate);
            /*feedbackMap.put("Contact",contact);
            feedbackMap.put("UID",uid);
            feedbackMap.put("Password",password);
            feedbackMap.put("image","default");*/
            FeedbackRef.child(spinnerValue).child(currentUserId).child(feedbackId).updateChildren(feedbackMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SendUserToMainActivity();
                        Toast.makeText(TeacherFeedbackActivity.this, "Feedback updated Successfully...", Toast.LENGTH_LONG).show();
                        feedbackTeacherName.setText("");
                        feedbackTeacherSpinnerValue.setText("");
                        feedbackTeacherSpinnerOtherOption.setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(TeacherFeedbackActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

}
