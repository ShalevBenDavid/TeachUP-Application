package com.example.login2.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Repositories.UserRepository;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Utils.CustomUtils;
import com.google.firebase.auth.FirebaseUser;



public class SignupTab extends Fragment {
    private EditText signupEmail, signupPassword, confirmPassword,userName;
    private Button signupButton;
    private FirebaseAuthRepository firebaseAuth;
    private UserRepository userRepository;
    private CustomProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        initializeAttributes(view);
        setupSignupButton();


        return view;
    }

    private void setupSignupButton() {
        signupButton.setOnClickListener((v)->{
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();
            String name = userName.getText().toString().trim();

            if(validateCredentials(email,password,confirmPass,name)){
                progressDialog.show();
                performSignUp(email,password,name);
            }
        });
    }

    private void performSignUp(String email, String password,String name) {

        firebaseAuth.signUp(email, password, new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                makeUser(email,name);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                CustomUtils.showToast(getContext(),"Registration Failed: "+message);
                progressDialog.dismiss();
            }
        });
    }

    private boolean validateCredentials(String email, String password, String confirmPass,String name) {
        boolean flag = true;

        if(name.isEmpty()){
            userName.setError("Enter your Name");
            flag = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signupEmail.setError("Provide a valid email address");
            flag = false;
        }

        if(password.isEmpty()){
            signupPassword.setError("Provide a password");
            flag=false;
        }

        if(!confirmPass.equals(password)){
            confirmPassword.setError("The passwords do not match");
            flag = false;
        }

        return flag;
    }

    private void initializeAttributes(View view) {
        progressDialog = new CustomProgressDialog(requireContext());
        firebaseAuth = new FirebaseAuthRepository();
        userRepository = new UserRepository();
        signupEmail = view.findViewById(R.id.email);
        signupPassword = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signupButton = view.findViewById(R.id.signupButton);
        userName = view.findViewById(R.id.name);
    }


    private void makeUser(String email, String name) {
        UserModel newUser = new UserModel(email,name);
        newUser.setUserId(firebaseAuth.getUid());

        userRepository.addUser(newUser, new UserRepository.FirestoreRepositoryCallback() {
            @Override
            public void onSuccess(UserModel user) {
                CustomUtils.showToast(getContext(),"Registered Successfully");
//                ((SignActivity) requireActivity()).viewPager2.setCurrentItem(0);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(getContext(),"Registration Failed: "+error);
                progressDialog.dismiss();
            }
        });
    }
}
