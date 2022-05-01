package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Search extends AppCompatActivity {

    private String[] items = {"Material", "Design", "Components"};
    private AutoCompleteTextView autoCompleteTxt;
    private ArrayAdapter<String> adapterItems;

    private Button loadButton, mapButton;
    private TextView textViewData, textViewCategories;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        loadButton = findViewById(R.id.loadBtn);
        mapButton = findViewById(R.id.mapBtn);
        textViewData = findViewById(R.id.textViewData);
        textViewCategories = findViewById(R.id.textViewCategories);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shopReference
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String data = "";
                                String categories = "";
                                double latitude = 0;
                                double longitude = 0;
                                String shopName = "";
                                List<String> duplicatesCategories = new ArrayList<>();
                                List<String> noDuplicatedCategories = new ArrayList<>();

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            Map<String, Object> shop = document.getData();
                                            for (Map.Entry<String, Object> entry : shop.entrySet()) {
                                                if (entry.getKey().equals("ShopCuisineProfile")) {
                                                    Map<String, Object> shopCuisineProfile = (Map<String, Object>) entry.getValue();
                                                    for (Map.Entry<String, Object> dataEntry : shopCuisineProfile.entrySet()) {
                                                        if (dataEntry.getKey().equals("location")) {
                                                            duplicatesCategories.add(dataEntry.getValue().toString());
                                                            Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                            GeoPoint geoPoint = (GeoPoint) dataEntry.getValue();
                                                            latitude = geoPoint.getLatitude();
                                                            longitude = geoPoint.getLongitude();
                                                            Log.d("TAG", dataEntry.getValue().toString());
                                                        }
                                                        if (dataEntry.getKey().equals("Name")) {
                                                            duplicatesCategories.add(dataEntry.getValue().toString());
                                                            Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                            shopName = dataEntry.getValue().toString();
                                                            Log.d("TAG", dataEntry.getValue().toString());
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(Search.this, "No such document", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "No such document");
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                Set<String> set = new HashSet<>(duplicatesCategories);

                                List<String> setAtoZ = new ArrayList<>();
                                for(String p : set) {
                                    setAtoZ.add(p);
                                }
                                Collections.sort(setAtoZ, new Comparator<String>() {
                                    @Override
                                    public int compare(String o1, String o2) {
                                        return o1.compareTo(
                                                o2);
                                    }
                                });

                                categories += setAtoZ;

                                LatLng position = new LatLng(latitude, longitude);
                                Bundle args = new Bundle();
                                args.putParcelable("longLat_dataProvider", position);
                                Intent categoryIntent = new Intent(Search.this, MapsActivity.class);
                                categoryIntent.putExtras(args);
                                categoryIntent.putExtra("name_dataProvider", shopName);
                                startActivity(categoryIntent);

                                textViewCategories.setText(categories);
                            }
                        });

//                showAlertDialog();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopReference
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String data = "";
                                String categories = "";
                                List<String> duplicatesCategories = new ArrayList<>();
                                List<String> noDuplicatedCategories = new ArrayList<>();

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            Map<String, Object> cuisineCategory = document.getData();
                                            for (Map.Entry<String, Object> entry : cuisineCategory.entrySet()) {
                                                if (entry.getKey().equals("CuisineCategory")) {
                                                    Map<String, Object> categoryName = (Map<String, Object>) entry.getValue();
                                                    for (Map.Entry<String, Object> dataEntry : categoryName.entrySet()) {
                                                        if (dataEntry.getKey().equals("Name")) {
                                                            duplicatesCategories.add(dataEntry.getValue().toString());
                                                            Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                            Log.d("TAG", dataEntry.getValue().toString());
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(Search.this, "No such document", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "No such document");
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                Set<String> set = new HashSet<>(duplicatesCategories);

                                List<String> setAtoZ = new ArrayList<>();
                                for(String p : set) {
                                    setAtoZ.add(p);
                                }
                                Collections.sort(setAtoZ, new Comparator<String>() {
                                    @Override
                                    public int compare(String o1, String o2) {
                                        return o1.compareTo(
                                                o2);
                                    }
                                });

                                categories += setAtoZ;

                                textViewCategories.setText(categories);
                            }
                        });
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(Search.this)
                .setTitle("Map")
                .setMessage("")
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
