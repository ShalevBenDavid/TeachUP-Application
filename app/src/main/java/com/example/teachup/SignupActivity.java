package com.example.teachup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    EditText userName, userEmail, userPassword;
    TextView SigninBtn, SignupBtn;
    String name, email, password;

    FirebaseFirestore db;
    CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        userName = findViewById(R.id.usernametext);
        userEmail = findViewById(R.id.emailtext);
        userPassword = findViewById(R.id.passwordtext);
        SigninBtn = findViewById(R.id.login);
        SignupBtn = findViewById(R.id.signup);

        // Sign-Up click button event.
        SignupBtn.setOnClickListener(v -> {
            // Save email, password and name.
            name = userName.getText().toString().trim();
            email = userEmail.getText().toString().trim();
            password = userPassword.getText().toString().trim();

            // If no name was entered, print error.
            if (TextUtils.isEmpty(name)) {
                userEmail.setError("No Name Was Entered");
                userEmail.requestFocus();
                return;
            }

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
            SignUp();
        });

        // Sign-Up click button event.
        SigninBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        });
    }

    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }
    }

    // Sign Up account and update database.
    private void SignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    UserProfileChangeRequest userProfileChangeRequest =
                            new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updateProfile(userProfileChangeRequest);

                    UserModel userModel = new UserModel
                            (FirebaseAuth.getInstance().getUid(), name, email, password);
                    usersCollection.add(userModel).addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(SignupActivity.this, "Error adding user to Firestore",
                                Toast.LENGTH_SHORT).show();
                    });
                })  // Sign Up failed.
                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "SignUp failed",
                        Toast.LENGTH_SHORT).show());
    }
}