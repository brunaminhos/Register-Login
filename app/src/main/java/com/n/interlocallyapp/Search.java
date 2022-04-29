package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Shops shop = document.toObject(Shops.class);
//                                shop.setId(document.getId());
//
//                                Toast.makeText(getApplicationContext(), document.getData().get("ShopOwner").toString() , Toast.LENGTH_SHORT).show();


                                Map<String, Object> profile = (Map<String, Object>) document.getData().get("ShopCuisineProfile");
                                Map<Double, Object> location = (Map<Double, Object>) document.getData().get("ShopCuisineProfile");
                                Map<Double, Object> categories = (Map<Double, Object>) document.getData().get("CuisineCategory");

//                                String[] values = categories.values().toArray(new String[0]);
//
//                                String id = document.getId();
//                                String name = (String) profile.get("Name");
//                                double latitude = (double) location.get("latitude");
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

                            textViewData.setText(data);
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