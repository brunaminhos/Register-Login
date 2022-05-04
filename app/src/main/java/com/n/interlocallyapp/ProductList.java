package com.n.interlocallyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductList extends AppCompatActivity {

    //ArrayList<ShopCuisineProduct> prodList = new ArrayList<ShopCuisineProduct>();
    ListView listview;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent myIntent = getIntent(); // gets the previously created intent

        String cuisineId = myIntent.getExtras().getString("cuisineId");

        HashMap<String, Object> hashMap = (HashMap<String, Object>)myIntent.getSerializableExtra("productMap");

        ArrayList<ShopCuisineProduct> prodList = createProductList(cuisineId,hashMap);

        System.out.println("WORKING" + hashMap.get("guarana"));

        Toast.makeText(getApplicationContext(), "Item: " + cuisineId, Toast.LENGTH_SHORT).show();

        listview = (ListView) findViewById(R.id.customListView);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), prodList);
        listview.setAdapter(customBaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProductList.this).create();


              //  CollectionReference cuisineCategoryDb = db.collection("ShopCuisineProfile");

               // DocumentReference profileDoc = cuisineCategoryDb.document(selectedCategory);

                alertDialog.setTitle("Item clicked");
                alertDialog.setMessage("PRODUCT CLICKED POSITION " + position);




                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


    }

    private ArrayList<ShopCuisineProduct> createProductList(String category, HashMap<String, Object> productListDb){


        ArrayList<ShopCuisineProduct> listProducts = new ArrayList<>();

        for (Map.Entry<String, Object> iterateMap : productListDb.entrySet()) {


            HashMap<String, Object> valuesList = (HashMap<String, Object>) iterateMap.getValue();

            System.out.println(valuesList);

            String prodId = iterateMap.getKey();
            String prodName = (String) valuesList.get("prod_name");
            String prodDescription = (String) valuesList.get("prod_description");
            String prodImgLink = (String) valuesList.get("prod_img");

            listProducts.add(new ShopCuisineProduct(prodId,prodName,prodDescription,prodImgLink));

        }

        return listProducts;

    }
}