package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondSemester extends AppCompatActivity
{
    ImageView microProcessor;
    ImageView objectOriented;
    ImageView numericalStats;
    ImageView greenComputing;
    ImageView webProgramming;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_semester);

        microProcessor = (ImageView) findViewById(R.id.micro_icon);
        objectOriented = (ImageView) findViewById(R.id.oops_icon);
        numericalStats = (ImageView) findViewById(R.id.nsm_icon);
        greenComputing = (ImageView) findViewById(R.id.green_icon);
        webProgramming = (ImageView) findViewById(R.id.webprog_icon);

        microProcessor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToMicroprocessor();
            }
        });
        objectOriented.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToObjectOriented();
            }
        });
        numericalStats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToNSM();
            }
        });
        greenComputing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SendToGreenComp();
            }
        });
        webProgramming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToWebProgramming();
            }
        });
    }

    private void SendToWebProgramming() {
        Intent webIntent = new Intent(this,WebProgramming.class);
        startActivity(webIntent);
    }
    private void SendToGreenComp() {
        Intent greenIntent = new Intent(this,GreenComputing.class);
        startActivity(greenIntent);
    }
    private void SendToNSM() {
        Intent nsmIntent = new Intent(this,Numerical.class);
        startActivity(nsmIntent);
    }
    private void SendToObjectOriented() {
        Intent oopIntent = new Intent(this,ObjectOriented.class);
        startActivity(oopIntent);
    }
    private void SendToMicroprocessor() {
        Intent microIntent = new Intent(this,Microprocessor.class);
        startActivity(microIntent);
    }
}
