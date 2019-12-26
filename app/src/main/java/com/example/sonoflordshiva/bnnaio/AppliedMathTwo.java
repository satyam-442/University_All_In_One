package com.example.sonoflordshiva.bnnaio;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

public class AppliedMathTwo extends AppCompatActivity
{
    private PDFView appmathtwopdfView;
    private TextView appmathtwotextView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mref = database.getReference("appliedtwo");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_math_two);

        appmathtwopdfView = (PDFView) findViewById(R.id.appmathtwo_pdfview);
        appmathtwotextView = (TextView) findViewById(R.id.appmathtwo_textview);
        mref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                appmathtwotextView.setText(value);
                String url = appmathtwotextView.getText().toString();
                new RetrivePdfStream().execute(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(AppliedMathTwo.this, "Failed to Load", Toast.LENGTH_SHORT).show();
            }
        });
    }
    class RetrivePdfStream extends AsyncTask<String,Void, InputStream>
    {
        @Override
        protected InputStream doInBackground(String... strings)
        {
            InputStream inputStream = null;
            try
            {
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
            appmathtwopdfView.fromStream(inputStream).load();
        }
    }
}
