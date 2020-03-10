package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class CommentsActivity extends AppCompatActivity
{
    RecyclerView CommentLists;
    ImageView PostCommentBtn;
    EditText CommentInputText;
    String Post_Key, current_user_id, saveCurrentDate, saveCurrentTime;
    DatabaseReference StudRef, QueryRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Post_Key = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        StudRef = FirebaseDatabase.getInstance().getReference().child("Students");
        QueryRef = FirebaseDatabase.getInstance().getReference().child("Query").child(Post_Key).child("Comments");

        current_user_id = mAuth.getCurrentUser().getUid();

        CommentLists = (RecyclerView) findViewById(R.id.comment_list);
        CommentLists.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentLists.setLayoutManager(linearLayoutManager);

        PostCommentBtn = (ImageView) findViewById(R.id.post_comment_btn);
        CommentInputText = (EditText) findViewById(R.id.comment_input);

        PostCommentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StudRef.child(current_user_id).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            String studName = dataSnapshot.child("Fullname").getValue().toString();
                            ValidateComment(studName);
                            CommentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        startListening();
    }


    private void startListening()
    {
        //Query query = FirebaseDatabase.getInstance().getReference().child("Query").child(Post_Key).child("Comments").limitToLast(50);
        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(QueryRef,Comments.class).build();
        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model)
            {
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
                CommentsViewHolder viewHolder = new CommentsViewHolder(view);
                return viewHolder;
            }
        };

        CommentLists.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
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
    }

    private void ValidateComment(String studName)
    {
        String commentText = CommentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "Comment can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar callForDate = Calendar.getInstance();
            final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            saveCurrentDate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(callForTime.getTime());

            final String randomKey = current_user_id + saveCurrentDate + saveCurrentTime;
            HashMap commentMap = new HashMap();
            commentMap.put("UID",current_user_id);
            commentMap.put("comment",commentText);
            commentMap.put("date",saveCurrentDate);
            commentMap.put("time",saveCurrentTime);
            commentMap.put("studname",studName);

            QueryRef.child(randomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        //SelfIntentCommentFunction();
                        Toast.makeText(CommentsActivity.this, "Comment Send", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(CommentsActivity.this, "Error Occurred! " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void SelfIntentCommentFunction()
    {
        Intent selfIntent = new Intent(this,CommentsActivity.class);
        selfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(selfIntent);
    }
}
