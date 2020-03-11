package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.Setting;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddVideoLecActivity extends AppCompatActivity
{

    private static final int PICK_VIDEO_INTENT = 1;
    Button uploadVideoSelectButton, uploadVideoButton;
    VideoView uploadVideoPreview;
    Uri videoUri;
    MediaController videoPreviewController;
    EditText uploadVideoDescription, uploadVideoSubject;
    StorageReference mStorageRef;
    StorageTask uploadTask;
    String current_user_id;
    FirebaseAuth mAuth;
    DatabaseReference uploadedVideoRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_lec);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uploadedVideoRef = FirebaseDatabase.getInstance().getReference();
        //current_user_id = mAuth.getCurrentUser().getUid();

        uploadVideoDescription = (EditText) findViewById(R.id.uploadVideoDescription);
        uploadVideoSubject = (EditText) findViewById(R.id.uploadVideoSubject);

        loadingBar = new ProgressDialog(this);

        uploadVideoSelectButton = (Button) findViewById(R.id.uploadVideoSelectButton);
        uploadVideoSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectVideoToPreview();
            }
        });

        uploadVideoButton = (Button) findViewById(R.id.uploadVideoButton);
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoToFirebaseStorage();
            }
        });

        uploadVideoPreview = (VideoView) findViewById(R.id.uploadVideoPreview);

        videoPreviewController = new MediaController(this);
        uploadVideoPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener()
                {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
                    {
                        uploadVideoPreview.setMediaController(videoPreviewController);
                        videoPreviewController.setAnchorView(uploadVideoPreview);
                    }
                });
            }
        });
        uploadVideoPreview.start();
    }

    private void UploadVideoToFirebaseStorage()
    {
        //VALIDATE EDITTEXT FIELD's
        final String description = uploadVideoDescription.getText().toString();
        final String subject = uploadVideoSubject.getText().toString();
        if (TextUtils.isEmpty(description) && TextUtils.isEmpty(subject))
        {
            Toast.makeText(this, "Field's are empty.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            final StorageReference fileReference = mStorageRef.child("Video_Lectures").child(current_user_id + getFileExtension(videoUri));
            uploadTask = fileReference.putFile(videoUri);
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
                        String current_uid = mAuth.getUid();
                        uploadedVideoRef = FirebaseDatabase.getInstance().getReference("VideoLectures").child(current_uid);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("video", mUri);
                        map.put("description", description);
                        map.put("subject", subject);
                        uploadedVideoRef.updateChildren(map);
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddVideoLecActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(AddVideoLecActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void SelectVideoToPreview()
    {
        Intent vv = new Intent();
        vv.setType("video/*");
        vv.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(vv,"Select a video"), PICK_VIDEO_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_INTENT && resultCode == RESULT_OK && data != null)
        {
            videoUri = data.getData();
            uploadVideoPreview.setVideoURI(videoUri);
        }
    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

}
