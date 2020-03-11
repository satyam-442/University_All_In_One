package com.example.sonoflordshiva.bnnaio;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class AdvanceJava extends AppCompatActivity
{
    private PDFView advJavapdfView;
    private TextView advJavatextView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mref = database.getReference("advancejava");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_java);

        advJavapdfView = (PDFView) findViewById(R.id.adv_java_pdfview);
        //advJavatextView = (TextView) findViewById(R.id.adv_java_textview);
        mref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue(String.class);
                advJavatextView.setText(value);
                String url = advJavatextView.getText().toString();
                new RetrivePdfStream().execute(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(AdvanceJava.this, "Failed to Load", Toast.LENGTH_SHORT).show();
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
            advJavapdfView.fromStream(inputStream).load();
        }
    }
}
