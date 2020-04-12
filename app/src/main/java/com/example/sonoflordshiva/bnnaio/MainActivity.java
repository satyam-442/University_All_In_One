package com.example.sonoflordshiva.bnnaio;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sonoflordshiva.bnnaio.AsTeacher.TeacherFeedbackActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    CircleImageView imageVHead;

    private TextView navProfileName;
    private Button mainPageButton;

    private FirebaseUser fUser;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private String currentUserId;
    Dialog connectionDailog;


    TextView titleTv;
    LinearLayout messageTv;
    CardView displayConnPopupImage, syllabusPage,pptpage,videolecturespage,blogpage,questionspage,feddbackpage;

    //EditText addQuery,addSubject, addComment;
    Button btnCloseApp;

    MyReciever myReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReciever = new MyReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReciever = new MyReciever();
        registerReceiver(myReciever,intentFilter);
        /*if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else
        {
            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
        }*/

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        //currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //currentUserId = fUser.getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students").child(mAuth.getCurrentUser().getUid());

        connectionDailog = new Dialog(this);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);

        imageVHead = (CircleImageView) navView.findViewById(R.id.nav_header_profile);

        syllabusPage = (CardView) findViewById(R.id.syllabus);
        pptpage = (CardView) findViewById(R.id.ppt);

        pptpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PPTActivity();
            }
        });
        videolecturespage = (CardView) findViewById(R.id.videolectures);
        videolecturespage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoLectures();
            }
        });
        blogpage = (CardView) findViewById(R.id.blogs);
        blogpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blog();
            }
        });
        questionspage = (CardView) findViewById(R.id.questions);
        questionspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions();
            }
        });
        feddbackpage= (CardView) findViewById(R.id.feedbacks);
        feddbackpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackpage();
            }
        });

        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("Fullname"))
                    {
                        String fullname = dataSnapshot.child("Fullname").getValue().toString();
                        navProfileName.setText(fullname);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile do not exists", Toast.LENGTH_SHORT).show();
                    }
                }

                final String imageHead = dataSnapshot.child("image").getValue().toString();
                if(!imageHead.equals("default"))
                {
                    Picasso.with(MainActivity.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                    Picasso.with(MainActivity.this).load(imageHead).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.c1).into(imageVHead, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(MainActivity.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });

        syllabusPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToSemesterActivity();
            }
        });
    }

    private void feedbackpage() {
        Intent homeIntent = new Intent(this,Feedback.class);
        startActivity(homeIntent);
    }

    private void questions() {
        Intent homeIntent = new Intent(this,Questions.class);
        startActivity(homeIntent);
    }

    private void blog() {
        Intent homeIntent = new Intent(this, BlogActivity.class);
        startActivity(homeIntent);
    }

    private void videoLectures() {
        Intent homeIntent = new Intent(this,VideoLectures.class);
        startActivity(homeIntent);
    }

    private void PPTActivity() {
        Intent homeIntent = new Intent(this,PowerPoint.class);
        startActivity(homeIntent);
    }


    private void SendUserToSemesterActivity() {
        Intent homeIntent = new Intent(this,HomeYear.class);
        startActivity(homeIntent);
    }



    private Dialog buildDialog(MainActivity mainActivity)
    {
        connectionDailog.setContentView(R.layout.customalertlayout);
        //closeConnPopupImage = (ImageView) connectionDailog.findViewById(R.id.btnCloseApp);
        btnCloseApp = (Button) connectionDailog.findViewById(R.id.btnCloseApp);

        titleTv = (TextView) connectionDailog.findViewById(R.id.titleTv);
        messageTv = (LinearLayout) connectionDailog.findViewById(R.id.messageTv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(connectionDailog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        connectionDailog.show();
        connectionDailog.getWindow().setAttributes(lp);

        connectionDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        connectionDailog.show();

        btnCloseApp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //connectionDailog.finish();
                finish();
            }
        });

        connectionDailog.setCanceledOnTouchOutside(false);
        return null;
    }

    //CHECK NETWORK STATE(OPEN)
    /*
    public boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }
        else
        return false;
    }


    public AlertDialog.Builder buildDialog(Context c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                finish();
            }
        });

        return builder;
    }
    */

    //CHECK NETWORK STATE(CLOSE)

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    /*@Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistance();
        }
    }*/

    private void CheckUserExistance()
    {
        final String currentUserId = mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid()))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
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
                SendUserToMainActivity();
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
                SendToQuestionPage();
                break;
            case R.id.nav_teacher:
                SendToTeacherPage();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(this,MainActivity.class);
        startActivity(homeIntent);
    }

    private void SendToQuestionPage() {
        Intent questIntent =  new Intent(this, QuestionActivity.class);
        startActivity(questIntent);
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

    private void SendToTeacherPage() {
        Intent profileIntent = new Intent(this,TeacherActivity.class);
        startActivity(profileIntent);
    }


}
