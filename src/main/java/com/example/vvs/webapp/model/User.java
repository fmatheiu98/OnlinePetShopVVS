package com.example.vvs.webapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    private String email;
    private String passwd;

    public User(String first_name, String last_name, String email, String passwd) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.passwd = passwd;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(email, user.email) && Objects.equals(passwd, user.passwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, first_name, last_name, email, passwd);
    }
}
