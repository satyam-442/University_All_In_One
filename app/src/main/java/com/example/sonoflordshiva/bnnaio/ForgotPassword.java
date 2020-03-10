package com.example.sonoflordshiva.bnnaio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity
{
    private Button ResetPasswordButton;
    private EditText ResetEmailInput;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        back = (ImageView) findViewById(R.id.backarrowReset);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ForgotPassword.this,LoginActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(back);
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.forgot_password_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        loadingbar = new ProgressDialog(this);

        ResetPasswordButton = (Button) findViewById(R.id.reset_password_button);
        ResetEmailInput = (EditText) findViewById(R.id.reset_password_email_input);
        ResetPasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = ResetEmailInput.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(ForgotPassword.this, "Field is empty...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.setTitle("Sending Mail");
                    loadingbar.setMessage("please wait...");
                    loadingbar.show();
                    loadingbar.setCanceledOnTouchOutside(true);
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ForgotPassword.this, "Mail sent", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
