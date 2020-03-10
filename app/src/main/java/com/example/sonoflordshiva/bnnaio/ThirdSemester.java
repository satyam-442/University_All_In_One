package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdSemester extends AppCompatActivity
{
    private ImageView pythonProgramming;
    private ImageView dataStructure;
    private ImageView computerNetwork;
    private ImageView databaseManagement;
    private ImageView appliedMathone;
    private ImageView appliedMathtwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_semester);
        
        pythonProgramming = (ImageView) findViewById(R.id.python_icon);
        dataStructure = (ImageView) findViewById(R.id.datastructure_icon);
        computerNetwork = (ImageView) findViewById(R.id.computernetwork_icon);
        databaseManagement = (ImageView)findViewById(R.id.dbms_icon);
        appliedMathone = (ImageView) findViewById(R.id.applied_math_one_icon);
        appliedMathtwo = (ImageView) findViewById(R.id.applied_math_two_icon);
        
        pythonProgramming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToPythonProgramming();
            }
        });
        dataStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToDataStructure();
            }
        });
        computerNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToComputerNetwork();
            }
        });
        databaseManagement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToDatabaseManagement();
            }
        });
        appliedMathone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToAppliedMathOne();
            }
        });
        appliedMathtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToAppliedMathTwo();
            }
        });
    }

    private void SendToAppliedMathTwo() {
        Intent appliedTwoIntent = new Intent(this,AppliedMathTwo.class);
        startActivity(appliedTwoIntent);
    }
    private void SendToAppliedMathOne() {
        Intent appliedOneIntent = new Intent(this,AppliedMath.class);
        startActivity(appliedOneIntent);
    }
    private void SendToDatabaseManagement() {
        Intent databseIntent = new Intent(this,DatabaseManagement.class);
        startActivity(databseIntent);
    }
    private void SendToComputerNetwork() {
        Intent computerNetworkIntent = new Intent(this,ComputerNetwork.class);
        startActivity(computerNetworkIntent);
    }
    private void SendToDataStructure() {
        Intent datastructIntent = new Intent(this,DataStructure.class);
        startActivity(datastructIntent);
    }
    private void SendToPythonProgramming() {
        Intent pythonIntent = new Intent(this,PythonPrograme.class);
        startActivity(pythonIntent);
    }
}
