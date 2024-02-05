package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityWelcomeBinding;
import com.google.android.material.card.MaterialCardView;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private FirebaseAuthRepository auth;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = new FirebaseAuthRepository();
        if(auth.getCurrentUser() != null){
            setCurrentUser();
        }
        setupUserCards();
        setupContinueButton();
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
        Intent intent = new Intent(WelcomeActivity.this, CourseListActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupContinueButton() {
        binding.continueButton.setOnClickListener((v)->{
            if(type != null){
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(getApplicationContext(),"Choose an account type..",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupUserCards() {
        binding.student.setOnClickListener((v -> {
            highlightCard(binding.student,binding.teacher);
            type = "student";
        }));

        binding.teacher.setOnClickListener((v)->{
            highlightCard(binding.teacher,binding.student);
            type = "teacher";
        });
    }

    private void highlightCard(MaterialCardView selectCard,MaterialCardView otherCard){
        selectCard.setStrokeColor(ContextCompat.getColor(this,R.color.black));
        selectCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.gray));
        selectCard.setCardElevation(16);

        otherCard.setStrokeColor(ContextCompat.getColor(this,R.color.bg_color));
        selectCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.white));
        otherCard.setCardElevation(8);
    }
}