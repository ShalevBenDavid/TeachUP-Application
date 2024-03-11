package com.example.login2.Repositories;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FirebaseAuthRepository {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, AuthResultListener listener) {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    listener.onSuccess(firebaseAuth.getCurrentUser());
                } else{
                    listener.onError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
    }

    public void logout(){
        firebaseAuth.signOut();
    }

    public void signUp(String email, String password, AuthResultListener listener){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                listener.onSuccess(firebaseAuth.getCurrentUser());
            } else{
                listener.onError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    public void sendResetPasswordEmail(String email,AuthResultListener listener){
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onSuccess(null);
                        } else {
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            listener.onError(errorMessage);
                        }
                    });
        } else {
          listener.onError("Email address format is invalid");
        }

    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public String getUid(){
        return firebaseAuth.getUid();
    }

    public interface AuthResultListener {
        void onSuccess(FirebaseUser user);

        void onError(String message);
    }
}


