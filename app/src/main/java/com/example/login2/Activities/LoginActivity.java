package com.example.login2.Activities;

import static com.example.login2.Utils.Constants.USER_TYPE_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login2.Models.UserModel;
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
        setContentView(binding.getRoot());

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
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        });

        binding.forgotPassword.setOnClickListener(v ->{
            PasswordReset dialogFragment = new PasswordReset();
            dialogFragment.show(getSupportFragmentManager(), "emailEntry");
        });
    }

    private void performLogin(String email,String password){
        progressDialog.show();
        auth.login(email, password, new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                saveUserType();
                onLoginSuccess();
            }

            @Override
            public void onError(String message) {
                onLoginFailure(message);
            }
        });
    }

    private void onLoginSuccess(){

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
        editor.putString(USER_TYPE_KEY,type);
        editor.apply();
    }

    private void startCourseListActivity() {
        Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        progressDialog.dismiss();
    }

    private void onLoginFailure(String message){
        CustomUtils.showToast(this,"Login failed:" + message);
        progressDialog.dismiss();
    }

    private boolean validateCredentials(String userEmail, String userPassword) {
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