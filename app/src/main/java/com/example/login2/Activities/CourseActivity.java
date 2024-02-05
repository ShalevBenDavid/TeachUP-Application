package com.example.login2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.login2.R;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityCourseBinding;
import com.example.login2.databinding.ActivityCourseListBinding;
import com.google.android.material.card.MaterialCardView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.firstName.setText(UserManager.getInstance().getCurrentUserModel().getUserName());
        binding.studyMaterialCard.setOnClickListener((v)->{
            startActivity(new Intent(CourseActivity.this,StudyMaterialActivity.class));
        });

        binding.courseCode.setText(CourseManager.getInstance().getCurrentCourse().getCourseId());
        binding.courseCodeCard.setOnClickListener((v)->{
            binding.courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.gray));
            copyToClipboard(binding.courseCode.getText().toString());
            new Handler().postDelayed(()->binding.courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this,R.color.white)),100);
            CustomUtils.showToast(this,"Course code copied to clipboard");
        });
       binding.profilePic.setOnClickListener((v)->{
            CustomUtils.showToast(this,"TODO: go to profile");
        });

       binding.chatActivity.setOnClickListener(v ->{
           CustomUtils.showToast(this,"chat clicked");
           startActivity(new Intent(CourseActivity.this,MainChatActivity.class));
       });
    }

    private void copyToClipboard(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null,string);
        clipboard.setPrimaryClip(clip);
    }
}