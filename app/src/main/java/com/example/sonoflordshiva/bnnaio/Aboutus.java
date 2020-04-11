package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Aboutus extends AppCompatActivity
{
    private Button devPage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        /*devPage = (Button) findViewById(R.id.developer);
        devPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToDeveloperPage();
            }
        });*/
    }

    public void meetDeveloper(View view)
    {
        Intent develeoperIntent = new Intent(Aboutus.this,Developer.class);
        startActivity(develeoperIntent);
    }
}
