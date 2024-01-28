package com.example.login2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.google.android.material.card.MaterialCardView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseActivity extends AppCompatActivity {
    private CircleImageView userProfile;
    private TextView firstName;
    private TextView courseCode;
    private MaterialCardView courseCodeCard;
    private MaterialCardView studyMaterialCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        userProfile = findViewById(R.id.profilePic);
        firstName = findViewById(R.id.firstName);
        studyMaterialCard = findViewById(R.id.studyMaterialCard);
        courseCode = findViewById(R.id.courseCode);
        courseCodeCard = findViewById(R.id.courseCodeCard);

        firstName.setText(UserManager.getInstance().getCurrentUserModel().getUserName());
        studyMaterialCard.setOnClickListener((v)->{
            startActivity(new Intent(CourseActivity.this,StudyMaterialActivity.class));
        });

        courseCode.setText(CourseManager.getInstance().getCurrentCourse().getCourseId());
        courseCodeCard.setOnClickListener((v)->{
            courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.gray));
            copyToClipboard(courseCode.getText().toString());
            new Handler().postDelayed(()->courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.white)),100);
            CustomUtils.showToast(this,"Course code copied to clipboard");
        });
        userProfile.setOnClickListener((v)->{
            CustomUtils.showToast(this,"TODO: go to profile");
        });
    }

    private void copyToClipboard(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null,string);
        clipboard.setPrimaryClip(clip);
    }
}