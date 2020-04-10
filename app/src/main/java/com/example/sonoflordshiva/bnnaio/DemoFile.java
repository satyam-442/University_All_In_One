package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sonoflordshiva.bnnaio.Model.Video;
import com.example.sonoflordshiva.bnnaio.ViewHolder.VideoViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DemoFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_file);
    }

    /*private void startListening()
    {
        //Query query = FirebaseDatabase.getInstance().getReference("VideoLectures");
        Query query = FirebaseDatabase.getInstance().getReference("Blogs").orderByChild("time").limitToFirst(50);
        //Query query = FirebaseDatabase.getInstance().getReference("VideoLectures").child(videoId).orderByChild("time").limitToLast(50);
        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>().setQuery(query,Video.class).build();
        FirebaseRecyclerAdapter<Video, VideoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, VideoViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, final int i, @NonNull final Video video)
            {
                videoViewHolder.txtBlogTitle.setText(video.getTitlee());
                videoViewHolder.txtBlogUsername.setText(video.getTeacherNamee());
                videoViewHolder.txtBlogSubject.setText(video.getSubjectt());
                videoViewHolder.txtBlogDate.setText("         " + video.getDatee());
                videoViewHolder.txtBlogTime.setText("         " + video.getTimee());

                //Picasso.with(getActivity()).load(product.getImagee()).into(productViewHolder.imageView);

                videoViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(BlogActivity.this,ReadBlogActivity.class);
                        intent.putExtra("blogId",video.getVideoIdd());
                        startActivity(intent);
                        *//*ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid",product.getPidd());
                        bundle.putString("uid",userId);
                        bundle.putString("imageUrl",product.getImagee());
                        productDetailsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container,productDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();*//*
                    }
                });
            }
            @NonNull
            @Override
            public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_blog_layout,parent,false);
                VideoViewHolder holder = new VideoViewHolder(view);
                return holder;
            }
        };
        productsRec.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }*/

}
