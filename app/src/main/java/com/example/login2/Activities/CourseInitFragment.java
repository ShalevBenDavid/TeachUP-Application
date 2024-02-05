package com.example.login2.Activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.login2.Models.CourseModel;
import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.CourseRepository;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;


public class CourseInitFragment extends DialogFragment {
    private ImageView courseLogo;
    private EditText courseName, courseDescription;
    private StorageRepository storage;
    private CourseRepository courseRepository;
    private CourseModel newCourse;
    private Uri logoUri;
    private ActivityResultLauncher<Intent> launcher;
    private CustomProgressDialog progressDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_course_init, null, false);

        initializeAttributes(view);

        courseLogo.setOnClickListener((v) -> pickCourseLogo());

        return setupInitDialog(view);
    }

    private void pickCourseLogo() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        launcher.launch(photoPicker);
    }

    private Dialog setupInitDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view)
                .setPositiveButton("Submit", (dialog, id) -> {
                    String name = courseName.getText().toString().trim();
                    String description = courseDescription.getText().toString().trim();

                    if (validateFields(name, description)) {
                        if (logoUri != null) {
                            uploadLogo(()->registerCourse(name,description));
                        }
                        registerCourse(name, description);
                    }else {
                        CustomUtils.showToast(requireContext(),"No course added");
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    private void uploadLogo(Runnable onUploadComplete) {
        String fileName = CustomUtils.generateFileName(requireContext(),logoUri);
        storage.uploadFile(logoUri, "course_logos/" + fileName, new StorageRepository.StorageTaskListener() {
            @Override
            public void onSuccess(String downloadUrl) {
                newCourse.setCourseLogoUrl(downloadUrl);
                onUploadComplete.run();
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(requireContext(),"Couldn't Upload the logo");
                progressDialog.dismiss();
            }
        });
    }

    private void registerCourse(String name, String description) {
        UserModel user = UserManager.getInstance().getCurrentUserModel();
        newCourse.setCourseDescription(description);
        newCourse.setCourseName(name);
        newCourse.setTeacherName(user.getUserName());
        newCourse.setCourseTeacherId(user.getUserId());

        courseRepository.addCourse(newCourse);
    }

    private boolean validateFields(String name, String description) {
        boolean flag = true;


        if (TextUtils.isEmpty(name)) {
            courseName.setError("Enter a Course Name");
            flag = false;
        }

        if (TextUtils.isEmpty(description)) {
            courseDescription.setError("Provide a Course Description");
            flag = false;
        }

        return flag;
    }


    private void initializeAttributes(View view) {
        courseLogo = view.findViewById(R.id.courseLogo);
        courseName = view.findViewById(R.id.courseName);
        courseDescription = view.findViewById(R.id.courseDescription);
        progressDialog = new CustomProgressDialog(requireContext());
        storage = new StorageRepository();
        newCourse = new CourseModel();
        courseRepository = new CourseRepository();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) return;

                        logoUri = data.getData();
                        courseLogo.setImageURI(logoUri);
                    } else {
                        CustomUtils.showToast(requireContext(), "No Image Selected");
                    }
                });

    }
}