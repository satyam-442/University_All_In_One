package com.example.sonoflordshiva.bnnaio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Model.Blogs;
import com.example.sonoflordshiva.bnnaio.Model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ReadBlogActivity extends AppCompatActivity
{

    TextView blogRead_titleActivity, blogRead_descriptionActivity, blogRead_dateActivity, blogRead_timeActivity;
    Button saveBlogForReference;

    String blogID = "", currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference blogRefList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_blog);

        mAuth = FirebaseAuth.getInstance();
        blogID = getIntent().getStringExtra("blogId");
        currentUserId = mAuth.getCurrentUser().getUid();
        blogRefList = FirebaseDatabase.getInstance().getReference().child("SavedBlog");

        blogRead_titleActivity = (TextView) findViewById(R.id.blogRead_titleActivity);
        blogRead_descriptionActivity = (TextView) findViewById(R.id.blogRead_descriptionActivity);
        blogRead_dateActivity = (TextView) findViewById(R.id.blogRead_dateActivity);
        blogRead_timeActivity = (TextView) findViewById(R.id.blogRead_timeActivity);

        saveBlogForReference = (Button) findViewById(R.id.saveBlogForReference);
        saveBlogForReference.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveBlogToPersonalDBIdentity();
            }
        });

        getBlogFullDescription(blogID);

    }

    private void getBlogFullDescription(String blogID)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Blogs");
        productRef.child(blogID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Blogs blogs = dataSnapshot.getValue(Blogs.class);
                    blogRead_titleActivity.setText(blogs.getTitlee());
                    blogRead_descriptionActivity.setText(blogs.getDescriptionn());
                    blogRead_dateActivity.setText(blogs.getDatee());
                    blogRead_timeActivity.setText(blogs.getTimee());
                    //Picasso.with(getApplicationContext()).load(product.getImagee()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    private void SaveBlogToPersonalDBIdentity()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("blogId",blogID);
        //cartMap.put("pname",productName.getText().toString());
        //cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("title",blogRead_titleActivity.getText().toString());
        cartMap.put("description",blogRead_descriptionActivity.getText().toString());
        cartMap.put("time",saveCurrentTime);
        blogRefList.child(currentUserId).child(blogID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(ReadBlogActivity.this, "Blog saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
