package com.example.login2.Activities;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.login2.R;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.databinding.FragmentInputCourseCodeBinding;
import com.example.login2.databinding.FragmentPasswordResetBinding;
import com.google.firebase.auth.FirebaseUser;


public class PasswordReset extends DialogFragment {

    private FragmentPasswordResetBinding binding;
    private FirebaseAuthRepository authRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authRepository = new FirebaseAuthRepository();
        binding.sendEmail.setOnClickListener(v -> authRepository.sendResetPasswordEmail(binding.emailEt.getText().toString(), new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                CustomUtils.showToast(getContext(),"Check your inbox for a link to reset your password");
                dismiss();
            }

            @Override
            public void onError(String message) {
                CustomUtils.showToast(getContext(),message);
            }
        }));
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
}