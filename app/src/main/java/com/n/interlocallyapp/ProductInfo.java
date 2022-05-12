package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductInfo extends AppCompatActivity {

    private TextView shopName, contactNumber, address, productNameView, descriptionView, priceView, discountView;
    private String iD, shopNameString;
    private ImageView shopImage, productImage;
    private ArrayList<Map<String,Object>> product;
    private RatingBar ratingBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference feedbackReference;
    List<Double> shopRating = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        shopImage = findViewById(R.id.shopImageView);
        shopName = findViewById(R.id.textViewShopName);
        contactNumber = findViewById(R.id.textViewPhoneNumber);
        address = findViewById(R.id.textViewShopAddress);
        productNameView = findViewById(R.id.textViewProductName);
        descriptionView = findViewById(R.id.textViewDescription);
        priceView = findViewById(R.id.textViewPrice);
        discountView = findViewById(R.id.textViewDiscount);
        productImage = findViewById(R.id.productImageView);
        ratingBar = findViewById(R.id.ratingBar);

        //get the intent in the target activity
        Intent intent = getIntent();

        Bundle args = intent.getExtras();
        args = intent.getExtras();
        product = (ArrayList<Map<String, Object>>) args.getSerializable("products_dataProvider");
        iD = args.getString("ID_dataProvider");

        Log.d("TAG2","ID: " + iD);
        Log.d("TAG2", product.toString());

//                Map<String, Object> productData = (Map<String, Object>) entry.getValue();
//                if (productData.get("Name").equals(selectedProduct)) {
//                    Map<String, Object> productsMap = (Map<String, Object>) entry.getValue();

        for (Map<String,Object> map : product) {
            // we iterate over all values of the current map
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(map.get("ID").equals(iD)){
                    if(entry.getKey().equals("Description")){
                        descriptionView.setText(entry.getValue().toString());
                    }else if(entry.getKey().equals("DiscountDescription")){
                        discountView.setText(entry.getValue().toString());
                    }else if(entry.getKey().equals("Name")){
                        productNameView.setText(entry.getValue().toString());
                    }else if(entry.getKey().equals("Picture")){
                        Picasso.get().load(entry.getValue().toString()).into(productImage);
//                        productPicture = entry.getValue().toString();
                    }else if(entry.getKey().equals("Price")){
                        priceView.setText("€" + entry.getValue().toString());
                    }
                }
            }
        }
        shopNameString = args.getString("ShopName_dataProvider");
//        Picasso.get().load(productPicture).into(productImage);
        Picasso.get().load(args.getString("shopPicture_dataProvider")).into(shopImage);
        shopName.setText(shopNameString);
        address.setText(args.getString("address_dataProvider"));
        contactNumber.setText(args.getString("contactNumber_dataProvider"));
    }


    private void feedbacks() {
        db.collection(shopNameString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d("TAG_1", document.getId() + " => " + document.get("rating"));
                                shopRating.add(Double.parseDouble(document.get("rating").toString()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        if(shopRating.size() != 0) {
                            double totalRatings = 0;
                            double sum = 0;
                            for (int i = 0; i < shopRating.size(); i++) {
                                sum += shopRating.get(i);
                            }
                            totalRatings = sum / shopRating.size();

                            Map<String, Object> saveRatings = new HashMap<>();
                            saveRatings.put("rating", totalRatings);

                            ratingBar.setRating((float) totalRatings);

                            DocumentReference documentReference = db.collection("Feedback").document(shopNameString);
                            documentReference.set(saveRatings).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("db", "Success saving data.");
                                }
                            })
                                    .addOnFailureListener(e -> Log.d("db_error", "Error"));
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        feedbacks();
    }
}