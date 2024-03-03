package com.example.login2.Activities;

import static com.example.login2.Utils.Constants.PROFILE_OWNER;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.login2.R;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityCourseBinding;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.firstName.setText(UserManager.getInstance().getCurrentUserModel().getUserName());
        binding.studyMaterialCard.setOnClickListener((v) -> {
            startActivity(new Intent(CourseActivity.this, StudyMaterialActivity.class));
        });

        binding.courseCode.setText(CourseManager.getInstance().getCurrentCourse().getCourseId());

        binding.courseCodeCard.setOnClickListener((v) -> {
            binding.courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            copyToClipboard(binding.courseCode.getText().toString());
            new Handler().postDelayed(() -> binding.courseCodeCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white)), 100);
            CustomUtils.showToast(this, "Course code copied to clipboard");
        });

        if(!UserManager.getInstance().getUserId().equals(CourseManager.getInstance().getCurrentCourse().getCourseTeacherId())){
            binding.courseCodeCard.setVisibility(View.GONE);
        }

        if (UserManager.getInstance().getCurrentUserModel().getProfilePicUrl() != null) {
            Glide.with(CourseActivity.this)
                    .load(UserManager.getInstance().getCurrentUserModel().getProfilePicUrl())
                    .placeholder(R.drawable.course_logo_placeholder)
                    .into(binding.profilePic);
        }

        binding.profilePic.setOnClickListener((v) -> {
            Intent intent = new Intent(new Intent(CourseActivity.this, ProfileActivity.class));
            intent.putExtra(PROFILE_OWNER, true);
            startActivity(intent);
        });

        binding.chatActivity.setOnClickListener(v -> {
            startActivity(new Intent(CourseActivity.this, MainChatActivity.class));
        });

        binding.quizActivity.setOnClickListener(v -> {
            startActivity(new Intent(CourseActivity.this, ComposeCourseQuizzesActivity.class));
        });
    }

    private void copyToClipboard(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, string);
        clipboard.setPrimaryClip(clip);
    }
}