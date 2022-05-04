package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Search extends AppCompatActivity {

    private String data = "";
    private String categories = "";
    private String selectedCategory = "brazilian";
    private AutoCompleteTextView autoCompleteCategoryTxt, autoCompleteProductsTxt;
    private ArrayAdapter<String> adapterCategories;
    private ArrayAdapter<String> adapterProducts;
    private List<String> setCategories = new ArrayList<>();
    private List<String> setProducts = new ArrayList<>();
    private List<String> duplicatesCategories = new ArrayList<>();
    private List<String> duplicatesProducts= new ArrayList<>();

    private Button loadButton, searchButton, listButton;
  
    private TextView textViewData, textViewCategories;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //categoryFinder();

        categoriesSetter();

        loadButton = findViewById(R.id.loadBtn);

        searchButton = findViewById(R.id.searchBtn);

        listButton = findViewById(R.id.listBtn);

        textViewData = findViewById(R.id.textViewData);
        textViewCategories = findViewById(R.id.textViewCategories);
        autoCompleteCategoryTxt = findViewById(R.id.auto_complete_category_txt);
        autoCompleteProductsTxt = findViewById(R.id.auto_complete_product_txt);

        adapterCategories = new ArrayAdapter<String>(this, R.layout.dropdown_item, setCategories);
        adapterProducts = new ArrayAdapter<>(this, R.layout.dropdown_item, setProducts);

        autoCompleteCategoryTxt.setAdapter(adapterCategories);
        autoCompleteProductsTxt.setAdapter(adapterProducts);

        autoCompleteCategoryTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString().toLowerCase();
                Toast.makeText(getApplicationContext(), "Item: " + selectedCategory, Toast.LENGTH_SHORT).show();
/*
                productsFinder(selectedCategory);
*/
                autoCompleteProductsTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String selectedProduct = parent.getItemAtPosition(position).toString();
//                        Toast.makeText(getApplicationContext(), "Item: " + selectedProduct, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewCategories.setText(categories);
                textViewData.setText(data);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference cuisineCategoryDb = db.collection("ShopCuisineProduct");

                DocumentReference categoryDoc = cuisineCategoryDb.document(selectedCategory);

                categoryDoc.collection("products")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    HashMap<String, Object> product = new HashMap<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        product.put(document.getId(),document.getData());
                                    }

                                    Intent listIntent = new Intent(Search.this, ProductList.class);

                                    listIntent.putExtra("cuisineId",selectedCategory);

                                    listIntent.putExtra("productMap",product);

                                    startActivity(listIntent);

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if()
                getMap();
            }
        });
    }

    private void categoriesSetter(){

        CollectionReference cuisineCategoryDb = db.collection("ShopCuisineProduct");

        cuisineCategoryDb.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //list.add(document.getId());
                        System.out.println(document.getId() + "  " + document.getData().get("name"));
                        setCategories.add((String) document.getData().get("name"));
                    }
                   // Log.d(TAG, list.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }



    private void getMap() {
        shopReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double latitude = 0;
                        double longitude = 0;
                        String shopName = "";
                        Map<String, Object> locationPerCategory; // trabalhar nisso
                        LatLng position;
                        Bundle args = new Bundle();
                        Intent categoryIntent = new Intent(Search.this, MapsActivity.class);

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                        if (document.exists()) {
//                                            Map<String, Object> data = document.getData();
//                                            Map<String, Object> product = (Map<String, Object>) data.get("ShopCuisineProduct");
//                                            Map<String, Object> category = (Map<String, Object>) data.get("CuisineCategory");
//                                            for (Map.Entry<String, Object> entry : category.entrySet()) {
//                                                for (Map.Entry<String, Object> entry2 : product.entrySet()) {
//                                                    Map<String, Object> productData = (Map<String, Object>) entry2.getValue();
//                                                    for (Map.Entry<String, Object> dataEntry : productData.entrySet()) {
//                                                        if (dataEntry.getKey().equals("Name") && entry.getValue().equals(selectedCategory)) {
//                                                            duplicatesProducts.add(dataEntry.getValue().toString());
//                                                            Log.d("TAG", dataEntry.getValue().toString());
//                                                        }

                                if (document.exists()) {
                                    Map<String, Object> shop = document.getData();
                                    for (Map.Entry<String, Object> entry : shop.entrySet()) {
                                        if (entry.getKey().equals("ShopCuisineProfile")) {
                                            Map<String, Object> profileMap = (Map<String, Object>) entry.getValue();
                                            for (Map.Entry<String, Object> dataEntry : profileMap.entrySet()) {
                                                if (dataEntry.getKey().equals("location")) {
//                                                    Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                    GeoPoint geoPoint = (GeoPoint) dataEntry.getValue();
                                                    latitude = geoPoint.getLatitude();
                                                    longitude = geoPoint.getLongitude();

//                                                    locationPerCategory = (Map<String, Object>) dataEntry.getValue();
                                                    position = new LatLng(latitude, longitude);
                                                    args.putParcelable("longLat_dataProvider",  position);
                                                    categoryIntent.putExtras(args);
                                                    categoryIntent.putExtra("name_dataProvider", shopName);

                                                    Log.d("TAG", dataEntry.getValue().toString());
                                                }
                                                if (dataEntry.getKey().equals("Name")) {
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
                        startActivity(categoryIntent);
                    }
                });
    }

    private void productsFinder(String selectedCategory) {
        duplicatesProducts.clear();
        shopReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Map<String, Object> data = document.getData();
                                    Map<String, Object> product = (Map<String, Object>) data.get("ShopCuisineProduct");
                                    Map<String, Object> category = (Map<String, Object>) data.get("CuisineCategory");
                                    for (Map.Entry<String, Object> entry : category.entrySet()) {
                                        for (Map.Entry<String, Object> entry2 : product.entrySet()) {
                                            Map<String, Object> productData = (Map<String, Object>) entry2.getValue();
                                            for (Map.Entry<String, Object> dataEntry : productData.entrySet()) {

                                                /*
                                                String mapProductName = "";
                                                String mapProductDescription = "";
*/
                                                if (dataEntry.getKey().equals("Name") && entry.getValue().equals(selectedCategory)) {
                                                    duplicatesProducts.add(dataEntry.getValue().toString());
                                                  //  Log.d("TAG", dataEntry.getValue().toString());

                                                }
/*
                                                ShopCuisineProduct mapProduct = new ShopCuisineProduct(mapProductName,mapProductDescription,R.drawable.brazilian);

                                                listProducts.add(mapProduct);

                                                System.out.println(mapProduct.name + " - " + mapProduct.description);

                                                System.out.println(dataEntry.getValue());
*/
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
                        data = "";
                        setProducts.clear();
                        Set<String> set = new HashSet<>(duplicatesProducts);
                        for (String p : set) {
                            setProducts.add(p);
                        }
                        Collections.sort(setProducts, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(
                                        o2);
                            }
                        });
                        data += duplicatesProducts;
                    }
                });
    }



    private void categoryFinder() {
        duplicatesCategories.clear();
        categories = "";
        shopReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                                    Log.d("TAGS", dataEntry.getValue().toString());
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
                        for (String p : set) {
                            setCategories.add(p);
                        }
                        Collections.sort(setCategories, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(
                                        o2);
                            }
                        });
                        categories += duplicatesCategories;
                    }
                });
    }
}