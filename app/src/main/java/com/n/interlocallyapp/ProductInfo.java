package com.n.interlocallyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProductInfo extends AppCompatActivity {

    private TextView shopName, contactNumber, address;
    private ImageView shopImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        //get the intent in the target activity
        Intent intent = getIntent();

        Bundle args = intent.getExtras();

        shopImage = findViewById(R.id.shopImageView);
        shopName = findViewById(R.id.textViewShopName);
        contactNumber = findViewById(R.id.textViewPhoneNumber);
        address = findViewById(R.id.textViewShopAddress);

        Picasso.get().load(args.getString("shopPicture_dataProvider")).into(shopImage);
        shopName.setText(args.getString("shopName_dataProvider"));
        contactNumber.setText(args.getString("shopName_dataProvider"));
        address.setText(args.getString("address_dataProvider"));
        contactNumber.setText(args.getString("contactNumber_dataProvider"));

    }
}