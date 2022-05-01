package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    private Button loadButton;
    private TextView textViewData, textViewCategories;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");
    private DocumentReference ref = db.collection("Shop").document();


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        loadButton = findViewById(R.id.loadBtn);
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

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        textViewData.setText(myRef.child("ShopCuisineProfile").get().toString());

//        loadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent categoryIntent = new Intent(Search.this, MapsActivity.class);
//                startActivity(categoryIntent);
//            }
//        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Shop")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String data = "";
                            String categorias = "";

                            String name = null;
                            double latitude = 0;
                            double longitude = 0;
                            String stringLongitude = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Shops shop = document.toObject(Shops.class);
//                                shop.setId(document.getId());
//
//                                Toast.makeText(getApplicationContext(), document.getData().get("ShopOwner").toString() , Toast.LENGTH_SHORT).show();


                                Map<String, Object> profile = (Map<String, Object>) document.getData().get("ShopCuisineProfile");


                                String stringGeoPoint = profile.get("location").toString();
                                String stringLatitude = stringGeoPoint.substring(0, stringGeoPoint.indexOf(","));
                                stringLatitude = stringLatitude.substring(stringLatitude.indexOf("="), stringLatitude.length());
                                stringLatitude = stringLatitude.substring(1, stringLatitude.length());
                                stringLongitude = stringGeoPoint.substring(stringGeoPoint.indexOf(","), stringGeoPoint.length());
                                stringLongitude = stringLongitude.substring(stringLongitude.indexOf("="), stringLongitude.length());
                                stringLongitude = stringLongitude.substring(1, stringLongitude.length() - 2);
                                latitude = Double.parseDouble(stringLatitude);
                                longitude = Double.parseDouble(stringLongitude);


                                Map<Double, Object> category = (Map<Double, Object>) document.getData().get("CuisineCategory");
//                                String[] values = categories.values().toArray(new String[0]);
//
//                                String id = document.getId();
                                name = (String) profile.get("Name");
//                                latitude = (double) location.getLatitude();
//                                double longitude = (double) location.get("longitude");
//
//                                data += "ID: " + id + "\nName: " + name + "\nLatitude: "
//                                        + latitude + "\nLongitude " + longitude + "\n\n";
//
//                                categorias = Arrays.toString(values);
                            }

//                            Query categoriesLists = db.collection("Shop")
//                                    .whereEqualTo("CuisineCategory", true)
//                                    .whereEqualTo("Name", true);

                            textViewData.setText(stringLongitude);

                            /**
                             * Sends the latitude and longitude to the MapsActivity class.
                             */
                            LatLng position = new LatLng(latitude, longitude);
                            Bundle args = new Bundle();
                            args.putParcelable("longLat_dataProvider", position);
                            Intent categoryIntent = new Intent(Search.this, MapsActivity.class);
                            categoryIntent.putExtras(args);
                            startActivity(categoryIntent);
//                            textViewCategories.setText(categoriesLists.toString());

                        }
                    });
            }
        });
    }
}

//                shopReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        String data = "";
//                        String categories = "";
//
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            Shops shop = documentSnapshot.toObject(Shops.class);
//                            shop.setId(documentSnapshot.getId());
//
//                            List<String> duplicatesCategories = new ArrayList<>();
//                            List<String> noDuplicatedCategories = new ArrayList<>();
//
//                            String id = shop.getId();
//                            String name = shop.getName();
//                            String cuisineCategory = shop.getCuisineCategory();
//                            double latitude = shop.getLatitude();
//                            double longitude = shop.getLongitude();
//
//                            data += "ID: " + id + "\nName: " + name + "\nCuisine Category: " + cuisineCategory + "\nLatitude: " + latitude + "\nLongitude " + longitude;
//
//
//                            duplicatesCategories.add(cuisineCategory);
//
//                            Set<String> set = new HashSet<>(duplicatesCategories);
//                            Iterator<String> categoriesIterator = set.iterator();
//
//                            while(categoriesIterator.hasNext()){
//                                noDuplicatedCategories.add(categoriesIterator.next());
//                                categories += categoriesIterator.next() + "\n";
//                            }
//                        }
//                        textViewData.setText(data);
//                        textViewCategories.setText(categories);
//                    }
//                });
//            }
//        });
//    }