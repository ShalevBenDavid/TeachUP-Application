package com.example.login2.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Repositories.StudyMaterialRepository;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UriUtils;

import java.util.List;

public class UploadStudyMaterial extends DialogFragment {

    private ActivityResultLauncher<Intent> launcher;
    private ImageView uploadImage;
    private EditText fileTitle,fileDescription;
    private Uri fileUri;
    private StorageRepository storage;
    private StudyMaterialRepository studyMaterialRepository;
    private CustomProgressDialog progressDialog;
    private StudyMaterialModel studyMaterial;
    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_upload_study_material, null, false);

        initializeAttributes(view);

        uploadImage.setOnClickListener((v) -> pickFile());

        return setupInitDialog(view);
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

    private Dialog setupInitDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        builder.setPositiveButton("Upload",null);
        builder.setNegativeButton("Cancel",(dialog,id)->dialog.dismiss());

        dialog = builder.create();

        dialog.setOnShowListener(dialogInterface ->{
            Button uploadButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            uploadButton.setOnClickListener((v)->{
                if(validateFields()){
                    uploadFile(this::addStudyMaterial);
                }
            });
        });

        return dialog;
    }

    private void addStudyMaterial() {
        studyMaterial.setTitle(fileTitle.getText().toString());
        studyMaterial.setDescription(fileDescription.getText().toString());

        studyMaterialRepository.addStudyMaterial(studyMaterial, new StudyMaterialRepository.FirestoreCallback() {
            @Override
            public void onSuccess(List<StudyMaterialModel> studyMaterials) {
                CustomUtils.showToast(requireContext(),"Uploaded successfully");
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(requireContext(),error);
                dialog.dismiss();
            }
        });
    }

    private void uploadFile(Runnable onUploadComplete) {
        progressDialog.show();
        storage.uploadFile(fileUri,
                CourseManager.getInstance().getCurrentCourse().getCourseId() + "/" + UriUtils.getFileName(requireContext(),fileUri),
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

        if (TextUtils.isEmpty(fileTitle.getText().toString())) {
            fileTitle.setError("Enter a Title");
            flag = false;
        }

        if (fileUri == null) {
            CustomUtils.showToast(requireContext(),"You have to chose a file");
            flag = false;
        }

        return flag;
    }


    private void initializeAttributes(View view) {
        uploadImage = view.findViewById(R.id.uploadLogo);
        fileTitle = view.findViewById(R.id.fileTitle);
        fileDescription = view.findViewById(R.id.fileDescription);
        storage = new StorageRepository();
        progressDialog = new CustomProgressDialog(requireContext());
        studyMaterial = new StudyMaterialModel();
        studyMaterialRepository = new StudyMaterialRepository();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) return;
                        fileUri = data.getData();
                        uploadImage.setImageResource(R.drawable.cloud_check_svgrepo_com);
                        studyMaterial.setFileType(UriUtils.getFileType(requireContext(),fileUri));
                    } else {
                        CustomUtils.showToast(requireContext(), "No Image Selected");
                    }
                });

    }

}