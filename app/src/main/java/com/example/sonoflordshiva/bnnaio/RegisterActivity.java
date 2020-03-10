package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class RegisterActivity extends AppCompatActivity
{
    private EditText UserFullname, UserFathername, UserRollno, UserContact, UserUid, UserEmail, UserPassword, UserConfirmPassword;
    TextView hasAccount;
    ImageView backReg;
    private Button CreateAccount;
    private FirebaseAuth mAuth;
    private CircleImageView StudProfileImage;
    private ProgressDialog loadingBar;
    private DatabaseReference studReference;

    private FirebaseUser mCurrentUser;

    // Storage Firebase
    private StorageReference mImageStorage;
    private Uri imageUri;
    private StorageTask uploadTask;
    private static final int GALLERY_PICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //UserFullname = (EditText) findViewById(R.id.sign_studFullname);
        //UserFathername = (EditText) findViewById(R.id.sign_studFathersname);
        //UserRollno = (EditText) findViewById(R.id.sign_studRollno);
        //UserContact = (EditText) findViewById(R.id.sign_studContact);
        //UserUid = (EditText) findViewById(R.id.sign_studUid);
        UserEmail = (EditText) findViewById(R.id.sign_studEmail);
        UserPassword = (EditText) findViewById(R.id.sign_studPass);
        UserConfirmPassword = (EditText) findViewById(R.id.sign_studCon_pass);
        CreateAccount = (Button) findViewById(R.id.sign_studButton);
        hasAccount = (TextView) findViewById(R.id.already_have_account);
        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        backReg = (ImageView) findViewById(R.id.backarrowRegis);
        backReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent wec = new Intent(RegisterActivity.this,WelcomeActivity.class);
                startActivity(wec);
            }
        });

        loadingBar = new ProgressDialog(this);
        CreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CreateNewAccount();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser studcurrentUser = mAuth.getCurrentUser();
        if(studcurrentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent=new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount()
    {
        //final String fulname = UserFullname.getText().toString();
        //final String fathername = UserFathername.getText().toString();
        //final String rollno = UserRollno.getText().toString();
        //final String contact = UserContact.getText().toString();
        //final String uid = UserUid.getText().toString();
        final String email = UserEmail.getText().toString();
        final String password = UserPassword.getText().toString();
        final String confirmpassword = UserConfirmPassword.getText().toString();
        /*if(TextUtils.isEmpty(fulname))
        {
            Toast.makeText(this, "Name is Mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fathername))
        {
            Toast.makeText(this, "Father Name is empty...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(rollno))
        {
            Toast.makeText(this, "Roll-no is empty...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contact))
        {
            Toast.makeText(this, "Contact cannot be empty...", Toast.LENGTH_SHORT).show();
        }
        else if(contact.length()!=10)
        {
            Toast.makeText(this, "Invalid Contact...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(uid))
        {
            Toast.makeText(this, "UID required...", Toast.LENGTH_SHORT).show();
        }
        else if(uid.length()!=10)
        {
            Toast.makeText(this, "Invaild UID...", Toast.LENGTH_SHORT).show();
        }
        else*/
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "E-mail is Mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Create Password...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(this, "Confirm Password...", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmpassword))
        {
            Toast.makeText(this, "Password Mis-matched", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Sign Up Status");
            loadingBar.setMessage("please wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToSetupActivity();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error Occurred ;" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}
