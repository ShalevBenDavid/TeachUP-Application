package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.login2.Models.UserModel;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.C;

public class WelcomeActivity extends AppCompatActivity {
    private MaterialCardView studentCard;
    private MaterialCardView teacherCard;
    private Button continueButton;
    private FirebaseAuthRepository auth;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        studentCard = findViewById(R.id.student);
        teacherCard = findViewById(R.id.teacher);
        continueButton = findViewById(R.id.continue_button);
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
        continueButton.setOnClickListener((v)->{
            if(type != null){
                Intent intent = new Intent(WelcomeActivity.this,SignActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(getApplicationContext(),"Choose an account type..",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupUserCards() {
        studentCard.setOnClickListener((v -> {
            highlightCard(studentCard,teacherCard);
            type = "student";
        }));

        teacherCard.setOnClickListener((v)->{
            highlightCard(teacherCard,studentCard);
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