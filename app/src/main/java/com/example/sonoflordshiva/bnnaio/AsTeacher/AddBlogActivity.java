package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.QuestionActivity;
import com.example.sonoflordshiva.bnnaio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBlogActivity extends AppCompatActivity
{

    MaterialSearchView searchView;

    AlertDialog.Builder adb;
    Dialog commentDailog, blogDailog;
    TextView titleTv, popupQueryName, wordCount;
    LinearLayout messageTv;
    ImageView addQuestionPopup, closePopupImage,closePopupComImage, btnSendComment, addBlogPopup;
    CircleImageView popupProfilePicture;

    EditText addQuery,addBlogSubject, addComment, addBlogTitle, addBlogDescription;
    Button btnSend;

    String title,description,subject, saveCurrentDate, saveCurrentTime, current_user_id, randonName, Post_Key, descripCount="";
    DatabaseReference QueryRef ,UnsolveQueryRef, StudRef, LikesRef, CommentRef, StudRefer, BlogsRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog loadingBar;
    RecyclerView queryList, commentList, blogList;
    Boolean UpvoteChecker = false;
    private long countQuery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = Prevalent.currentOnlineUser.getNamee() + Prevalent.currentOnlineUser.getPhonee();
        /*mUser = FirebaseAuth.getInstance().getCurrentUser();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Blog");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        loadingBar = new ProgressDialog(this);

        StudRef = FirebaseDatabase.getInstance().getReference().child("Students");
        //StudRefer = FirebaseDatabase.getInstance().getReference().child("Students").child(current_user_id);
        QueryRef = FirebaseDatabase.getInstance().getReference().child("Query");
        BlogsRef = FirebaseDatabase.getInstance().getReference().child("Blogs");
        UnsolveQueryRef = FirebaseDatabase.getInstance().getReference().child("UnsolveQuery");
        //CommentRef = FirebaseDatabase.getInstance().getReference().child("Query").child(Post_Key).child("Comments");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        CommentRef = FirebaseDatabase.getInstance().getReference().child("Query").child("Comments");

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //adb = new AlertDialog.Builder(this);
        //queryDailog = adb.setView(new View(this)).create();
        blogDailog = new Dialog(this);

        addBlogTitle = (EditText) findViewById(R.id.popupBlogTitle);
        addBlogDescription = (EditText) findViewById(R.id.popupBlogDescription);
        // addComment = (EditText) findViewById(R.id.popupProfileComment);

        //btnSend = (Button) findViewById(R.id.btnSendQuery);

        addBlogPopup = (ImageView) findViewById(R.id.addBlogPopup);
        addBlogPopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowQueryPopup();
            }
        });

        //RECYCLER VIEW FOR QUERY UPDATE(OPEN)
        blogList = (RecyclerView) findViewById(R.id.all_teach_blog_list);
        blogList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        blogList.setLayoutManager(linearLayoutManager);
        //RECYCLER VIEW FOR QUERY UPDATE(CLOSE)
        //startListen();
    }

    private void ShowQueryPopup()
    {
        blogDailog.setContentView(R.layout.add_blog_layout);
        closePopupImage = (ImageView) blogDailog.findViewById(R.id.closePopupImage);
        popupProfilePicture = (CircleImageView) blogDailog.findViewById(R.id.popupProfileImage);
        popupQueryName = (TextView) blogDailog.findViewById(R.id.popupProfileName);
        addBlogTitle = (EditText) blogDailog.findViewById(R.id.popupBlogTitle);
        addBlogDescription = (EditText) blogDailog.findViewById(R.id.popupBlogDescription);
        wordCount = (TextView) blogDailog.findViewById(R.id.popupBlogWordCount);
        addBlogSubject = (EditText) blogDailog.findViewById(R.id.popupBlogSubject);
        btnSend = (Button) blogDailog.findViewById(R.id.btnSendBlog);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                title = addBlogTitle.getText().toString();
                description = addBlogDescription.getText().toString();
                subject = addBlogSubject.getText().toString();
                if(TextUtils.isEmpty(title))
                {
                    Toast.makeText(AddBlogActivity.this, "Your Question is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(description))
                {
                    Toast.makeText(AddBlogActivity.this, "Description is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(subject))
                {
                    Toast.makeText(AddBlogActivity.this, "Subject is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setMessage("please wait");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    SaveQueryToDatabase();
                }
            }
        });

        /*StudRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String fullname = dataSnapshot.child("Fullname").getValue().toString();
                popupQueryName.setText(fullname);

                final String image = dataSnapshot.child("image").getValue().toString();
                if(!image.equals("default"))
                {
                    Picasso.with(QuestionActivity.this).load(image).placeholder(R.drawable.default_avatar).into(popupProfilePicture);
                    Picasso.with(QuestionActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(popupProfilePicture, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError()
                        {
                            Picasso.with(QuestionActivity.this).load(image).placeholder(R.drawable.default_avatar).into(popupProfilePicture);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        titleTv = (TextView) blogDailog.findViewById(R.id.titleTv);
        messageTv = (LinearLayout) blogDailog.findViewById(R.id.messageTv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(blogDailog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        blogDailog.show();
        blogDailog.getWindow().setAttributes(lp);

        blogDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        blogDailog.show();

        closePopupImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                blogDailog.dismiss();
            }
        });

        blogDailog.setCanceledOnTouchOutside(false);
    }

    private void SaveQueryToDatabase()
    {
        BlogsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    countQuery = dataSnapshot.getChildrenCount();
                }
                else
                {
                    countQuery = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        Calendar callForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        randonName = current_user_id + saveCurrentDate + saveCurrentTime;

        BlogsRef.child(randonName).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final HashMap queryMap = new HashMap();
                queryMap.put("uid", current_user_id);
                queryMap.put("blogId", randonName);
                queryMap.put("date", saveCurrentDate);
                queryMap.put("time", saveCurrentTime);
                queryMap.put("title", title);
                queryMap.put("description", description);
                queryMap.put("subject", subject);
                queryMap.put("Fullname", Prevalent.currentOnlineUser.getNamee());
                queryMap.put("counter",countQuery);
                BlogsRef.child(randonName).updateChildren(queryMap).addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            SelfIntentFunction();
                            //Toast.makeText(QuestionActivity.this, "Question Submitted Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                                /*UnsolveQueryRef.child(current_user_id + randonName).updateChildren(queryMap).addOnCompleteListener(new OnCompleteListener()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            //startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                                            SelfIntentFunction();
                                            //Toast.makeText(QuestionActivity.this, "Question Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else
                                        {
                                            String mess = task.getException().getMessage();
                                            Toast.makeText(AddBlogActivity.this, "Error Occured :" + mess, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });*/
                            //startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                                /*SelfIntentFunction();
                                Toast.makeText(QuestionActivity.this, "Question Submitted Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();*/
                        }
                        else
                        {
                            /*String mess = task.getException().getMessage();
                            Toast.makeText(QuestionActivity.this, "Error Occured :" + mess, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();*/
                            String mess = task.getException().getMessage();
                            Toast.makeText(AddBlogActivity.this, "Error Occured :" + mess, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void SelfIntentFunction() {
        Intent selfIntent = new Intent(this,AddBlogActivity.class);
        selfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(selfIntent);
    }

}
