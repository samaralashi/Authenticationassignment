package com.example.authenticationassignment;

public class ReadWriteUserDetails {
    public String email;
    public String fullName;
    public String mobile;
    public String gender;


    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String email, String fullName, String mobile, String gender) {
        this.email = email;
        this.fullName = fullName;
        this.mobile = mobile;
        this.gender = gender;
    }
}

