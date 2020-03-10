package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FifthActivity extends AppCompatActivity
{
    private ImageView advanceJava;
    private ImageView aspNet;
    private ImageView linuxAdmin;
    private ImageView networkSecurity;
    private ImageView softwareTesting;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        advanceJava = (ImageView) findViewById(R.id.advancejava_icon);
        aspNet = (ImageView) findViewById(R.id.asp_icon);
        linuxAdmin = (ImageView) findViewById(R.id.linux_icon);
        networkSecurity = (ImageView) findViewById(R.id.networksecurity_icon);
        softwareTesting = (ImageView) findViewById(R.id.software_testing_icon);

        advanceJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToAdvanceJava();
            }
        });
        aspNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToASPNET();
            }
        });
        linuxAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToLinuxAdmin();
            }
        });
        networkSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToNetworkSecurity();
            }
        });
        softwareTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSoftwareTesting();
            }
        });
    }

    private void SendToSoftwareTesting() {
        Intent softtestingIntent = new Intent(this,SoftwareTesting.class);
        startActivity(softtestingIntent);
    }
    private void SendToNetworkSecurity() {
        Intent networkIntent = new Intent(this,NetworkSecurity.class);
        startActivity(networkIntent);
    }
    private void SendToLinuxAdmin() {
        Intent linuxIntent = new Intent(this,LinuxAdmin.class);
        startActivity(linuxIntent);
    }
    private void SendToASPNET() {
        Intent aspIntent = new Intent(this,Aspnet.class);
        startActivity(aspIntent);
    }
    private void SendToAdvanceJava() {
        Intent advanceIntent = new Intent(this,AdvanceJava.class);
        startActivity(advanceIntent);
    }
}
