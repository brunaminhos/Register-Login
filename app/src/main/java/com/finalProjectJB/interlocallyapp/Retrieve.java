package com.finalProjectJB.interlocallyapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

class Retrieve extends ShopOwner{


    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    private String email, password;

    public void retrieveData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Shop");

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Shop").document()
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ShopOwner shopOwner = documentSnapshot.toObject(ShopOwner.class);
                email = shopOwner.getEmail().trim();;
                password = shopOwner.getPassword().trim();

                authenticate();
            }
        });
    }

    private void authenticate() {
        //register the user into the firebase
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("User Authenticated.");
            }
        });
    }
}
