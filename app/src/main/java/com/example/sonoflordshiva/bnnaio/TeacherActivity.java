package com.example.sonoflordshiva.bnnaio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherActivity extends AppCompatActivity
{

    private EditText searchInputText;
    private ImageButton searchButton;
    private RecyclerView searchResultList;
    private DatabaseReference allTeacherRefer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        searchResultList = (RecyclerView) findViewById(R.id.all_teacher_list);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchInputText = (EditText) findViewById(R.id.teacher_search_input);
        searchButton = (ImageButton) findViewById(R.id.teacher_search_button);

        allTeacherRefer = FirebaseDatabase.getInstance().getReference().child("Teachers");

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String searchText = searchInputText.getText().toString();
                SearchForTeachers(searchText);
            }
        });

    }

    //SEARCH FOR TEACHER's METHOD
    private void SearchForTeachers(String searchText)
    {
        Query searchTeacherQuery = allTeacherRefer.orderByChild("Fullname").startAt(searchText).endAt(searchText + "\uf8ff");
        //Query query = FirebaseDatabase.getInstance().getReference().child("Teachers").limitToLast(50);
        FirebaseRecyclerOptions<Teacher> options = new FirebaseRecyclerOptions.Builder<Teacher>().setQuery(searchTeacherQuery,Teacher.class).build();

        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        FirebaseRecyclerAdapter<Teacher,FindTeacherViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Teacher, FindTeacherViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull FindTeacherViewHolder holder, final int position, @NonNull Teacher model)
            {
                holder.setTeachFullname(model.getTeachFullname());
                holder.setTeachImage(getApplicationContext(),model.getTeachImage());
                holder.setTeachQualification(model.getTeachQualification());
                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();
                        Intent teacherProfileIntent = new Intent(TeacherActivity.this,TeacherProfile.class);
                        teacherProfileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(teacherProfileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public FindTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_teacher_layout,parent,false);
                return new FindTeacherViewHolder(view);
            }
        };

        searchResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public static class FindTeacherViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindTeacherViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setTeachImage(Context ctx,String image)
        {
            CircleImageView teacherImage = (CircleImageView) mView.findViewById(R.id.teacherRecyclerView_profile_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.default_avatar).into(teacherImage);
        }

        public void setTeachQualification(String qualification)
        {
            TextView merit = (TextView) mView.findViewById(R.id.teacherRecyclerView_merits);
            merit.setText(qualification);
        }

        public void setTeachFullname(String fullname)
        {
            TextView teachFullname = (TextView) mView.findViewById(R.id.teacherRecyclerView_username);
            teachFullname.setText(fullname);
        }

        public void setTeachEmail(String email)
        {
            TextView teachEmail = (TextView) mView.findViewById(R.id.teacherRecyclerView_username);
            teachEmail.setText(email);
        }
    }

    //LIST OF ALL TEACHER's METHOD
    @Override
    protected void onStart()
    {
        super.onStart();
        startListening();
    }

    private void startListening()
    {
        //Query searchTeacherQuery = allTeacherRefer.orderByChild("Fullname").startAt(searchText).endAt(searchText + "\uf8ff");
        Query query = FirebaseDatabase.getInstance().getReference().child("Teachers").limitToLast(50);
        FirebaseRecyclerOptions<Teacher> options = new FirebaseRecyclerOptions.Builder<Teacher>().setQuery(query,Teacher.class).build();


        FirebaseRecyclerAdapter<Teacher,FindTeacherViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Teacher, FindTeacherViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull FindTeacherViewHolder holder, final int position, @NonNull Teacher model)
            {
                holder.setTeachFullname(model.getTeachFullname());
                holder.setTeachImage(getApplicationContext(),model.getTeachImage());
                holder.setTeachQualification(model.getTeachQualification());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();
                        Intent teacherProfileIntent = new Intent(TeacherActivity.this,TeacherProfile.class);
                        teacherProfileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(teacherProfileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public FindTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_teacher_layout,parent,false);
                return new FindTeacherViewHolder(view);
            }
        };

        searchResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
}
