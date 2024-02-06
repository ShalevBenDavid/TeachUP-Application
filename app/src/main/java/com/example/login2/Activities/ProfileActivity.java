package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.login2.R;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.userName.setText(UserManager.getInstance().getUserName());
        binding.descriptionBox.setText(UserManager.getInstance().getUserDescription());

    }
}