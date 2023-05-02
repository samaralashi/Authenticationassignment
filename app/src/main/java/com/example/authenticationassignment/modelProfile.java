package com.example.authenticationassignment;

public class modelProfile {

        private String pass;
        private String email;
        private String phone;

    public modelProfile() {
    }

    public modelProfile(String pass, String email, String phone) {
        this.pass = pass;
        this.email = email;
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
