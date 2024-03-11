package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuthRepository auth;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = new FirebaseAuthRepository();
        if(auth.getCurrentUser() != null){
            setCurrentUser();
        } else{
            new Handler().postDelayed(()->{
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();
            },2000);
        }
    }

    private void setCurrentUser() {
        getLastUserType();
        UserManager.getInstance().setUserModel(auth.getUid(),
                type, new UserManager.UserManagerCallback() {
                    @Override
                    public void onUserLoaded(UserModel user) {
                        skipLogin();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("error",error);
                    }
                });
    }

    private void getLastUserType() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFS_FILE,MODE_PRIVATE);
        type = preferences.getString(Constants.USER_TYPE_KEY,"student");
    }

    private void skipLogin() {
        new Handler().postDelayed(()->{
            startActivity(new Intent(SplashActivity.this, CourseListActivity.class));
            finish();
        },2000);
    }

}