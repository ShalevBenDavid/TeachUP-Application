package com.example.teachup;

public class UserModel {

    String userID;
    String userName;
    String userEmail;
    String userPassword;

    // Constructor.
    public UserModel (String userID, String userName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    // Empty constructor.
    public UserModel () {

    }

    // Getters.
    public String getUserID () {
        return userID;
    }
    public String getUserName () {
        return userName;
    }
    public String getUserEmail () {
        return userEmail;
    }
    public String getUserPassword () {
        return userPassword;
    }


    // Setters.
    public void setUserID (String userID) {
        this.userID = userID;
    }
    public void setUserName (String userName) {
        this.userName = userName;
    }
    public void setUserEmail (String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserPassword (String userPassword) {
        this.userPassword = userPassword;
    }
}
