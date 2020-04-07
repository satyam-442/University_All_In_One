package com.example.sonoflordshiva.bnnaio;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionActivity extends AppCompatActivity
{
    MaterialSearchView searchView;

    AlertDialog.Builder adb;
    Dialog queryDailog, commentDailog;
    TextView titleTv, popupQueryName;
    LinearLayout messageTv;
    ImageView addQuestionPopup, closePopupImage,closePopupComImage, btnSendComment;
    CircleImageView popupProfilePicture;

    EditText addQuery,addSubject, addComment;
    Button btnSend;

    String query,subject, saveCurrentDate, saveCurrentTime, current_user_id, randonName, Post_Key;
    DatabaseReference QueryRef ,UnsolveQueryRef, StudRef, LikesRef, CommentRef, StudRefer;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog loadingBar;
    RecyclerView queryList, commentList;
    Boolean LikeChecker = false;
    private long countQuery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Post_Key = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        loadingBar = new ProgressDialog(this);

        commentDailog = new Dialog(this);

        //RECYCLER VIEW FOR COMMENT UPDATE(OPEN)

        /*commentList = (RecyclerView) commentDailog.findViewById(R.id.popup_comment_list);
        commentList.setHasFixedSize(true);
        LinearLayoutManager llmComment = new LinearLayoutManager(this);
        llmComment.setReverseLayout(true);
        llmComment.setStackFromEnd(true);
        commentList.setLayoutManager(llmComment);*/

        //RECYCLER VIEW FOR COMMENT UPDATE(CLOSE)


        StudRef = FirebaseDatabase.getInstance().getReference().child("Students");
        StudRefer = FirebaseDatabase.getInstance().getReference().child("Students").child(current_user_id);
        QueryRef = FirebaseDatabase.getInstance().getReference().child("Query");
        UnsolveQueryRef = FirebaseDatabase.getInstance().getReference().child("UnsolveQuery");
        //CommentRef = FirebaseDatabase.getInstance().getReference().child("Query").child(Post_Key).child("Comments");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        CommentRef = FirebaseDatabase.getInstance().getReference().child("Query").child("Comments");

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //adb = new AlertDialog.Builder(this);
        //queryDailog = adb.setView(new View(this)).create();
        queryDailog = new Dialog(this);

        addQuery = (EditText) findViewById(R.id.popupProfileQuery);
       // addComment = (EditText) findViewById(R.id.popupProfileComment);

        //btnSend = (Button) findViewById(R.id.btnSendQuery);

        addQuestionPopup = (ImageView) findViewById(R.id.addQuestionPopup);
        addQuestionPopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowQueryPopup();
            }
        });


        //RECYCLER VIEW FOR QUERY UPDATE(OPEN)

        queryList = (RecyclerView) findViewById(R.id.all_stud_query_list);
        queryList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        queryList.setLayoutManager(linearLayoutManager);


        //RECYCLER VIEW FOR QUERY UPDATE(CLOSE)

        startListen();
    }

    //CODE TO DISPLAY ALL THE QUERY IN DATABASE
    /*Question is a Model class which is created to access the data from the database*/

    @Override
    protected void onStart() {
        super.onStart();
        startListen();
        //startListening();
    }

    /*private void startListening()
    {
        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(QueryRef,Comments.class).build();
        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> firebaseRecyclerAdapterComment = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model)
            {
                //final String PostKey = getRef(position).getKey();
                holder.setStudname(model.getStudname());
                holder.setComment(model.getComment());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comment_layout,parent,false);
                CommentsViewHolder viewHolderComment = new CommentsViewHolder(view);
                return viewHolderComment;
            }
        };

        commentList.setAdapter(firebaseRecyclerAdapterComment);
        firebaseRecyclerAdapterComment.startListening();
    }


    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView ;
        public CommentsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }
        public void setStudname(String studname)
        {
            TextView comStudName = (TextView) mView.findViewById(R.id.comment_username);
            comStudName.setText(studname);
        }

        public void setComment(String comment)
        {
            TextView comComment = (TextView) mView.findViewById(R.id.comment_text);
            comComment.setText(comment);
        }

        public void setDate(String date)
        {
            TextView comDate = (TextView) mView.findViewById(R.id.comment_date);
            comDate.setText("  Date:  " + date);
        }

        public void setTime(String time)
        {
            TextView comTime = (TextView) mView.findViewById(R.id.comment_time);
            comTime.setText("  Time:  " + time);
        }
    }*/


    //THIS IS FOR THE QUERY PAGE
    private void startListen()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Query").orderByChild("counter").limitToLast(50);
        FirebaseRecyclerOptions<Question> options = new FirebaseRecyclerOptions.Builder<Question>().setQuery(query,Question.class).build();
        FirebaseRecyclerAdapter<Question, QueryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Question, QueryViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull QueryViewHolder holder, int position, @NonNull Question model)
            {

                final String PostKey = getRef(position).getKey();

                holder.setFullnames(model.getFullnames());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setDescription(model.getDescription());
                holder.setSubject(model.getSubject());
                holder.setProfile(getApplicationContext(),model.getProfile());

                holder.setLikeButtonStatus(PostKey);

                holder.CommentPostButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //ShowCommentPopup();
                        //CHECKING THE POPUP WILL WORK OR NOT
                        Intent commentsIntent = new Intent(QuestionActivity.this, CommentsActivity.class);
                        commentsIntent.putExtra("PostKey", PostKey );
                        startActivity(commentsIntent);
                    }
                });

                holder.LikePostButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LikeChecker = true;
                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(LikeChecker.equals(true))
                                {
                                    if(dataSnapshot.child(PostKey).hasChild(current_user_id))
                                    {
                                        LikesRef.child(PostKey).child(current_user_id).removeValue();
                                        LikeChecker = false;
                                    }
                                    else
                                    {
                                        LikesRef.child(PostKey).child(current_user_id).setValue(true);
                                        LikeChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_query_layout,parent,false);
                return new QueryViewHolder(view);
            }
        };
        queryList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*And this is the static class*/
    public static class QueryViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        ImageView LikePostButton, CommentPostButton;
        TextView DisplayNoLike, DisplayNoComments;
        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;

        public QueryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            LikePostButton = (ImageView) mView.findViewById(R.id.like_btn);
            CommentPostButton = (ImageView) mView.findViewById(R.id.comment_btn);
            DisplayNoLike = (TextView) mView.findViewById(R.id.displayLikesNo);
            DisplayNoComments = (TextView) mView.findViewById(R.id.displayCommentsNo);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikeButtonStatus(final String PostKey)
        {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.like);
                        DisplayNoLike.setText((Integer.toString(countLikes)+(" Likes")));
                    }
                    else
                    {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.dislike);
                        DisplayNoLike.setText((Integer.toString(countLikes)+(" Likes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        public void setFullnames(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.post_username);
            username.setText(fullname);
        }

        public void setProfile(Context ctx,String profile)
        {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.query_profile_image);
            Picasso.with(ctx).load(profile).into(image);
        }

        public void setTime(String time)
        {
            TextView query_time = (TextView) mView.findViewById(R.id.post_time);
            query_time.setText("   " + time);
        }

        public void setDate(String date)
        {
            TextView query_date = (TextView) mView.findViewById(R.id.post_date);
            query_date.setText("        " + date);
        }

        public void setDescription(String description)
        {
            TextView query_description = (TextView) mView.findViewById(R.id.question_description);
            query_description.setText(description);
        }

        public void setSubject(String subject)
        {
            TextView query_subject = (TextView) mView.findViewById(R.id.post_subject);
            query_subject.setText("Subject: "+subject);
        }

    }

    //END OF STATIC CLASS

    private void SaveQueryToDatabase()
    {

        QueryRef.addValueEventListener(new ValueEventListener()
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

        randonName = saveCurrentDate + saveCurrentTime;

        StudRef.child(current_user_id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String studFullname = dataSnapshot.child("Fullname").getValue().toString();
                    String studProfile = dataSnapshot.child("image").getValue().toString();

                    final HashMap queryMap = new HashMap();
                    queryMap.put("uid", current_user_id);
                    queryMap.put("date", saveCurrentDate);
                    queryMap.put("time", saveCurrentTime);
                    queryMap.put("description", query);
                    queryMap.put("subject", subject);
                    queryMap.put("profile", studProfile);
                    queryMap.put("Fullname", studFullname);
                    queryMap.put("counter",countQuery);
                    QueryRef.child(current_user_id + randonName).updateChildren(queryMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                UnsolveQueryRef.child(current_user_id + randonName).updateChildren(queryMap).addOnCompleteListener(new OnCompleteListener()
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
                                            Toast.makeText(QuestionActivity.this, "Error Occured :" + mess, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });


                                //startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                                /*SelfIntentFunction();
                                Toast.makeText(QuestionActivity.this, "Question Submitted Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();*/
                            }
                            /*else
                            {
                                String mess = task.getException().getMessage();
                                Toast.makeText(QuestionActivity.this, "Error Occured :" + mess, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }*/
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void SelfIntentFunction() {
        Intent selfIntent = new Intent(this,QuestionActivity.class);
        selfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(selfIntent);
    }

    //POPUP FOR COMMENT UPLOADING
    /*private void ShowCommentPopup()
    {
        commentDailog.setContentView(R.layout.comment_layout);
        closePopupComImage = (ImageView) commentDailog.findViewById(R.id.closeCommentPopupImage);
        addComment = (EditText) commentDailog.findViewById(R.id.popupProfileComment);
        btnSendComment = (ImageView) commentDailog.findViewById(R.id.btnSendComment);

        //RECYCLER VIEW FOR COMMENT UPDATE(OPEN)

        commentList = (RecyclerView) commentDailog.findViewById(R.id.popup_comment_list);
        commentList.setHasFixedSize(true);
        LinearLayoutManager llmComment = new LinearLayoutManager(this);
        llmComment.setReverseLayout(true);
        llmComment.setStackFromEnd(true);
        commentList.setLayoutManager(llmComment);

        //RECYCLER VIEW FOR COMMENT UPDATE(CLOSE)

        btnSendComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StudRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String studName = dataSnapshot.child("Fullname").getValue().toString();
                            ValidateComment(studName);
                            addComment.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        WindowManager.LayoutParams lpc = new WindowManager.LayoutParams();
        lpc.copyFrom(queryDailog.getWindow().getAttributes());
        lpc.width = WindowManager.LayoutParams.MATCH_PARENT;
        queryDailog.show();
        queryDailog.getWindow().setAttributes(lpc);

        queryDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        queryDailog.show();

        closePopupComImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                queryDailog.dismiss();
            }
        });

        queryDailog.setCanceledOnTouchOutside(false);

    }

    private void ValidateComment(String studName)
    {
        String commentText = addComment.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "Empty Comment", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar callForDate = Calendar.getInstance();
            final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            saveCurrentDate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            saveCurrentTime = currentTime.format(callForTime.getTime());

            final String randomKey = current_user_id + saveCurrentDate + saveCurrentTime;
            HashMap commentMap = new HashMap();
            commentMap.put("UID",current_user_id);
            commentMap.put("comment",commentText);
            commentMap.put("date",saveCurrentDate);
            commentMap.put("time",saveCurrentTime);
            commentMap.put("studname",studName);

            CommentRef.child(randomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SelfIntentCommentFunction();
                        Toast.makeText(QuestionActivity.this, "Comment Send", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(QuestionActivity.this, "Error Occurred! " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }*/

    //POPUP FOR QUESTION UPLOADING
    private void ShowQueryPopup()
    {
        queryDailog.setContentView(R.layout.question_layout);
        closePopupImage = (ImageView) queryDailog.findViewById(R.id.closePopupImage);
        popupProfilePicture = (CircleImageView) queryDailog.findViewById(R.id.popupProfileImage);
        popupQueryName = (TextView) queryDailog.findViewById(R.id.popupProfileName);
        addQuery = (EditText) queryDailog.findViewById(R.id.popupProfileQuery);
        addSubject = (EditText) queryDailog.findViewById(R.id.popupProfileSubject);
        btnSend = (Button) queryDailog.findViewById(R.id.btnSendQuery);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                query = addQuery.getText().toString();
                subject = addSubject.getText().toString();
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(QuestionActivity.this, "Your Question is Empty", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(subject))
                {
                    Toast.makeText(QuestionActivity.this, "Subject is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Status");
                    loadingBar.setMessage("Submitting under progress");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    SaveQueryToDatabase();
                }
            }
        });


        StudRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        });


        titleTv = (TextView) queryDailog.findViewById(R.id.titleTv);
        messageTv = (LinearLayout) queryDailog.findViewById(R.id.messageTv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(queryDailog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        queryDailog.show();
        queryDailog.getWindow().setAttributes(lp);

        queryDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        queryDailog.show();

        closePopupImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                queryDailog.dismiss();
            }
        });

        queryDailog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

}
