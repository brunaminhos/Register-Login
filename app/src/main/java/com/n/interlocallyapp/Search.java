package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Search extends AppCompatActivity {

    private String data = "";
    private String categories = "";
    private String selectedCategory;
    private AutoCompleteTextView autoCompleteCategoryTxt, autoCompleteProductsTxt;
    private ArrayAdapter<String> adapterCategories;
    private ArrayAdapter<String> adapterProducts;
    private List<String> setCategories = new ArrayList<>();
    private List<String> setProducts = new ArrayList<>();
    private List<String> duplicatesCategories = new ArrayList<>();
    private List<String> duplicatesProducts= new ArrayList<>();

    private Button loadButton;
    private TextView textViewData, textViewCategories;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        categoryFinder();

        loadButton = findViewById(R.id.loadBtn);
        textViewData = findViewById(R.id.textViewData);
        textViewCategories = findViewById(R.id.textViewCategories);
        autoCompleteCategoryTxt = findViewById(R.id.auto_complete_category_txt);
        autoCompleteProductsTxt = findViewById(R.id.auto_complete_product_txt);

        adapterCategories = new ArrayAdapter<String>(this, R.layout.dropdown_item, setCategories);
        adapterProducts = new ArrayAdapter<>(this, R.layout.dropdown_item, setProducts);

        adapterCategories.setDropDownViewResource(R.layout.dropdown_item);

        autoCompleteCategoryTxt.setAdapter(adapterCategories);
        autoCompleteProductsTxt.setAdapter(adapterProducts);

        autoCompleteCategoryTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

                productsFinder(selectedCategory);
            }
        });


        autoCompleteProductsTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewCategories.setText(categories);
                textViewData.setText(data);
            }
        });
    }

    private void productsFinder(String category) {
        shopReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
//                                    List<String> transList = new ArrayList<>();
                                    Map<String, Object> data = document.getData();
                                    Map<String, Object> product = (Map<String, Object>) data.get("ShopCuisineProduct");
                                    for (Map.Entry<String, Object> entry : product.entrySet()) {
                                        Map<String, Object> productData = (Map<String, Object>) entry.getValue();
                                        for (Map.Entry<String, Object> dataEntry : productData.entrySet()) {
                                            if (dataEntry.getKey().equals("Name")) {
                                                duplicatesProducts.add(dataEntry.getValue().toString());
//                                                Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                Log.d("TAG", dataEntry.getValue().toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
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
                        data += setProducts;
                    }
                });
    }

    private void categoryFinder() {
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
                        categories += setCategories;
                    }
                });
    }
}