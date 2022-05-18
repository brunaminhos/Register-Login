package com.finalProjectJB.interlocallyapp;

import com.google.firebase.database.Exclude;

public class ShopOwner {
    //Retrieve shop owner information and authenticate user.

    private String id, name, email, password;

    public ShopOwner() {
    }

    public ShopOwner(String email, String password) {
        this.email = email;
        this.password = password;
    }


    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}