package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Aboutus;
import com.example.sonoflordshiva.bnnaio.Contact;
import com.example.sonoflordshiva.bnnaio.MainActivity;
import com.example.sonoflordshiva.bnnaio.MyReciever;
import com.example.sonoflordshiva.bnnaio.NotificationActivity;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.Profile;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.Setting;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherMainActivity extends AppCompatActivity
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
    CardView displayConnPopupImage, syllabusPage,pptpage,addVideoLecturesPage,addBlogPage,questionspage,feddbackpage;

    //EditText addQuery,addSubject, addComment;
    Button btnCloseApp;

    MyReciever myReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        myReciever = new MyReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReciever = new MyReciever();
        registerReceiver(myReciever,intentFilter);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        //currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //currentUserId = fUser.getUid();
        //UserRef= FirebaseDatabase.getInstance().getReference().child("Students").child(mAuth.getCurrentUser().getUid());

        connectionDailog = new Dialog(this);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar_teacher);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(TeacherMainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        TextView userNameTextView = navView.findViewById(R.id.nav_user_full_name);
        CircleImageView userImage = navView.findViewById(R.id.nav_header_profile);

        userNameTextView.setText(Prevalent.currentOnlineUser.getNamee());
        Picasso.with(TeacherMainActivity.this).load(Prevalent.currentOnlineUser.getImagee()).into(userImage);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });

        addVideoLecturesPage = (CardView) findViewById(R.id.addVideoLectures);
        addVideoLecturesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLectures = new Intent(TeacherMainActivity.this, AddVideoLecActivity.class);
                startActivity(addLectures);
            }
        });

        addBlogPage = (CardView) findViewById(R.id.addBlog);
        addBlogPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBlog = new Intent(TeacherMainActivity.this, AddBlogActivity.class);
                startActivity(addBlog);
            }
        });
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
                //SendUserToMainActivity();
                Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                SendUserToProfileActivity();
                Toast.makeText(this, "PROFILE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                SendUserToSettingActivity();
                Toast.makeText(this, "SETTING", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_discuss:
                //SendToQuestionPage();
                Toast.makeText(this, "DISCUSS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact:
                SendUserToContactPage();
                Toast.makeText(this, "CONTACT US", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                SendUserToAboutPage();
                Toast.makeText(this, "ABOUT US", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent logIntent = new Intent(TeacherMainActivity.this, LoginTeacher.class);
                startActivity(logIntent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void SendUserToAboutPage() {
        Intent aboutIntent =  new Intent(this, Aboutus.class);
        startActivity(aboutIntent);
    }

    private void SendUserToContactPage() {
        Intent contactIntent =  new Intent(this, TeacherContactusActivity.class);
        startActivity(contactIntent);
    }

    private void SendUserToSettingActivity() {
        Intent settingIntent =  new Intent(this, TeacherSettingsActivity.class);
        startActivity(settingIntent);
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(this, TeacherOwnProfileActivity.class);
        startActivity(profileIntent);
    }

    public void navigateToAddPptPage(View view) {
        Intent intent = new Intent(this,AddPptActivity.class);
        startActivity(intent);
    }

    public void navigateToFeedbackPage(View view) {
        Intent profileIntent = new Intent(this, TeacherFeedbackActivity.class);
        startActivity(profileIntent);
    }

}