package com.example.login2.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuthRepository auth;
    private ActivityLoginBinding binding;
    private String type;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = new FirebaseAuthRepository();
        progressDialog = new CustomProgressDialog(this);

        type = getIntent().getStringExtra("type");


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        binding.signinButton.setOnClickListener((v)->{
            String userEmail = binding.loginEmail.getText().toString().trim();
            String userPassword = binding.loginPassword.getText().toString();

            if(validateCredentials(userEmail,userPassword)){
                performLogin(userEmail,userPassword);
            }
        });

        binding.signUp.setOnClickListener(v ->{
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        });
    }

    private void performLogin(String email,String password){
        progressDialog.show();
        auth.login(email, password, new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                onLoginSuccess();
            }

            @Override
            public void onError(String message) {
                onLoginFailure(message);
            }
        });
    }

    private void onLoginSuccess(){
        saveUserType();

        UserManager.getInstance().setUserModel(auth.getUid(),type, new UserManager.UserManagerCallback() {
            @Override
            public void onUserLoaded(UserModel user) {
                progressDialog.dismiss();
                startCourseListActivity();
            }

            @Override
            public void onError(String error) {
                onLoginFailure(error);
                progressDialog.dismiss();
            }
        });
    }

    private void saveUserType() {
        SharedPreferences preferences = getSharedPreferences(
                Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_TYPE_KEY,type);
        editor.apply();
    }

    private void startCourseListActivity() {
        Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        finish();
    }

    private void onLoginFailure(String message){
        CustomUtils.showToast(this,"Login failed:" + message);
        progressDialog.dismiss();
    }

    private boolean validateCredentials(String userEmail, String userPassword) {
        Log.e("email",userEmail);
        Log.e("pass",userPassword);
        if(userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            CustomUtils.showToast(this,"Enter a valid email address");
            return false;
        }

        if(userPassword.isEmpty()){
            CustomUtils.showToast(this,"Enter a password");
            return false;
        }
        return true;
    }


}