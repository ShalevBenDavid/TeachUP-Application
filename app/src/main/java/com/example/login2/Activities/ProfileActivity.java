package com.example.login2.Activities;

// Import statements for required classes and interfaces
import static com.example.login2.Utils.Constants.PROFILE_OWNER;
import static com.example.login2.Utils.Constants.USER_MODEL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // For image loading and display
import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Repositories.UserRepository;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityProfileBinding;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding; // Binding for layout views
    private ActivityResultLauncher<Intent> launcher; // For handling image pick result
    private StorageRepository storageRepository; // For uploading images to storage
    private UserRepository userRepository; // For user data operations
    private CustomProgressDialog progressDialog; // Custom progress dialog for loading indication
    private Uri fileUri; // URI of the selected image file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new CustomProgressDialog(this);

        // Determine if the profile belongs to the current user or another user
        boolean owner = getIntent().getBooleanExtra(PROFILE_OWNER, false);
        if (owner) {
            setupForOwner(); // Setup UI for profile owner
        } else {
            setupForViewer(); // Setup UI for profile viewer
        }
    }

    // Configure the UI for a user viewing another user's profile
    private void setupForViewer() {
        UserModel user = getIntent().getParcelableExtra(USER_MODEL);
        if (user != null) {
            // Set the user's information on the UI elements
            binding.userName.setText(user.getUserName());
            binding.descriptionBox.setText(user.getUserDescription());

            // Load the user's profile picture
            loadProfilePic(user.getProfilePicUrl());

            // Hide editing controls since the viewer cannot edit another user's profile
            binding.editImage.setVisibility(View.GONE);
            binding.descriptionBox.setEnabled(false);
            binding.editProfileButton.setVisibility(View.GONE);
        }
    }

    // Configure the UI for the profile owner
    private void setupForOwner() {
        // Set the current user's information on the UI elements
        binding.userName.setText(UserManager.getInstance().getUserName());
        binding.descriptionBox.setText(UserManager.getInstance().getUserDescription());

        // Load the current user's profile picture
        loadProfilePic(UserManager.getInstance().getCurrentUserModel().getProfilePicUrl());


        // Setup image picker for profile image editing
        binding.editImage.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            launcher.launch(photoPicker);
        });




        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();

                if (data == null) return;

                // Set the picked image to the profile image view and enable the save button
                fileUri = data.getData();
                binding.profileImage.setImageURI(fileUri);
                binding.editProfileButton.setEnabled(true);
            } else {
                CustomUtils.showToast(this, "No Image Selected");
            }
        });


        // Enable save button when the description is changed
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

        // Handle profile update process
        binding.editProfileButton.setOnClickListener(v -> {
            progressDialog.show();
            if (fileUri != null) {
                // First, upload the new profile image, pass a callback to updateProfile
                uploadProfileImage(this::updateProfile);
            } else{
               updateProfile();
            }
        });
    }

    // Uploads the selected profile image to a predefined path in the storage
    private void uploadProfileImage(Runnable onUploadComplete) {
        storageRepository = new StorageRepository();
        storageRepository.uploadFile(fileUri, "profile_pics/" + UserManager.getInstance().getUserId(), new StorageRepository.StorageTaskListener() {
            @Override
            public void onSuccess(String downloadUrl) {
                // Update the user model with the new profile picture URL
                UserManager.getInstance().getCurrentUserModel().setProfilePicUrl(downloadUrl);
                onUploadComplete.run();
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(ProfileActivity.this, "Couldn't Upload the logo");
                progressDialog.dismiss();
            }
        });
    }

    private void updateProfile(){
        userRepository = new UserRepository();
        UserModel user = UserManager.getInstance().getCurrentUserModel();
        Map<String, Object> updates = new HashMap<>();
        updates.put("userDescription", binding.descriptionBox.getText().toString());
        updates.put("profilePicUrl", user.getProfilePicUrl());
        userRepository.editUser(UserManager.getInstance().getUserId()
                , updates
                , new UserRepository.FirestoreRepositoryCallback() {
                    @Override
                    public void onSuccess(UserModel user) {
                        CustomUtils.showToast(ProfileActivity.this, "Profile Updated Successfully");
                        userRepository.getCurrentUser(UserManager.getInstance().getUserId(), new UserRepository.FirestoreRepositoryCallback() {
                            @Override
                            public void onSuccess(UserModel user) {
                                UserManager.getInstance().setUserModel(user);
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onError(String error) {
                                CustomUtils.showToast(ProfileActivity.this, "Can't display the updated profile");
                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        CustomUtils.showToast(ProfileActivity.this, "Couldn't Update the profile");
                        progressDialog.dismiss();
                    }
                });
    }

    private void loadProfilePic(String imageUrl){
        Glide.with(ProfileActivity.this)
                .load(imageUrl)
                .placeholder(R.drawable.course_logo_placeholder)
                .into(binding.profileImage);
    }
}