package com.n.interlocallyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProductInfo extends AppCompatActivity {

    TextView categoryClick, feedbackClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        categoryClick = findViewById(R.id.categoryText);
        feedbackClick = findViewById(R.id.feedbackText);

        categoryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductInfo.this, "Working", Toast.LENGTH_SHORT).show();
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {

        AlertDialog dialog = new AlertDialog.Builder(ProductInfo.this)
                .setTitle("Password Reset")
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                }).create();
        dialog.show();
    }
}