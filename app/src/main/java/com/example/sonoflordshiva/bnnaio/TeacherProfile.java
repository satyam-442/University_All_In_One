package com.example.sonoflordshiva.bnnaio;

import android.provider.CalendarContract;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends AppCompatActivity
{

    private TextView userTeachFullname,  userTeachMerit, userTeachEmail, userTeachBlogPost, userTeachBlogFollower;
    private CircleImageView userTeachProfileImage;
    private Button blogFollowBtn;
    private DatabaseReference profileTeachUserRef, StudFollowingRef;
    private FirebaseAuth mAuth;
    String currentUserId, senderUserId, recieverUserId, CURRENT_STATE, saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        mAuth = FirebaseAuth.getInstance();

        currentUserId = getIntent().getExtras().get("visit_user_id").toString();
        senderUserId = mAuth.getCurrentUser().getUid();
        profileTeachUserRef = FirebaseDatabase.getInstance().getReference().child("Teachers").child(currentUserId);

        StudFollowingRef = FirebaseDatabase.getInstance().getReference().child("StudentFollowing");

        //InitializeFieldset();

        userTeachFullname = (TextView) findViewById(R.id.teacher_profilePage_name);
        userTeachMerit = (TextView) findViewById(R.id.teacher_profilePage_merit);
        userTeachEmail = (TextView) findViewById(R.id.teacher_profilePage_email);
        userTeachBlogPost = (TextView) findViewById(R.id.teacher_profilePage_blogPost);
        userTeachBlogFollower = (TextView) findViewById(R.id.teacher_profilePage_blogFollower);
        userTeachProfileImage = (CircleImageView) findViewById(R.id.teacher_profilePage_picture);
        CURRENT_STATE = "not_friends";

        blogFollowBtn = (Button) findViewById(R.id.teacher_profilePage_button);

        profileTeachUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userTeachfullname = dataSnapshot.child("Fullname").getValue().toString();
                    //String userrollno = dataSnapshot.child("Roll No").getValue().toString();
                    //String useruid = dataSnapshot.child("UID").getValue().toString();
                    String userTeachmerit = dataSnapshot.child("Qualification").getValue().toString();
                    String userTeachemail = dataSnapshot.child("Email").getValue().toString();

                    userTeachFullname.setText(userTeachfullname);
                    userTeachMerit.setText(userTeachmerit);
                    //userTeachBlogFollower.setText(userrollno);
                    //userTeachBlogPost.setText(useruid);
                    userTeachEmail.setText(userTeachemail);

                    MaintainanceOfButton();

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if(!image.equals("default"))
                    {
                        Picasso.with(TeacherProfile.this).load(image).placeholder(R.drawable.default_avatar).into(userTeachProfileImage);
                        Picasso.with(TeacherProfile.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(userTeachProfileImage, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {

                            }

                            @Override
                            public void onError()
                            {
                                Picasso.with(TeacherProfile.this).load(image).placeholder(R.drawable.default_avatar).into(userTeachProfileImage);
                            }
                        });
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blogFollowBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                blogFollowBtn.setEnabled(false);
                if(CURRENT_STATE.equals("not_friends"))
                {
                    StartFollowingTheTeacher();
                }
                if(CURRENT_STATE.equals("friends"))
                {
                    UnfollowTeacher();
                }
            }
        });

    }

    private void UnfollowTeacher()
    {
        StudFollowingRef.child(senderUserId).child(currentUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    StudFollowingRef.child(currentUserId).child(senderUserId)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                blogFollowBtn.setEnabled(true);
                                CURRENT_STATE = "not_friends";
                                blogFollowBtn.setText("F O L L O W");
                            }
                        }
                    });
                }
            }
        });
    }

    private void MaintainanceOfButton()
    {
        StudFollowingRef.child(senderUserId).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("date"))
                {

                    blogFollowBtn.setText("F O L L O W I N G");

                    //String Date = dataSnapshot.child("date").getValue().toString();
                    //if(Date.equals("date"))
                    //{
                    //    CURRENT_STATE = "request_sent";
                    //    blogFollowBtn.setText("F O L L O W I N G");
                    //}

                    /*String request_type = dataSnapshot.child(currentUserId).child("date").getValue().toString();
                    if(request_type.equals("date"))
                    {
                        CURRENT_STATE = "request_sent";
                        blogFollowBtn.setText("F O L L O W I N G");
                    }
                    else if(request_type.equals("received"))
                    {
                        CURRENT_STATE = "request_received";
                        blogFollowBtn.setText("");
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void StartFollowingTheTeacher()
    {
        Calendar callForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        StudFollowingRef.child(senderUserId).child(currentUserId).child("date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    StudFollowingRef.child(currentUserId).child(senderUserId).child("date").setValue(saveCurrentDate)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                blogFollowBtn.setEnabled(true);
                                CURRENT_STATE = "friends";
                                blogFollowBtn.setText("F O L L O W I N G");
                            }
                        }
                    });
                }
            }
        });





        /*StudFollowRef.child(senderUserId).child(currentUserId).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    StudFollowRef.child(currentUserId).child(senderUserId).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                blogFollowBtn.setEnabled(true);
                                CURRENT_STATE = "request_sent";
                                blogFollowBtn.setText("F O L L O W I N G");
                            }
                        }
                    });
                }
            }
        });*/
    }

    private void InitializeFieldset()
    {
        Calendar callForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        StudFollowingRef.child(senderUserId).child(currentUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    StudFollowingRef.child(currentUserId).child(senderUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                blogFollowBtn.setEnabled(true);
                                CURRENT_STATE = "friends";
                                blogFollowBtn.setText("F O L L O W I N G");
                            }
                        }
                    });
                }
            }
        });
    }
}
