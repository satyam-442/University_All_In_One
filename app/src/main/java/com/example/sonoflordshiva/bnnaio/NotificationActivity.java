package com.example.sonoflordshiva.bnnaio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class NotificationActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private RecyclerView notifyList;

    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mToolbar = (Toolbar) findViewById(R.id.notify_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notification");

        myApplication = MyApplication.getInstance();

        notifyList = (RecyclerView) findViewById(R.id.all_notification);
        notifyList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        notifyList.setLayoutManager(linearLayoutManager);


        DisplayAllNotifications();
    }

    private void DisplayAllNotifications()
    {
        
    }
}
