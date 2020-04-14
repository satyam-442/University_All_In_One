package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
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

public class TeacherOwnProfileActivity extends AppCompatActivity
{

    private TextView teacher_profilePage_name, teacher_profilePage_phone, teacher_profilePage_email, teacher_profilePage_merit;
    private CircleImageView teacher_profilePage_picture;
    private DatabaseReference profileUserRef, myQueryRef,solveQueryRef,unsolveQueryRef;
    private FirebaseAuth mAuth;
    private LinearLayout allQueryListLay,unsolveListLay;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_own_profile);

        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Teachers").child(Prevalent.currentOnlineUser.getPhonee());
        profileUserRef.keepSynced(true);

        teacher_profilePage_name = (TextView) findViewById(R.id.teacher_profilePage_name);
        teacher_profilePage_phone = (TextView) findViewById(R.id.teacher_profilePage_phone);
        teacher_profilePage_email = (TextView) findViewById(R.id.teacher_profilePage_email);
        teacher_profilePage_merit = (TextView) findViewById(R.id.teacher_profilePage_merit);
        teacher_profilePage_picture = (CircleImageView) findViewById(R.id.teacher_profilePage_picture);

        DisplayUserDetails(teacher_profilePage_name, teacher_profilePage_phone, teacher_profilePage_email, teacher_profilePage_merit, teacher_profilePage_picture);

    }

    private void DisplayUserDetails(final TextView teacher_profilePage_name, final TextView teacher_profilePage_phone, final TextView teacher_profilePage_email, final TextView teacher_profilePage_merit, final CircleImageView teacher_profilePage_picture)
    {
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("Phone").exists())
                    {
                        String name = dataSnapshot.child("Name").getValue().toString();
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        String email = dataSnapshot.child("Email").getValue().toString();
                        String qualification = dataSnapshot.child("Qualification").getValue().toString();

                        /*Picasso.with(TeacherSettingsActivity.this).load(image).into(settingTeacher_profile_image);*/
                        teacher_profilePage_name.setText(name);
                        teacher_profilePage_phone.setText(phone);
                        teacher_profilePage_email.setText(email);
                        teacher_profilePage_merit.setText(qualification);

                        final String image = dataSnapshot.child("image").getValue().toString();
                        if(!image.equals("default"))
                        {
                            Picasso.with(TeacherOwnProfileActivity.this).load(image).placeholder(R.drawable.default_avatar).into(teacher_profilePage_picture);
                            Picasso.with(TeacherOwnProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(teacher_profilePage_picture, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                { }
                                @Override
                                public void onError()
                                {
                                    Picasso.with(TeacherOwnProfileActivity.this).load(image).placeholder(R.drawable.default_avatar).into(teacher_profilePage_picture);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }
}
