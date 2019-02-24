package com.example.android.plannertracker;

public class User {
    private String userName;
    private String userEmail;
    private String userPassword;


    public User(){};
    public User(String userName, String userEmail,String userPassword ){
        this.userName=userName;
        this.userEmail=userEmail;
         this.userPassword=userPassword;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserName()
    {return userName;}

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
