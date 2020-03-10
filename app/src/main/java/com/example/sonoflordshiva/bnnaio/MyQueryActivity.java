package com.example.sonoflordshiva.bnnaio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyQueryActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private DatabaseReference QueryRef;
    private RecyclerView queryList;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_query);

        mAuth = FirebaseAuth.getInstance();
        QueryRef = FirebaseDatabase.getInstance().getReference().child("Query");
        currentUserId = mAuth.getCurrentUser().getUid();

        queryList = (RecyclerView) findViewById(R.id.all_stud_query_listSelf);
        queryList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        queryList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayAllMyQuery();
    }

    private void DisplayAllMyQuery()
    {
        Query query = QueryRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff");
        FirebaseRecyclerOptions<Question> options = new FirebaseRecyclerOptions.Builder<Question>().setQuery(query,Question.class).build();
        FirebaseRecyclerAdapter<Question, MyQueryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Question, MyQueryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyQueryViewHolder holder, int position, @NonNull Question model)
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
            public MyQueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_query_layout,parent,false);
                return new MyQueryViewHolder(view);
            }
        };
        queryList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    //STATIC CLASS FOR THE RETRIEVAL OF OWN QUESTION FROM DATABASE

    public static class MyQueryViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public MyQueryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
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
