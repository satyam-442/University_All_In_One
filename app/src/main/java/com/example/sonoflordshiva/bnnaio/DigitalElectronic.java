package com.example.sonoflordshiva.bnnaio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DigitalElectronic extends AppCompatActivity
{
    private PDFView pdfView;
    private TextView textView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ProgressBar bar ;
    private DatabaseReference mref = database.getReference("digitalelectronics");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_electronic);
        pdfView = (PDFView) findViewById(R.id.digital_pdfview);
        textView = (TextView) findViewById(R.id.digital_textview);
        bar = (ProgressBar) findViewById(R.id.proElect);
        mref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                textView.setText(value);
                String url = textView.getText().toString();
                new RetrivePdfStream().execute(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(DigitalElectronic.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.INVISIBLE);
            }
        });
    }
    class RetrivePdfStream extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try
            {
                //bar.setVisibility(View.VISIBLE);
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()==200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e)
                {
                    return null;
                }
                return inputStream;
            }
            @Override
            protected void onPostExecute(InputStream inputStream)
            {
                bar.setVisibility(View.GONE);
                pdfView.fromStream(inputStream).load();
            }

        @Override
        protected void onProgressUpdate(Void... values) {
            bar.setVisibility(View.INVISIBLE);
        }
    }
    }
