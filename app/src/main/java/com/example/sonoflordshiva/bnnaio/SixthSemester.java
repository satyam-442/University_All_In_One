package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SixthSemester extends AppCompatActivity
{
    private ImageView businessIntelligentOne;
    private ImageView businessIntelligentTwo;
    private ImageView securityComputingOne;
    private ImageView securityComputingTwo;
    private ImageView softwareQualityOne;
    private ImageView softwareQualityTwo;
    private ImageView gis;
    private ImageView itServiceMgmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth_semester);

        businessIntelligentOne = (ImageView) findViewById(R.id.businessintelligent_icon);
        businessIntelligentTwo = (ImageView) findViewById(R.id.businessintelligent_two_icon);
        securityComputingOne = (ImageView) findViewById(R.id.securitycomputing_icon);
        securityComputingTwo = (ImageView) findViewById(R.id.securitycomputing_two_icon);
        softwareQualityOne = (ImageView) findViewById(R.id.softwarequality_icon);
        softwareQualityTwo = (ImageView) findViewById(R.id.softwarequality_two_icon);
        gis = (ImageView) findViewById(R.id.gis_icon);
        itServiceMgmt = (ImageView) findViewById(R.id.itservice_icon);

        businessIntelligentOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToBIone();
            }
        });
        businessIntelligentTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToBItwo();
            }
        });
        securityComputingOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSecurityComOne();
            }
        });
        securityComputingTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSecurityComTwo();
            }
        });
        softwareQualityOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSoftwareQualityOne();
            }
        });
        softwareQualityTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToSoftwareQualityTwo();
            }
        });
        gis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToGIS();
            }
        });
        itServiceMgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToITMgnt();
            }
        });
    }

    private void SendToITMgnt() {
        Intent itServiceIntent = new Intent(this,ItService.class);
        startActivity(itServiceIntent);
    }

    private void SendToGIS() {
        Intent gisIntent = new Intent(this,GIS.class);
        startActivity(gisIntent);
    }

    private void SendToSoftwareQualityTwo() {
        Intent swQualityTwoIntent = new Intent(this,SoftwareQualityTwo.class);
        startActivity(swQualityTwoIntent);
    }
    private void SendToSoftwareQualityOne() {
        Intent swQualityOneIntent = new Intent(this,SoftwareQuality.class);
        startActivity(swQualityOneIntent);
    }
    private void SendToSecurityComTwo() {
        Intent secComputTwoIntent = new Intent(this,SecurityComputingTwo.class);
        startActivity(secComputTwoIntent);
    }
    private void SendToSecurityComOne() {
        Intent secComputOneIntent = new Intent(this,SecurityComputing.class);
        startActivity(secComputOneIntent);
    }
    private void SendToBItwo() {
        Intent bitwoIntent = new Intent(this,BusinessIntelligenceTwo.class);
        startActivity(bitwoIntent);
    }
    private void SendToBIone() {
        Intent bioneIntent = new Intent(this,BusinessIntelligence.class);
        startActivity(bioneIntent);
    }
}
