package com.example.vvs.webapp.web.dto;

public class OrderDto {

    private String first_name;
    private String last_name;
    private String email;
    private String country;
    private String address;

    public OrderDto(String first_name, String last_name, String email, String country, String address) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.country = country;
        this.address = address;
    }

    public OrderDto(){

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
