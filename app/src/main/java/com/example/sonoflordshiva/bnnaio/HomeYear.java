package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeYear extends AppCompatActivity
{
    private CardView firstSem, secondSem, thirdSem, fourthSem, fifthSem, sixthSem;
    private NavigationView homeNavigationView;
    private DrawerLayout homeDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private TextView homeNavProfileName;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String currentUserId;
    CircleImageView imageVHead;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_year);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students");

        mToolbar = (Toolbar) findViewById(R.id.home_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Semesters");

        homeDrawerLayout =(DrawerLayout) findViewById(R.id.home_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeYear.this,homeDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        homeDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = homeNavigationView.inflateHeaderView(R.layout.navigation_header);
        homeNavProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);
        imageVHead = (CircleImageView) navView.findViewById(R.id.nav_header_profile);

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
                        Toast.makeText(HomeYear.this, "Profile do not exists", Toast.LENGTH_SHORT).show();
                    }
                }
                final String imageHead = dataSnapshot.child("image").getValue().toString();
                if(!imageHead.equals("default"))
                {
                    Picasso.with(HomeYear.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                    Picasso.with(HomeYear.this).load(imageHead).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.c1).into(imageVHead, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(HomeYear.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       firstSem =(CardView) findViewById(R.id.sem1);
        secondSem =(CardView) findViewById(R.id.sem2);
        thirdSem = (CardView) findViewById(R.id.sem3);
        fourthSem = (CardView) findViewById(R.id.sem4);
        fifthSem = (CardView) findViewById(R.id.sem5);
        sixthSem = (CardView) findViewById(R.id.sem6);

        firstSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToFirstSemester();

            }
        });
        secondSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToSecondSemester();
            }
        });
        thirdSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToThirdSemester();
            }
        });
        fourthSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToFourthSemester();
            }
        });
        fifthSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToFifthSemester();
            }
        });
        sixthSem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToSixthSemester();
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
        homeDrawerLayout= (DrawerLayout) findViewById(R.id.home_drawer_layout);
        if(homeDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            homeDrawerLayout.closeDrawer(GravityCompat.START);
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
        homeDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void SendUserToThirdSemester() {
        Intent thirdIntent = new Intent(this,ThirdSemester.class);
        startActivity(thirdIntent);
    }

    private void SendUserToSixthSemester() {
        Intent sixthIntent = new Intent(this,SixthSemester.class);
        startActivity(sixthIntent);
    }

    private void SendUserToFifthSemester() {
        Intent fifthIntent = new Intent(this,FifthActivity.class);
        startActivity(fifthIntent);
    }

    private void SendUserToFourthSemester() {
        Intent fourthIntent = new Intent(this,FourthActivity.class);
        startActivity(fourthIntent);
    }

    private void SendUserToSecondSemester() {
        Intent secondIntent = new Intent(this,SecondSemester.class);
        startActivity(secondIntent);
    }

    private void SendUserToFirstSemester() {
        Intent firstIntent = new Intent(this,FirstSemester.class);
        startActivity(firstIntent);
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
