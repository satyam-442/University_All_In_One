package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity
{
    private Toolbar mToolbar;
    private EditText userName, userContact, userEmail, userRollno, userUid;
    private Button UpdateInfoButton;
    private CircleImageView userProfileImage;

    private DatabaseReference SettingUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    private ProgressDialog loadingBar;

    private FirebaseUser mCurrentUser;

    // Storage Firebase
    private StorageReference mImageStorage;
    private Uri imageUri;
    private StorageTask uploadTask;
    private static final int GALLERY_PICK = 1;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        SettingUserRef = FirebaseDatabase.getInstance().getReference().child("Students").child(currentUserID);
        SettingUserRef.keepSynced(true);

        userProfileImage = (CircleImageView) findViewById(R.id.setting_profile_image);

        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

        back = (ImageView) findViewById(R.id.backarrowSetting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHomePage();
            }
        });

        userName = (EditText) findViewById(R.id.setting_profile_name);
        userContact = (EditText) findViewById(R.id.setting_profile_contact);
        userEmail = (EditText) findViewById(R.id.setting_profile_email);
        userRollno = (EditText) findViewById(R.id.setting_profile_rollno);
        userUid = (EditText) findViewById(R.id.setting_profile_uid);
        UpdateInfoButton = (Button) findViewById(R.id.setting_update_profilebtn);
        userProfileImage = (CircleImageView) findViewById(R.id.setting_profile_image);

        loadingBar = new ProgressDialog(this);

        SettingUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userNAME = dataSnapshot.child("Fullname").getValue().toString();
                    String userROLLNO = dataSnapshot.child("Roll No").getValue().toString();
                    String userUID = dataSnapshot.child("UID").getValue().toString();
                    String userCONTACT = dataSnapshot.child("Contact").getValue().toString();
                    String userEMAIL = dataSnapshot.child("Email").getValue().toString();

                    userName.setText(userNAME);
                    userContact.setText(userCONTACT);
                    userRollno.setText(userROLLNO);
                    userUid.setText(userUID);
                    userEmail.setText(userEMAIL);

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if(!image.equals("default"))
                    {
                        Picasso.with(Setting.this).load(image).placeholder(R.drawable.default_avatar).into(userProfileImage);
                        Picasso.with(Setting.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(userProfileImage, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {

                            }

                            @Override
                            public void onError()
                            {
                                Picasso.with(Setting.this).load(image).placeholder(R.drawable.default_avatar).into(userProfileImage);
                            }
                        });
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
        UpdateInfoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidateAccountInfo();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        mImageStorage = FirebaseStorage.getInstance().getReference();

    }

    private void BackToHomePage() {
        Intent wec = new Intent(Setting.this,MainActivity.class);
        wec.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(wec);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.home)
        {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ValidateAccountInfo()
    {
        String username = userName.getText().toString();
        String useremail = userEmail.getText().toString();
        String usercontact = userContact.getText().toString();
        String userroll = userRollno.getText().toString();
        String useruid = userUid.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Enter The Full Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(useremail))
        {
            Toast.makeText(this, "Enter The E-mail", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(usercontact))
        {
            Toast.makeText(this, "Enter The Contact no.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userroll))
        {
            Toast.makeText(this, "Enter The Roll No", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(useruid))
        {
            Toast.makeText(this, "Enter The UID no", Toast.LENGTH_SHORT).show();
        }
        else
        {
            UpdateAccountInfo(username,useremail,usercontact,userroll,useruid);
        }
    }

    private void UpdateAccountInfo(String username, String useremail, String usercontact, String userroll, String useruid)
    {
        HashMap<String,Object> studentMap = new HashMap<String,Object>();
        studentMap.put("Fullname",username);
        studentMap.put("Contact",usercontact);
        studentMap.put("UID",useruid);
        studentMap.put("Roll No",userroll);
        studentMap.put("E-mail",useremail);
        SettingUserRef.updateChildren(studentMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    SendUserToMainActivity();
                    Toast.makeText(Setting.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Setting.this, "Error Occurred: Try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);
            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && uploadTask!= null && uploadTask.isInProgress() && data != null && data.getData() != null)
            {
                Toast.makeText(this, "Upload In Progress", Toast.LENGTH_SHORT).show();
            }
            else
            {
                UloadImage();
            }
        }
    }

    private void UloadImage() {
        loadingBar = new ProgressDialog(Setting.this);
        loadingBar.setTitle("Uploading Image...");
        loadingBar.setMessage("Please wait while we upload and process the image.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(imageUri != null)
        {
            String current_user_id = mAuth.getUid();
            final StorageReference fileReference = mImageStorage.child("profile_images").child(current_user_id + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);

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
                        SettingUserRef = FirebaseDatabase.getInstance().getReference("Students").child(current_uid);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", mUri);
                        SettingUserRef.updateChildren(map);
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(Setting.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(Setting.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

}
