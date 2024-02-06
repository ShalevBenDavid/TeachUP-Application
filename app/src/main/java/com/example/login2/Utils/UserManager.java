package com.example.login2.Utils;

import android.util.Log;

import com.example.login2.Models.UserModel;
import com.example.login2.Repositories.UserRepository;

public class UserManager {
    private static volatile UserManager instance;
    private UserModel currentUser;
    private String type;

    private UserManager() {
    }

    public static UserManager getInstance() {
        Log.d("UserManager", "calling getinstance");
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public UserModel getCurrentUserModel() {
        return currentUser;
    }

    public void setUserModel(String userId, String userType, UserManagerCallback userManagerCallback) {
        new UserRepository().getCurrentUser(userId, new UserRepository.FirestoreRepositoryCallback() {
            @Override
            public void onSuccess(UserModel user) {
                UserManager.this.currentUser = user;
                type = userType;
                userManagerCallback.onUserLoaded(user);
                Log.d("UserManager", "currentUser set: " + user);

            }

            @Override
            public void onError(String error) {
                userManagerCallback.onError(error);
                Log.d("UserManager", "Error fetching user: " + error);

            }
        });
    }

    public String getUserType(){
        return type;
    }

    public String getUserName(){
        return currentUser.getUserName();
    }

    public String getUserDescription(){
        return currentUser.getUserDescription();
    }
    public String getUserId(){
        return currentUser.getUserId();
    }

    public interface UserManagerCallback {
        void onUserLoaded(UserModel user);

        void onError(String error);
    }
}
