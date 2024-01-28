package com.example.login2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.login2.Models.UserModel;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Repositories.UserRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.google.firebase.auth.FirebaseUser;


public class LoginTab extends Fragment {
    private FirebaseAuthRepository auth;
    private EditText password,email;
    private Button loginButton;
    private String type;
    private CustomProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        initializeAttributes(view);
        setupLoginButton();

        setupLoginButton();
        return view;
    }

    private void setupLoginButton() {
        loginButton.setOnClickListener((v)->{
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if(validateCredentials(userEmail,userPassword)){
                performLogin(userEmail,userPassword);
            }
        });
    }

    private boolean validateCredentials(String userEmail, String userPassword) {
        if(userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            CustomUtils.showToast(getContext(),"Enter a valid email address");
            return false;
        }

        if(userPassword.isEmpty()){
            CustomUtils.showToast(getContext(),"Enter a password");
            return false;
        }
        return true;
    }

    private void performLogin(String email,String password){
        progressDialog.show();
        auth.login(email, password, new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                onLoginSuccess();
            }

            @Override
            public void onError(String message) {
                onLoginFailure(message);
            }
        });
    }

    private void onLoginSuccess(){
        saveUserType();

        UserManager.getInstance().setUserModel(auth.getUid(),type, new UserManager.UserManagerCallback() {
            @Override
            public void onUserLoaded(UserModel user) {
                progressDialog.dismiss();
                startCourseListActivity();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                CustomUtils.showToast(getContext(), "Failed to load user data.");
            }
        });
        }

    private void saveUserType() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(
                Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_TYPE_KEY,type);
        editor.apply();
    }

    private void startCourseListActivity() {
        Intent intent = new Intent(getActivity(), CourseListActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        requireActivity().finish();
    }

    private void onLoginFailure(String message){
        CustomUtils.showToast(getContext(),"Login failed:" + message);
        progressDialog.dismiss();
    }

    private void initializeAttributes(View view) {
        password = view.findViewById(R.id.loginPassword);
        email = view.findViewById(R.id.loginEmail);
        loginButton = view.findViewById(R.id.signinButton);
        auth = new FirebaseAuthRepository();
        progressDialog = new CustomProgressDialog(requireContext());
        type = ((SignActivity) requireActivity()).getUserType();
    }
}