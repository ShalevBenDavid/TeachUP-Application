package com.example.login2.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Repositories.StudyMaterialRepository;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UriUtils;
import com.example.login2.ViewModels.StudyMaterialViewModel;
import com.example.login2.databinding.ActivityUploadMaterialBinding;

public class UploadMaterialActivity extends AppCompatActivity {
    private ActivityUploadMaterialBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private Uri fileUri;
    private CustomProgressDialog progressDialog;
    private StudyMaterialModel studyMaterial;
    private StudyMaterialViewModel studyMaterialViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.uploadLogo.setOnClickListener(v ->{
            pickFile();
        });

        progressDialog = new CustomProgressDialog(this);

        studyMaterialViewModel = new ViewModelProvider(this).get(StudyMaterialViewModel.class);

        studyMaterial = new StudyMaterialModel();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) return;
                        fileUri = data.getData();
                        binding.uploadLogo.setImageResource(R.drawable.cloud_check_svgrepo_com);
                        studyMaterial.setFileType(UriUtils.getFileType(this,fileUri));
                    } else {
                        CustomUtils.showToast(this, "No Image Selected");
                    }
                });

        binding.uploadButton.setOnClickListener(v ->{
            if(validateFields()){
                Log.e("here uploading","uploading");
                uploadFile(this::addStudyMaterial);
            }
        });

        binding.cancelButton.setOnClickListener(v->{
            finish();
        });
    }

    private void pickFile() {
        Intent filePicker = new Intent(Intent.ACTION_GET_CONTENT);
        filePicker.setType("*/*");
        String[] mimeTypes = {"image/*", "video/*", "application/pdf", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"};
        filePicker.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        filePicker.addCategory(Intent.CATEGORY_OPENABLE);
        launcher.launch(filePicker);
    }

    private void addStudyMaterial() {
        studyMaterial.setTitle(binding.fileTitle.getText().toString());
        studyMaterial.setDescription(binding.fileDescription.getText().toString());

        studyMaterialViewModel.addStudyMaterial(studyMaterial, new StudyMaterialRepository.FirestoreCallback() {
            @Override
            public void onSuccess() {
                onUploadedSuccessful();
            }

            @Override
            public void onError(String error) {
                onUploadFailure(error);
            }
        });
    }

    private void onUploadFailure(String error) {
        CustomUtils.showToast(this,error);
        progressDialog.dismiss();
    }

    private void onUploadedSuccessful() {
        CustomUtils.showToast(this,"Uploaded Successfully");
        progressDialog.dismiss();
        finish();
    }

    private void uploadFile(Runnable onUploadComplete) {
        progressDialog.show();
        studyMaterialViewModel.uploadStudyMaterial(fileUri,
                CourseManager.getInstance().getCurrentCourse().getCourseId() + "/" + UriUtils.getFileName(this,fileUri),
                new StorageRepository.StorageTaskListener() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        studyMaterial.setFileUrl(downloadUrl);
                        onUploadComplete.run();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean validateFields() {
        boolean flag = true;

        if (TextUtils.isEmpty(binding.fileTitle.getText().toString())) {
            binding.fileTitle.setError("Enter a Title");
            flag = false;
        }

        if (fileUri == null) {
            CustomUtils.showToast(this,"You have to chose a file");
            flag = false;
        }

        return flag;
    }


}