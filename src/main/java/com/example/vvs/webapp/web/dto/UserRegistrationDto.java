package com.example.vvs.webapp.web.dto;

public class UserRegistrationDto {

    private String first_name;
    private String last_name;
    private String email;
    private String passwd;

    public UserRegistrationDto(String first_name, String last_name, String email, String passwd) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.passwd = passwd;
    }

    public UserRegistrationDto() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
