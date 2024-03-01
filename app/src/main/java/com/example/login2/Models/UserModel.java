package com.example.login2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel implements Parcelable {

    private String userId;
    private String userEmail;
    private String userName;
    private String userDescription;
    private String profilePicUrl;

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
