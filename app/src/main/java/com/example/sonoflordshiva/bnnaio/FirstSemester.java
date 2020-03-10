package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstSemester extends AppCompatActivity
{
    private ImageView digitalElectronics;
    private ImageView communicationSkills;
    private ImageView imperativeProgramming;
    private ImageView discreteMaths;
    private ImageView operatingSystem;

    TextView mytv;
    Typeface myfont;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_semester);
        mytv = (TextView) findViewById(R.id.tv_digital);
        myfont = Typeface.createFromAsset(this.getAssets(),"fonts/glyphicons.ttf");
        mytv.setTypeface(myfont);

        digitalElectronics = (ImageView) findViewById(R.id.digi_electronics);
        communicationSkills = (ImageView) findViewById(R.id.communication_skill);
        imperativeProgramming = (ImageView) findViewById(R.id.imperative_programming);
        discreteMaths = (ImageView) findViewById(R.id.descrete_maths);
        operatingSystem = (ImageView) findViewById(R.id.operating_system);

        digitalElectronics.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToDigitalElectronics();
            }
        });

        communicationSkills.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToCommunicationSkill();
            }
        });

        imperativeProgramming.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToImperativeProgramming();
            }
        });

        discreteMaths.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToDescreteMath();

            }
        });

        operatingSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToOperatingSystem();
            }
        });
    }

    private void SendToDescreteMath() {
        Intent discreteIntent = new Intent(FirstSemester.this,DiscreteMath.class);
        startActivity(discreteIntent);
    }

    private void SendToOperatingSystem() {
        Intent opeartingIntent = new Intent(FirstSemester.this,OperatingSystem.class);
        startActivity(opeartingIntent);
    }

    private void SendToImperativeProgramming()
    {
        Intent imperativeIntent = new Intent(FirstSemester.this,ImperativeProgramming.class);
        startActivity(imperativeIntent);
    }

    private void SendToCommunicationSkill()
    {
        Intent communicationIntent = new Intent(FirstSemester.this,CommunicationSkill.class);
        startActivity(communicationIntent);
    }

    private void SendToDigitalElectronics()
    {
        Intent digitalIntent = new Intent(FirstSemester.this,DigitalElectronic.class);
        startActivity(digitalIntent);
    }
}
