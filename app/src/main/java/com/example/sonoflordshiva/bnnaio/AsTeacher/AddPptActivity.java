package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPptActivity extends AppCompatActivity
{
    Button selectPdfFile, uploadPdfFile;
    TextView selectFileName;
    EditText selectFileSubject;
    DatabaseReference uploadPdfRef;
    StorageReference uploadPdfStorageRef;
    int PDF_CODE = 1;
    Uri pdfUri;
    StorageTask uploadTask;
    String uploadPptId;
    ProgressDialog loadingBar;
    PDFView uploadPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ppt);

        //uploadPdfRef = FirebaseDatabase.getInstance().getReference().child("UploadsPpt");
        uploadPdfStorageRef = FirebaseStorage.getInstance().getReference().child("UploadsPpt");

        loadingBar = new ProgressDialog(this);

        selectPdfFile = (Button) findViewById(R.id.selectPdfFile);
        selectPdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(AddPptActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    SelectFileFromStorage();
                }
                else
                {
                    ActivityCompat.requestPermissions(AddPptActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
                /*if (ContextCompat.checkSelfPermission(AddPptActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    //SelectFileFromStorage();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddPptActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
                    {
                        Toast.makeText(AddPptActivity.this, "permission granted...", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    ActivityCompat.requestPermissions(AddPptActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }*/
            }
        });

        uploadPdfFile = (Button) findViewById(R.id.uploadPdfFile);
        uploadPdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (pdfUri != null)
                {
                    UploadPdfToFirebaseStorage(pdfUri);
                }
                else
                {
                    Toast.makeText(AddPptActivity.this, "please select file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectFileName = (TextView) findViewById(R.id.selectFileName);
        selectFileSubject = (EditText) findViewById(R.id.selectFileSubject);
        uploadPdfView = (PDFView) findViewById(R.id.uploadPdfView);
    }

    private void UploadPdfToFirebaseStorage(final Uri pdfUri)
    {
        loadingBar.setMessage("please wait...");
        loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //loadingBar.setProgress(0);
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        uploadPptId = saveCurrentDate + saveCurrentTime;

        final StorageReference fileReference = uploadPdfStorageRef.child(getFileName(pdfUri));
        //uploadPdfStorageRef.child(getFileName(pdfUri)).putFile(pdfUri);
        uploadTask = fileReference.putFile(pdfUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();
                    //String current_uid = mAuth.getUid();
                    uploadPdfRef = FirebaseDatabase.getInstance().getReference("UploadsPpt").child(uploadPptId);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("pdf", mUri);
                    map.put("teacherName", Prevalent.currentOnlineUser.getNamee());
                    map.put("subject", selectFileSubject.getText().toString());
                    map.put("date", saveCurrentDate);
                    map.put("time", saveCurrentTime);
                    map.put("pptId", uploadPptId);
                    uploadPdfRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToTeacherMainActivity();
                                Toast.makeText(AddPptActivity.this, "PPT Uploaded Successfully...", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(AddPptActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(AddPptActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                Toast.makeText(AddPptActivity.this, "Error Occurred!" + exception, Toast.LENGTH_SHORT).show();
            }
        });
        /*.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            }
        });*/
    }

    private void SendUserToTeacherMainActivity() {
        Intent intent = new Intent(this,TeacherMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            SelectFileFromStorage();
        }
        else
        {
            Toast.makeText(this, "please provide the storage permission...", Toast.LENGTH_LONG).show();
        }
    }

    private void SelectFileFromStorage()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_CODE && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            selectFileName.setText("You selected:- " + data.getData().getLastPathSegment());
            //uploadPdfView.fromAsset("client.pdf").load();
            uploadPdfView.fromUri(pdfUri).load();
        } else {
            Toast.makeText(this, "Please select file", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileName(Uri uri)
    {
        String result = null;
        if (uri.getScheme().equals("content"))
        {
            Cursor cursor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(uri, null, null, null);
            }
            try
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
            {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
