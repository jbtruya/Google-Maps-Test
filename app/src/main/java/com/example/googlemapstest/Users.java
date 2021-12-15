package com.example.googlemapstest;

public class Users {

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users(String userid, String email) {
        this.userid = userid;
        this.email = email;
    }

    public Users() {

    }

    String userid;
    String email;
}
