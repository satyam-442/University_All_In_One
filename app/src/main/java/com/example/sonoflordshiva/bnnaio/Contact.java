package com.example.sonoflordshiva.bnnaio;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Contact extends AppCompatActivity
{
    private TextView eMail;
    private TextView pNumber;
    private TextView wAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        eMail = (TextView) findViewById(R.id.contact_email);
        eMail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                semdMail();
            }
        });
        pNumber = (TextView) findViewById(R.id.contact_number);
        pNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contactBnn();
            }
        });
        wAddress = (TextView) findViewById(R.id.contact_web);
        wAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent bnnLink = new Intent(Intent.ACTION_VIEW);
                bnnLink.setData(Uri.parse("http://bnncollege.org"));
                startActivity(bnnLink);
            }
        });
    }

    private void contactBnn()
    {
        String number = "02522230879";
        Uri call = Uri.parse("tel:" + number);
        Intent contactIntent = new Intent(Intent.ACTION_CALL,call);
        startActivity(Intent.createChooser(contactIntent,"Choose Calling Client"));
    }

    private void semdMail()
    {
        Uri uri = Uri.parse("mailto:bnncollege1966@gmail.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(intent,"Choose Mailing Client"));
    }
}
