package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.example.login2.Models.UserModel;
import com.example.login2.Utils.CustomProgressDialog;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Repositories.UserRepository;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuthRepository repository = new FirebaseAuthRepository();
    private CustomProgressDialog progressDialog;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new CustomProgressDialog(this);

        type = getIntent().getStringExtra("type");

        binding.signin.setOnClickListener(v ->{
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
            finish();
        });

        setupSignupButton();
    }

    private void setupSignupButton() {
        binding.signupButton.setOnClickListener((v)->{
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String confirmPass = binding.confirmPassword.getText().toString().trim();
            String name = binding.name.getText().toString().trim();

            if(validateCredentials(email,password,confirmPass,name)){
                progressDialog.show();
                performSignUp(email,password,name);
            }
        });
    }

    private void performSignUp(String email, String password,String name) {

        repository.signUp(email, password, new FirebaseAuthRepository.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                makeUser(email,name);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                CustomUtils.showToast(SignUpActivity.this,"Registration Failed: "+message);
                progressDialog.dismiss();
            }
        });
    }

    private boolean validateCredentials(String email, String password, String confirmPass,String name) {
        boolean flag = true;

        if(name.isEmpty()){
            binding.name.setError("Enter your Name");
            flag = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Provide a valid email address");
            flag = false;
        }

        if(password.isEmpty()){
            binding.password.setError("Provide a password");
            flag=false;
        }

        if(!confirmPass.equals(password)){
            binding.confirmPassword.setError("The passwords do not match");
            flag = false;
        }

        return flag;
    }




    private void makeUser(String email, String name) {
        UserModel newUser = new UserModel(email,name);
        newUser.setUserId(repository.getUid());

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(newUser, new UserRepository.FirestoreRepositoryCallback() {
            @Override
            public void onSuccess(UserModel user) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                intent.putExtra("type",type);
                CustomUtils.showToast(SignUpActivity.this,"Registered Successfully");
                progressDialog.dismiss();
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                CustomUtils.showToast(SignUpActivity.this,"Registration Failed: "+error);
                progressDialog.dismiss();
            }
        });
    }
}
