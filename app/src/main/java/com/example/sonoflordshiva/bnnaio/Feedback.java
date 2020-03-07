package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedback extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout blogDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView homeNavigationView;
    private TextView homeNavProfileName;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students");


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

