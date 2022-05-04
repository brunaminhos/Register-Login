package com.n.interlocallyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestActivity extends AppCompatActivity {

    private Button brazil;
    private FirebaseUser activeShop = FirebaseAuth.getInstance().getCurrentUser();
    private String currentId = activeShop.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        brazil = findViewById(R.id.brazilTest);

        currentId = "X0mfAipqqDAXRU9DfUgk";
        LatLng location = new LatLng(52.660846, -8.631604);
        String shopName = "Link Brazil";
        String address = "test worked";
        String contactNumber = "weeeeeeee worked";
        String shopPicture = "https://firebasestorage.googleapis.com/v0/b/interlocally-c1c70.appspot.com/o/linkbrazil.jpg?alt=media&token=d54a5478-9b6a-435f-b884-8004232647aa";


        brazil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the intent in the target activity
                Intent brazilIntent = getIntent();

                //get the attached bundle from the intent
                Bundle args = brazilIntent.getExtras();

                args.putString("id_dataProvider", currentId);
                args.putParcelable("longLat_dataProvider", location);
                args.putString("shopName_dataProvider", shopName);
                args.putString("address_dataProvider", address);
                args.putString("contactNumber_dataProvider", contactNumber);
                args.putString("shopPicture_dataProvider", shopPicture);

                brazilIntent = new Intent(TestActivity.this, ProductInfo.class);
                brazilIntent.putExtras(args);
                startActivity(brazilIntent);
            }
        });

    }
}