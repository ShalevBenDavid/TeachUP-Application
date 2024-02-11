package com.example.login2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class UserModel implements Parcelable {

    private String userId;
    private String userEmail;
    private String userName;
    private String userDescription;
    private String profilePicUrl;

    public UserModel() {
    }

    public UserModel(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    protected UserModel(Parcel in) {
        userId = in.readString();
        userEmail = in.readString();
        userName = in.readString();
        userDescription = in.readString();
        profilePicUrl = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userEmail);
        dest.writeString(userName);
        dest.writeString(userDescription);
        dest.writeString(profilePicUrl);
    }
}
