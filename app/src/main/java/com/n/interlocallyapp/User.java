package com.n.interlocallyapp;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;


public class User {

    private String userID;
    private String email;
    private GeoPoint location;

    public User() {
    }

    public User(String email, GeoPoint location) {
        this.email = email;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public void createNewUser() {
        String email = getEmail();
        GeoPoint location = getLocation();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> users = new HashMap<>();
        users.put("email", email);
        users.put("location", location);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Success saving data.");
            }
        })
                .addOnFailureListener(e -> Log.d("db_error", "Error"));
    }
}