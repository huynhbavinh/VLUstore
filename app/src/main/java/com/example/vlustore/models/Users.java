package com.example.vlustore.models;

public class Users {
    String fullname, username, password,  phoneNo;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Users() {

    }

    public Users(String fullname, String username, String password, String phoneNo) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.phoneNo = phoneNo;
    }
}
