package com.example.tictactoegui.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class User {
    private String username;
    private String email;
    private String userStatus;

    @JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("status") String userStatus) {
        this.username = username;
        this.email = email;
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
    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', status='" + userStatus + "'}";
    }

}
