package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Profile extends AppCompatActivity
{
    private TextView userFullname, userUID,  userRollno, userContact, userEmail, userQueryAll, userQuerySolve, userQueryUnsolve;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef, myQueryRef,solveQueryRef,unsolveQueryRef;
    private FirebaseAuth mAuth;
    private LinearLayout allQueryListLay,unsolveListLay;
    String currentUserId;
    private int counterQuery = 0, unsolveQuery = 0, solveQuery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Students").child(currentUserId);
        myQueryRef = FirebaseDatabase.getInstance().getReference().child("Query");
        solveQueryRef = FirebaseDatabase.getInstance().getReference().child("Query");
        unsolveQueryRef = FirebaseDatabase.getInstance().getReference().child("UnsolveQuery");

        userFullname = (TextView) findViewById(R.id.profile_page_name);
        userRollno = (TextView) findViewById(R.id.profile_page_rollno);
        userUID = (TextView) findViewById(R.id.profile_page_uid);
        userContact = (TextView) findViewById(R.id.profile_page_contact);
        userEmail = (TextView) findViewById(R.id.profile_page_email);

        userQueryAll = (TextView) findViewById(R.id.allQueryList);
        allQueryListLay = (LinearLayout) findViewById(R.id.allQueryListLayout);
        userQuerySolve = (TextView) findViewById(R.id.solveQueryList);
        userQueryUnsolve = (TextView) findViewById(R.id.unsolveQueryList);
        unsolveListLay = (LinearLayout) findViewById(R.id.unsolveQueryLayout);


        myQueryRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff")
                .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    counterQuery = (int) dataSnapshot.getChildrenCount();
                    //userQueryAll.setText(counterQuery);
                    userQueryAll.setText(Integer.toString(counterQuery));
                }
                else
                {
                    userQueryAll.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        unsolveQueryRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            counterQuery = (int) dataSnapshot.getChildrenCount();
                            //userQueryAll.setText(counterQuery);
                            userQueryUnsolve.setText(Integer.toString(counterQuery));
                        }
                        else
                        {
                            userQueryAll.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });

        userQueryAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent allQueryIntent = new Intent(Profile.this,MyQueryActivity.class);
                startActivity(allQueryIntent);
            }
        });

        allQueryListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allQueryIntentLay = new Intent(Profile.this,MyQueryActivity.class);
                startActivity(allQueryIntentLay);
            }
        });

        unsolveListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unsolveIntentLay = new Intent(Profile.this,UnsolvedQuery.class);
                startActivity(unsolveIntentLay);
            }
        });

        userProfileImage = (CircleImageView) findViewById(R.id.profile_page_image);

        profileUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userfullname = dataSnapshot.child("Fullname").getValue().toString();
                    String userrollno = dataSnapshot.child("Roll No").getValue().toString();
                    String useruid = dataSnapshot.child("UID").getValue().toString();
                    String usercontact = dataSnapshot.child("Contact").getValue().toString();
                    String useremail = dataSnapshot.child("Email").getValue().toString();

                    userFullname.setText(userfullname);
                    userContact.setText(usercontact);
                    userRollno.setText(userrollno);
                    userUID.setText(useruid);
                    userEmail.setText(useremail);

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if(!image.equals("default"))
                    {
                        Picasso.with(Profile.this).load(image).placeholder(R.drawable.default_avatar).into(userProfileImage);
                        Picasso.with(Profile.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(userProfileImage, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {

                            }

                            @Override
                            public void onError()
                            {
                                Picasso.with(Profile.this).load(image).placeholder(R.drawable.default_avatar).into(userProfileImage);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
