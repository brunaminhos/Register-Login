package com.finalProjectJB.interlocallyapp;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Rating {
    private double rating;
    private String shopName, collectionName;
    private Map<String, Object> saveRatings = new HashMap<>();

    public Rating(double rating, String shopName) {
        this.rating = rating;
        this.shopName = shopName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void createRating() {
        String shopName = getShopName();
        double rating = getRating();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

            saveRatings.put("rating", rating);

        DocumentReference documentReference = db.collection(shopName).document();
        documentReference.set(saveRatings).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Success saving data.");
            }
        })
                .addOnFailureListener(e -> Log.d("db_error", "Error"));
    }
}
