package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sonoflordshiva.bnnaio.Model.Blogs;
import com.example.sonoflordshiva.bnnaio.Model.Video;
import com.example.sonoflordshiva.bnnaio.ViewHolder.BlogViewHolder;
import com.example.sonoflordshiva.bnnaio.ViewHolder.VideoViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout blogDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView homeNavigationView;
    private TextView homeNavProfileName;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currentUserId;
    CircleImageView imageVHead;

    DatabaseReference UserRefTwo, productRef;
    MyReciever myReciever;
    RecyclerView productsRec;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Students");


        mToolbar = (Toolbar) findViewById(R.id.blog_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Blogs");

        blogDrawerLayout =(DrawerLayout) findViewById(R.id.blog_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(BlogActivity.this,blogDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        blogDrawerLayout.addDrawerListener(actionBarDrawerToggle);
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
                        Toast.makeText(BlogActivity.this, "Profile do not exists", Toast.LENGTH_SHORT).show();
                    }
                }

                final String imageHead = dataSnapshot.child("image").getValue().toString();
                if(!imageHead.equals("default"))
                {
                    Picasso.with(BlogActivity.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
                    Picasso.with(BlogActivity.this).load(imageHead).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.c1).into(imageVHead, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(BlogActivity.this).load(imageHead).placeholder(R.drawable.c1).into(imageVHead);
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

        productsRec = findViewById(R.id.blogRecView);
        productsRec.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        productsRec.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListening();
    }

    private void startListening()
    {
        //Query query = FirebaseDatabase.getInstance().getReference("VideoLectures");
        Query query = FirebaseDatabase.getInstance().getReference("Blogs")/*.orderByChild("time")*/.limitToFirst(50);
        //Query query = FirebaseDatabase.getInstance().getReference("VideoLectures").child(videoId).orderByChild("time").limitToLast(50);
        FirebaseRecyclerOptions<Blogs> options = new FirebaseRecyclerOptions.Builder<Blogs>().setQuery(query,Blogs.class).build();
        FirebaseRecyclerAdapter<Blogs, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogs, BlogViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder blogViewHolder, final int i, @NonNull final Blogs blogs)
            {
                blogViewHolder.txtBlogTitle.setText(blogs.getTitlee());
                blogViewHolder.txtBlogUsername.setText(blogs.getFullnamee());
                blogViewHolder.txtBlogSubject.setText(blogs.getSubjectt());
                blogViewHolder.txtBlogDate.setText("         " + blogs.getDatee());
                blogViewHolder.txtBlogTime.setText("         " + blogs.getTimee());

                //Picasso.with(getActivity()).load(product.getImagee()).into(productViewHolder.imageView);

                blogViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(BlogActivity.this,ReadBlogActivity.class);
                        intent.putExtra("blogId",blogs.getBlogIdd());
                        startActivity(intent);
                        /*ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid",product.getPidd());
                        bundle.putString("uid",userId);
                        bundle.putString("imageUrl",product.getImagee());
                        productDetailsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container,productDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();*/
                    }
                });
            }
            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_blog_layout,parent,false);
                BlogViewHolder holder = new BlogViewHolder(view);
                return holder;
            }
        };
        productsRec.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        blogDrawerLayout= (DrawerLayout) findViewById(R.id.blog_drawer_layout);
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
