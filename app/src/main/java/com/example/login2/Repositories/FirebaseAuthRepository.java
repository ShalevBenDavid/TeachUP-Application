package com.example.login2.Repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                    listener.onError(task.getException().getMessage());
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
                listener.onError(task.getException().getMessage());
            }
        });
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


