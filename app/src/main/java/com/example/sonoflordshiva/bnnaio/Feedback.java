package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Feedback extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout blogDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView homeNavigationView;
    private TextView homeNavProfileName;
    FirebaseAuth mAuth;
    DatabaseReference UserRef,FeedbackRef;
    String currentUserId, result, feedbackId;
    CircleImageView imageVHead;

    EditText feedbackName, feedbackSpinnerValue, feedbackSpinnerOtherOption;
    Spinner selectOptions;
    Button sendButton;
    String options[] = {"-Select Options-","Material Problem","User Interface","Service Problem","Others"};
    ArrayAdapter <String> adapter;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students");
        FeedbackRef= FirebaseDatabase.getInstance().getReference().child("Feedback").child("Students");

        mToolbar = (Toolbar) findViewById(R.id.feedback_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Feedbacks");

        blogDrawerLayout =(DrawerLayout) findViewById(R.id.feedback_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(Feedback.this,blogDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        blogDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = homeNavigationView.inflateHeaderView(R.layout.navigation_header);
        homeNavProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);
        imageVHead = (CircleImageView) navView.findViewById(R.id.nav_header_profile);

        //FEEDBACK VALIDATIONS
        feedbackName = findViewById(R.id.feedbackName);
        feedbackSpinnerValue = findViewById(R.id.feedbackSpinnerValue);
        feedbackSpinnerOtherOption = findViewById(R.id.feedbackSpinnerOtherOption);
        selectOptions = findViewById(R.id.selectOptions);
        loadingBar = new ProgressDialog(this);

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFeedbackToDatabase();
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,options);
        selectOptions.setAdapter(adapter);
        selectOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        result = "-Select Options-";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 1:
                        result = "Material Problem";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 2:
                        result = "User Interface";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 3:
                        result = "Service Problem";
                        feedbackSpinnerValue.setText(result);
                        break;
                    case 4:
                        result = "Others";
                        feedbackSpinnerValue.setText(result);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        UserRef.child(currentUserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("Fullname"))
                    {
                        String fullname = dataSnapshot.child("Fullname").getValue().toString();
                        homeNavProfileName.setText(fullname);
                    }
                    else
                    {
                        Toast.makeText(Feedback.this, "Profile do not exists", Toast.LENGTH_SHORT).show();
                    }
                }


                final String imageHead = dataSnapshot.child("image").getValue().toString();
                if(!imageHead.equals("default"))
                {
                    Picasso.with(Feedback.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                    Picasso.with(Feedback.this).load(imageHead).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.c1).into(imageVHead, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(Feedback.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        homeNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });

    }

    private void SaveFeedbackToDatabase()
    {
        String name = feedbackName.getText().toString();
        String spinnerValue = feedbackSpinnerValue.getText().toString();
        String suggestion = feedbackSpinnerOtherOption.getText().toString();

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
                        Toast.makeText(Feedback.this, "Feedback updated Successfully...", Toast.LENGTH_LONG).show();
                        feedbackName.setText("");
                        feedbackSpinnerValue.setText("");
                        feedbackSpinnerOtherOption.setText("");
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(Feedback.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        blogDrawerLayout= (DrawerLayout) findViewById(R.id.feedback_drawer_layout);
        if(blogDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            blogDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //DRAWER MENU ACTIVITY
    private void UserMenuSelector(MenuItem item)
    {
        switch ( item.getItemId())
        {
            case R.id.nav_home:
                SendUserToHomeActivity();
                break;
            case R.id.nav_profile:
                SendUserToProfileActivity();
                break;
            case R.id.nav_notification:
                SendUserToNotificationActivity();
                break;
            case R.id.nav_setting:
                SendUserToSettingActivity();
                break;
            case R.id.nav_contact:
                SendUserToContactPage();
                break;
            case R.id.nav_about:
                SendUserToAboutPage();
                break;
            case R.id.nav_discuss:
                SendUserToDiscussPage();
                break;
            case R.id.nav_teacher:
                SendToTeacherPage();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
        blogDrawerLayout.closeDrawer(GravityCompat.START);

    }

    private void SendUserToDiscussPage() {
        Intent discussIntent = new Intent(this, QuestionActivity.class);
        startActivity(discussIntent);
    }

    private void SendUserToAboutPage() {
        Intent aboutIntent =  new Intent(this,Aboutus.class);
        startActivity(aboutIntent);
    }

    private void SendUserToContactPage() {
        Intent contactIntent =  new Intent(this,Contact.class);
        startActivity(contactIntent);
    }

    private void SendUserToNotificationActivity() {
        Intent notifyIntent =  new Intent(this,NotificationActivity.class);
        startActivity(notifyIntent);
    }

    private void SendUserToSettingActivity() {
        Intent settingIntent =  new Intent(this,Setting.class);
        startActivity(settingIntent);
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(this,Profile.class);
        startActivity(profileIntent);
    }

    private void SendUserToHomeActivity() {
        Intent homeIntent = new Intent(this,MainActivity.class);
        startActivity(homeIntent);
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendToTeacherPage() {
        Intent profileIntent = new Intent(this,TeacherActivity.class);
        startActivity(profileIntent);
    }
}

