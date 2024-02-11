package com.example.login2.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Repositories.UserRepository;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityProfileBinding;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private StorageRepository storageRepository;
    private UserRepository userRepository;
    private CustomProgressDialog progressDialog;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new CustomProgressDialog(this);
        
        boolean owner = getIntent().getBooleanExtra("profileowner",false);
        Log.e("isowner", String.valueOf(owner));
        if(owner){
            setUpForOwner();
        } else {
            setUpForViewer();
        }
    }

    private void setUpForViewer() {
        UserModel user = getIntent().getParcelableExtra("userModel");
        if(user != null) {
            binding.userName.setText(user.getUserName());
            binding.descriptionBox.setText(user.getUserDescription());

            Glide.with(ProfileActivity.this)
                    .load(user.getProfilePicUrl())
                    .placeholder(R.drawable.course_logo_placeholder)
                    .into(binding.profileImage);

            binding.editImage.setVisibility(View.GONE);
            binding.editTextButton.setVisibility(View.GONE);
            binding.descriptionBox.setEnabled(false);
            binding.editProfileButton.setVisibility(View.GONE);
        }
    }

    private void setUpForOwner() {
        binding.userName.setText(UserManager.getInstance().getUserName());
        binding.descriptionBox.setText(UserManager.getInstance().getUserDescription());

        binding.editImage.setOnClickListener(v ->{
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            launcher.launch(photoPicker);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == RESULT_OK){
                Intent data = result.getData();

                if(data == null) return;

                fileUri = data.getData();
                binding.profileImage.setImageURI(fileUri);
                binding.editProfileButton.setEnabled(true);
            } else{
                CustomUtils.showToast(this,"No Image Selected");
            }
        });

        if(UserManager.getInstance().getCurrentUserModel().getProfilePicUrl() != null) {
            Glide.with(ProfileActivity.this)
                    .load(UserManager.getInstance().getCurrentUserModel().getProfilePicUrl())
                    .placeholder(R.drawable.course_logo_placeholder)
                    .into(binding.profileImage);
        }

        binding.descriptionBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editProfileButton.setEnabled(true);
            }
        });

        binding.editProfileButton.setOnClickListener(v ->{
            progressDialog.show();
            userRepository = new UserRepository();
            if(fileUri != null){
                uploadProfileImage(new Runnable() {
                    @Override
                    public void run() {
                        UserModel user = UserManager.getInstance().getCurrentUserModel();
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("userDescription",binding.descriptionBox.getText().toString());
                        updates.put("profilePicUrl",user.getProfilePicUrl());
                        userRepository.editUser(UserManager.getInstance().getUserId()
                                , updates
                                , new UserRepository.FirestoreRepositoryCallback() {
                                    @Override
                                    public void onSuccess(UserModel user) {
                                        CustomUtils.showToast(ProfileActivity.this,"Profile Updated Successfully");
                                        UserManager.getInstance().setUserModel(user);
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        CustomUtils.showToast(ProfileActivity.this,"Couldn't Update the profile");
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }

    private void uploadProfileImage(Runnable onUploadComplete) {
        storageRepository = new StorageRepository();
        storageRepository.uploadFile(fileUri, "profile_pics/" + UserManager.getInstance().getUserId(), new StorageRepository.StorageTaskListener() {
            @Override
            public void onSuccess(String downloadUrl) {
                UserManager.getInstance().getCurrentUserModel().setProfilePicUrl(downloadUrl);
                onUploadComplete.run();
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(ProfileActivity.this,"Couldn't Upload the logo");
                progressDialog.dismiss();
            }
        });
    }
}