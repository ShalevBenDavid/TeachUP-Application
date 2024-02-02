package com.example.teachup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    TextView SigninBtn, SignupBtn;
    String email, password;

    FirebaseFirestore db;
    CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        userEmail = findViewById(R.id.emailtext);
        userPassword = findViewById(R.id.passwordtext);
        SigninBtn = findViewById(R.id.login);
        SignupBtn = findViewById(R.id.signup);

        // Sign-In click button event.
        SigninBtn.setOnClickListener(v -> {
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
        });

        // Sign-Up click button event.
        SignupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
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
                .addOnSuccessListener(authResult -> {
                    String userID  = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    usersCollection.document(userID).get()
                            // Sign In succeeded.
                            .addOnSuccessListener(documentSnapshot -> {
                                    String username = documentSnapshot.getString("userName");
                                    Intent intent = new Intent
                                            (SigninActivity.this, MainActivity.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                    finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(SigninActivity.this,
                                        "Error retrieving user data", Toast.LENGTH_SHORT).show();
                            });
                }) // Sign In failed.
                .addOnFailureListener(e -> {
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
                });
    }
}