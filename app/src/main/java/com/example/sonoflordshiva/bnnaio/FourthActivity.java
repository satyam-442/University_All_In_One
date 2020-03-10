package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FourthActivity extends AppCompatActivity
{
    private ImageView javaProgram;
    private ImageView embedSystem;
    private ImageView cost;
    private ImageView softwareEngineering;
    private ImageView computerGraphics;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        javaProgram = (ImageView) findViewById(R.id.javaprograme_icon);
        embedSystem = (ImageView) findViewById(R.id.embedsystem_icon);
        cost = (ImageView) findViewById(R.id.cost_icon);
        softwareEngineering = (ImageView) findViewById(R.id.softwareengine_icon);
        computerGraphics = (ImageView) findViewById(R.id.computergraphics_icon);

        javaProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToJavaProgram();
            }
        });
        embedSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToEmbedSystem();
            }
        });
        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToCOST();
            }
        });
        softwareEngineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSoftwareEngine();
            }
        });
        computerGraphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SenaToCompurtGraphics();
            }
        });
    }

    private void SenaToCompurtGraphics() {
        Intent cgIntent = new Intent(this,ComputerGraphics.class);
        startActivity(cgIntent);
    }
    private void SendToSoftwareEngine() {
        Intent softwareengineIntent = new Intent(this,SoftwareEngineering.class);
        startActivity(softwareengineIntent);
    }
    private void SendToCOST() {
        Intent costIntent = new Intent(this,COST.class);
        startActivity(costIntent);
    }
    private void SendToEmbedSystem() {
        Intent embedIntent = new Intent(this,EmbededSystem.class);
        startActivity(embedIntent);
    }
    private void SendToJavaProgram() {
        Intent javaIntent = new Intent(this,JavaProgramming.class);
        startActivity(javaIntent);
    }
}
