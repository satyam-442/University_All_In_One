package com.example.sonoflordshiva.bnnaio.AsTeacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sonoflordshiva.bnnaio.MainActivity;
import com.example.sonoflordshiva.bnnaio.Prevalent.Prevalent;
import com.example.sonoflordshiva.bnnaio.R;
import com.example.sonoflordshiva.bnnaio.Setting;
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
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherSettingsActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private Button settingTeacher_update_profilebtn;
    private CircleImageView settingTeacher_profile_image;

    private DatabaseReference SettingUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID, myUrl="";

    private ProgressDialog loadingBar;

    private FirebaseUser mCurrentUser;
    private EditText settingTeacher_profile_name, settingTeacher_profile_contact, settingTeacher_profile_email, settingTeacher_profile_rollno, userUid;
    // Storage Firebase
    private StorageReference mImageStorage;
    private Uri imageUri;
    private StorageTask uploadTask;
    private static final int GALLERY_PICK = 1;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_settings);

        mAuth=FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        SettingUserRef = FirebaseDatabase.getInstance().getReference().child("Teachers").child(Prevalent.currentOnlineUser.getPhonee());
        SettingUserRef.keepSynced(true);

        back = (ImageView) findViewById(R.id.backTeacherarrowSetting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToHomePage();
            }
        });

        settingTeacher_profile_name = (EditText) findViewById(R.id.settingTeacher_profile_name);
        //settingTeacher_profile_contact = (EditText) findViewById(R.id.settingTeacher_profile_contact);
        settingTeacher_profile_email = (EditText) findViewById(R.id.settingTeacher_profile_email);
        settingTeacher_profile_rollno = (EditText) findViewById(R.id.settingTeacher_profile_rollno);
        settingTeacher_update_profilebtn = (Button) findViewById(R.id.settingTeacher_update_profilebtn);
        settingTeacher_profile_image = (CircleImageView) findViewById(R.id.settingTeacher_profile_image);

        loadingBar = new ProgressDialog(this);
        settingTeacher_update_profilebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidateAccountInfo();
            }
        });

        /*settingTeacher_profile_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(TeacherSettingsActivity.this);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });*/

        settingTeacher_profile_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(TeacherSettingsActivity.this);
                //startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mImageStorage = FirebaseStorage.getInstance().getReference().child("profile_images").child("Teachers");

        displayUsersDetails(settingTeacher_profile_image,settingTeacher_profile_name, settingTeacher_profile_contact, settingTeacher_profile_email,settingTeacher_profile_rollno);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            settingTeacher_profile_image.setImageURI(imageUri);
        }*/

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settingTeacher_profile_image.setImageURI(imageUri);

            UploadImageToFBDatabase();
        }
        else
        {
            //UploadImageToFBDatabase(imageUri);
        }
    }

    private void UploadImageToFBDatabase() {
        loadingBar = new ProgressDialog(TeacherSettingsActivity.this);
        loadingBar.setTitle("Uploading Image...");
        loadingBar.setMessage("Please wait while we upload and process the image.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(imageUri != null)
        {
            String current_user_id = Prevalent.currentOnlineUser.getNamee() + Prevalent.currentOnlineUser.getPhonee();
            final StorageReference fileReference = mImageStorage.child(current_user_id + getFileExtension(imageUri));
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
                        String current_uid = Prevalent.currentOnlineUser.getPhonee();
                        SettingUserRef = FirebaseDatabase.getInstance().getReference("Teachers").child(current_uid);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", mUri);
                        SettingUserRef.updateChildren(map);
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(TeacherSettingsActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(TeacherSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    private void displayUsersDetails(final CircleImageView settingTeacher_profile_image, final EditText settingTeacher_profile_name, final EditText settingTeacher_profile_contact, final EditText settingTeacher_profile_email, final EditText settingTeacher_profile_rollno)
    {
        SettingUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("Phone").exists())
                    {
                        String name = dataSnapshot.child("Name").getValue().toString();
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        String email = dataSnapshot.child("Email").getValue().toString();
                        String qualification = dataSnapshot.child("Qualification").getValue().toString();

                        /*Picasso.with(TeacherSettingsActivity.this).load(image).into(settingTeacher_profile_image);*/
                        settingTeacher_profile_name.setText(name);
                        settingTeacher_profile_contact.setText(phone);
                        settingTeacher_profile_email.setText(email);
                        settingTeacher_profile_rollno.setText(qualification);

                        final String image = dataSnapshot.child("image").getValue().toString();
                        if(!image.equals("default"))
                        {
                            Picasso.with(TeacherSettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(settingTeacher_profile_image);
                            Picasso.with(TeacherSettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(settingTeacher_profile_image, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {

                                }

                                @Override
                                public void onError()
                                {
                                    Picasso.with(TeacherSettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(settingTeacher_profile_image);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void BackToHomePage() {
        Intent wec = new Intent(this, TeacherMainActivity.class);
        wec.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(wec);
        finish();
    }

    private void ValidateAccountInfo()
    {
        String username = settingTeacher_profile_name.getText().toString();
        String useremail = settingTeacher_profile_email.getText().toString();
        String usercontact = settingTeacher_profile_contact.getText().toString();
        String userroll = settingTeacher_profile_rollno.getText().toString();

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
        else
        {
            UpdateAccountInfo(username,useremail,usercontact,userroll);
        }
    }

    private void UpdateAccountInfo(String username, String useremail, String usercontact, String userroll) {
        HashMap<String,Object> studentMap = new HashMap<String,Object>();
        studentMap.put("Name",username);
        studentMap.put("Phone",usercontact);
        studentMap.put("Qualification",userroll);
        studentMap.put("Email",useremail);
        SettingUserRef.updateChildren(studentMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    //SendUserToMainActivity();
                    Toast.makeText(TeacherSettingsActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(TeacherSettingsActivity.this, "Error Occurred: Try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

}
