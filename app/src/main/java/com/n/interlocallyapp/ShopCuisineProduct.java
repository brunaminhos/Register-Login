package com.n.interlocallyapp;

public class ShopCuisineProduct  {

    String productId, name, description, discountDescription, imagelink;
    int  cuisineId, imageId;
    double productPrice;
    boolean productAvailable;

    public ShopCuisineProduct(String productId, String name, String description, String imagelink /*,String discountDescription, ,int imageId , int cuisineId, double productPrice, boolean productAvailable*/) {
        this.name = name;
        this.description = description;
        //  this.discountDescription = discountDescription;
        //this.imageId = imageId;
        this.imagelink = imagelink;
        this.productId = productId;
       /* this.cuisineId = cuisineId;
        this.productPrice = productPrice;
        this.productAvailable = productAvailable;*/
    }
}
