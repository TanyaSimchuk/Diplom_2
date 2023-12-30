package edu.praktikum.models;

public class UserCreate {
    private String email;
    private String password;
    private String name;
    public UserCreate withEmail(String email) {
        this.email = email;
        return this;
    }
    public UserCreate withPassword(String password) {
        this.password = password;
        return this;
    }
    public UserCreate withName(String name) {
        this.name = name;
        return this;
    }
}
