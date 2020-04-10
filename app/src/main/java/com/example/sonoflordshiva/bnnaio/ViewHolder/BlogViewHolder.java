package com.example.sonoflordshiva.bnnaio.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sonoflordshiva.bnnaio.Interface.ItemClickListener;
import com.example.sonoflordshiva.bnnaio.R;

public class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtBlogTitle, txtBlogUsername, txtBlogSubject, txtBlogDate,txtBlogTime;
    public ItemClickListener listener;

    public BlogViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtBlogTitle = itemView.findViewById(R.id.blog_title);
        txtBlogUsername = itemView.findViewById(R.id.blogpost_username);
        txtBlogSubject = itemView.findViewById(R.id.post_subject);
        txtBlogDate = itemView.findViewById(R.id.blogpost_date);
        txtBlogTime = itemView.findViewById(R.id.blogpost_time);
    }

    public void setItemClickedListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);
    }


}
