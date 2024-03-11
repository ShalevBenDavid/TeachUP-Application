package com.example.login2.Models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel {

    private String userId;
    private String userEmail;
    private String userName;
    private String userDescription;
    private String profilePicUrl;


    public UserModel(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

}
