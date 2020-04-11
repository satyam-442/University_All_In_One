package com.example.sonoflordshiva.bnnaio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class ViewVideoLecturesActivity extends AppCompatActivity
{

    TextView videoView_titleActivity, videoView_descriptionActivity, videoView_dateActivity, videoView_timeActivity;
    Button saveVideoForReference;
    VideoView videoView_showVideoActivity;
    MediaController videoPreviewController;

    String videoID = "", currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference videoLecRefList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_lectures);

        mAuth = FirebaseAuth.getInstance();
        videoID = getIntent().getStringExtra("videoID");
        currentUserId = mAuth.getCurrentUser().getUid();
        videoLecRefList = FirebaseDatabase.getInstance().getReference().child("SavedVideoLectures");

        videoView_titleActivity = (TextView) findViewById(R.id.videoView_titleActivity);
        videoView_descriptionActivity = (TextView) findViewById(R.id.videoView_descriptionActivity);
        videoView_dateActivity = (TextView) findViewById(R.id.videoView_dateActivity);
        videoView_timeActivity = (TextView) findViewById(R.id.videoView_timeActivity);
        videoView_showVideoActivity = (VideoView) findViewById(R.id.videoView_showVideoActivity);

        videoPreviewController = new MediaController(this);
        videoView_showVideoActivity.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener()
                {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
                    {
                        videoView_showVideoActivity.setMediaController(videoPreviewController);
                        videoPreviewController.setAnchorView(videoView_showVideoActivity);
                    }
                });
            }
        });
        videoView_showVideoActivity.start();

        /*saveVideoForReference = (Button) findViewById(R.id.saveVideoForReference);
        saveVideoForReference.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveVideoLecToPersonalDBIdentity();
            }
        });*/

        getBlogFullDescription(videoID);

    }

    private void getBlogFullDescription(String videoID)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("VideoLectures");
        productRef.child(videoID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Video video = dataSnapshot.getValue(Video.class);
                    videoView_titleActivity.setText(video.getTitlee());
                    videoView_descriptionActivity.setText(video.getDescriptionn());
                    videoView_dateActivity.setText(video.getDatee());
                    videoView_timeActivity.setText(video.getTimee());
                    videoView_showVideoActivity.setVideoPath(video.getVideoo());
                    //Picasso.with(getApplicationContext()).load(product.getImagee()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    /*private void SaveVideoLecToPersonalDBIdentity()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("videoId",videoID);
        //cartMap.put("pname",productName.getText().toString());
        //cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("title",videoView_titleActivity.getText().toString());
        cartMap.put("description",videoView_descriptionActivity.getText().toString());
        cartMap.put("time",saveCurrentTime);
        cartMap.put("video",videoView_showVideoActivity);
        videoLecRefList.child(currentUserId).child(videoID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
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
    }*/
}
