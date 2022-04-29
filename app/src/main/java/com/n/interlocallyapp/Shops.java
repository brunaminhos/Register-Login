package com.n.interlocallyapp;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class Shops {

    private String id, name, cuisineCategory;
    private double latitude, longitude;

    public Shops() {
    }

    public Shops(String id, String name, double latitude, double longitude, String cuisineCategory) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cuisineCategory = cuisineCategory;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCuisineCategory() {
        return cuisineCategory;
    }

    public void setCuisineCategory(String cuisineCategory) {
        this.cuisineCategory = cuisineCategory;
    }
}