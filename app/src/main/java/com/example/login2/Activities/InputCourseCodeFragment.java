package com.example.login2.Activities;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.login2.Models.UserModel;
import com.example.login2.Repositories.CourseRepository;
import com.example.login2.Repositories.EnrollmentsRepository;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.FragmentInputCourseCodeBinding;

import java.util.List;


public class InputCourseCodeFragment extends DialogFragment {
    private FragmentInputCourseCodeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInputCourseCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpButtons();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dimWindow(dialog);
        return dialog;
    }

    private void dimWindow(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.75f);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    private void setUpButtons() {
        binding.enrollButton.setOnClickListener(v -> enrollStudent());
        binding.cancelButton.setOnClickListener(v->dismiss());
    }

    private void enrollStudent() {
        String code = binding.courseCodeET.getText().toString().trim();
        Log.e("course code",code);
        validateCode(code, (isValid, message) -> {
            if(isValid){
                EnrollmentsRepository repository = new EnrollmentsRepository();
                repository.enrollStudent(code, UserManager.getInstance().getUserId(), new EnrollmentsRepository.EnrollmentCallback() {
                    @Override
                    public void onSuccess(List<UserModel> students) {
                        CourseRepository courseRepository = new CourseRepository();
                        courseRepository.getCourse(code).addOnSuccessListener(courseModel->{
                            ((CourseListActivity) requireContext()).getStudentCourseAdapter().addCourse(courseModel);
                            new Handler().postDelayed(()-> {CustomUtils.showToast(requireContext(),"Enrolled Successfully"); dismiss();},500);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        CustomUtils.showToast(getActivity().getApplicationContext(),error);
                    }
                });
            } else{
                CustomUtils.showToast(getActivity().getApplicationContext(),message);

            }
        });
    }

    private void validateCode(String code, EnrollmentResultListener result) {
        if (TextUtils.isEmpty(code)) {
            result.onResult(false, "Enter a course code");
            return;
        }

        CourseRepository courseRepository = new CourseRepository();

        courseRepository.doesExist(code).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult()) {
                // The course exists, now check if the student is enrolled
                EnrollmentsRepository enrollmentsRepository = new EnrollmentsRepository();
                enrollmentsRepository.isEnrolled(UserManager.getInstance().getUserId(), code)
                        .addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful() && task2.getResult()) {
                                // Student is already enrolled in this course
                                result.onResult(false, "Student already enrolled in this course");
                            } else {
                                // Student is not enrolled in this course
                                result.onResult(true, null);
                            }
                        });
            } else {
                // The course code does not exist or there was an error in the task
                result.onResult(false, "Course code does not exist");
            }
        });
    }

    interface EnrollmentResultListener {
        void onResult(boolean success, String message);
    }


}