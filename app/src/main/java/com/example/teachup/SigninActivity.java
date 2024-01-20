package com.example.teachup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    TextView SigninBtn, SignupBtn;
    String email, password;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userEmail = findViewById(R.id.emailtext);
        userPassword = findViewById(R.id.passwordtext);
        SigninBtn = findViewById(R.id.login);
        SignupBtn = findViewById(R.id.signup);

        // Sign-In click button event.
        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save email and password.
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                // If no email was entered, print error.
                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("No Email Was Entered");
                    userEmail.requestFocus();
                    return;
                }
                // If no password was entered, print error.
                if (TextUtils.isEmpty(password)) {
                    userPassword.setError("No Password Was Entered");
                    userPassword.requestFocus();
                    return;
                }
                SignIn();
            }
        });

        // Sign-Up click button event.
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // If user already logged in.
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            finish();
        }
    }

    private void SignIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    // Sign In succeeded.
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String username = FirebaseAuth.getInstance().getCurrentUser()
                                .getDisplayName();
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        intent.putExtra("name", username);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Sign In failed.
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If user wasn't found in database, print error.
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(SigninActivity.this, "User Doesn't Exists",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // Otherwise, login failed for other reason.
                        else {
                            Toast.makeText(SigninActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}