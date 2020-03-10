package com.example.sonoflordshiva.bnnaio;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UnsolvedQuery extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private DatabaseReference QueryRef, SolvedQuery;
    private RecyclerView queryListUn;
    private String currentUserId,saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsolved_query);

        mAuth = FirebaseAuth.getInstance();
        QueryRef = FirebaseDatabase.getInstance().getReference().child("UnsolveQuery");
        SolvedQuery = FirebaseDatabase.getInstance().getReference().child("SolveQuery");
        currentUserId = mAuth.getCurrentUser().getUid();

        queryListUn = (RecyclerView) findViewById(R.id.all_unsolve_query_listSelf);
        queryListUn.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        queryListUn.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayUnsolveQuery();
    }


    private void DisplayUnsolveQuery()
    {
        Query query = QueryRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff");
        FirebaseRecyclerOptions<Question> options = new FirebaseRecyclerOptions.Builder<Question>().setQuery(query,Question.class).build();
        FirebaseRecyclerAdapter<Question, UnsolveQueryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Question, UnsolveQueryViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull UnsolveQueryViewHolder holder, int position, @NonNull Question model)
            {
                holder.setFullnames(model.getFullnames());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setDescription(model.getDescription());
                holder.setSubject(model.getSubject());
                holder.setProfile(getApplicationContext(),model.getProfile());
            }

            @NonNull
            @Override
            public UnsolveQueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_unsolve_query_layout,parent,false);
                return new UnsolveQueryViewHolder(view);
            }
        };
        queryListUn.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*private void MoveToSolveQuery()
    {
        QueryRef.child(currentUserId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(UnsolvedQuery.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    //STATIC CLASS FOR THE RETRIEVAL OF OWN QUESTION FROM DATABASE
    public static class UnsolveQueryViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageView upvote;
        DatabaseReference UnsolveQuery, SolvedQuery;
        String currentUserId;
        FirebaseAuth mAuth;

        public UnsolveQueryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mAuth = FirebaseAuth.getInstance();
            UnsolveQuery = FirebaseDatabase.getInstance().getReference().child("UnsolveQuery");
            SolvedQuery = FirebaseDatabase.getInstance().getReference().child("SolveQuery");
            currentUserId = mAuth.getCurrentUser().getUid();
            upvote = (ImageView) mView.findViewById(R.id.upvote_btn);
            upvote.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MoveToSolveQuery();
                }
            });

        }

        public void MoveToSolveQuery()
        {
            UnsolveQuery.child(currentUserId + "\uf8ff").removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        UnsolveQuery.child(currentUserId + "\uf8ff").removeValue();
                    }
                }
            });
        }

        public void setFullnames(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.post_username);
            username.setText(fullname);
        }

        public void setProfile(Context ctx, String profile)
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
}
