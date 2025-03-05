package com.example.tictactoegui.model;

public class User {
    private String username;
    private String email;
    private String userStatus;
    public User(String UserName, String Email, String userStatus) {
        this.username = UserName;
        this.email = Email;
        this.userStatus = userStatus;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getUserStatus() {
        return userStatus;
    }

}
