package com.example.login2.Repositories;

import com.example.login2.Models.UserModel;
import com.example.login2.Utils.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository(){
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(UserModel user, FirestoreRepositoryCallback listener){
        db.collection(Constants.USERS_COLLECTION).document(user.getUserId()).set(user)
                .addOnSuccessListener(aVoid -> {
                    listener.onSuccess(null);
                }).addOnFailureListener(e ->{
                    listener.onError(e.getMessage());
                });
    }


    public void editUser(String userId,
                         Map<String,String> updates,
                         FirestoreRepositoryCallback listener){

    }

    public void getCurrentUser(String userId, FirestoreRepositoryCallback listener){
         db.collection(Constants.USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
             listener.onSuccess(documentSnapshot.toObject(UserModel.class));
         }).addOnFailureListener(e ->{
             listener.onError(e.getMessage());
         });
    }


    public interface FirestoreRepositoryCallback {
        void onSuccess(UserModel user);
        void onError(String error);
    }

}

