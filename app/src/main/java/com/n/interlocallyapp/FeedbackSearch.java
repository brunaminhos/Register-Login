package com.n.interlocallyapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
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

public class FeedbackSearch extends AppCompatActivity {

    private String data = "";
    private RatingBar ratingBar2;
    private EditText feedbackRating;
    private Button saveBtn;
    private TextView tvFeedback, shopErrorMessage;
    private ArrayAdapter<String> adapterShops;
    private List<String> setShops = new ArrayList<>();
    private AutoCompleteTextView autoCompleteShopsDropDown;
    private double rateGiven;
    private String selectedShop;
    private final List<String> duplicatesShops = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_search);

        shopsFinder();

        shopErrorMessage = findViewById(R.id.shopErrorMessage);
        ratingBar2 = findViewById(R.id.ratingBar2);
        saveBtn = findViewById(R.id.feedbackBtn);
        tvFeedback = findViewById(R.id.tvFeedback);
        autoCompleteShopsDropDown = findViewById(R.id.auto_complete_shops_dropdown);
        adapterShops = new ArrayAdapter<>(this, R.layout.dropdown_item, setShops);
        autoCompleteShopsDropDown.setAdapter(adapterShops);

        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {

                rateGiven = ratingBar.getRating();

                if (rateGiven <= 1) {
                    tvFeedback.setText("Very Dissatisfied");
                } else if (rateGiven <=2) {
                    tvFeedback.setText("Dissatisfied");
                }else if (rateGiven <=3) {
                    tvFeedback.setText("It is alright");
                } else if (rateGiven <=4) {
                    tvFeedback.setText("Satisfied");
                } else if (rateGiven <=5) {
                    tvFeedback.setText("Very Satisfied");
                }
            }
        });

        shopsDropdown();
        Log.d("TAG_1", "Shop selected: " + selectedShop + ", rating: " + rateGiven);


        // validate user input
        registration();
        Log.d("TAG_1", "Shop selected: " + selectedShop + ", rating: " + rateGiven);


    }

    private void shopsDropdown() {
        autoCompleteShopsDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedShop = parent.getItemAtPosition(position).toString();

//                Log.d("TAG_1", "Shop selected: " + selectedShop + ", rating: " + rateGiven);
            }
        });
    }

    // Register rating on Firebase
    private void registration() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Rating saveRatings = new Rating(rateGiven, selectedShop);

                if(selectedShop == null || rateGiven == 0){
                    shopErrorMessage.setText("Shop and rating need to be selected.");
                }else {

                    Log.d("TAG_1", "Shop selected: " + selectedShop + ", rating: " + rateGiven);

                    saveRatings.createRating();
                    Toast.makeText(FeedbackSearch.this, "Rating Created: " + String.valueOf(rateGiven) + " for shop: " + selectedShop, Toast.LENGTH_SHORT).show();
                    Intent mainMenuIntent = new Intent(FeedbackSearch.this, MainActivity.class);
                    startActivity(mainMenuIntent);
                }
            }
        });
    }

    private void shopsFinder() {
        shopReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    List<String> transList = new ArrayList<>();
                                    Map<String, Object> data = document.getData();
                                    Map<String, Object> profile = (Map<String, Object>) data.get("ShopCuisineProfile");
                                    for (Map.Entry<String, Object> entry : profile.entrySet()) {
                                        if (entry.getKey().equals("Name")){
                                            duplicatesShops.add(entry.getValue().toString());
//                                            Toast.makeText(Search.this, dataEntry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", entry.getValue().toString());
                                        }
                                    }
                                }
                            }
                        }
                        Set<String> set = new HashSet<>(duplicatesShops);
                        for (String p : set) {
                            setShops.add(p);
                        }
                        Collections.sort(setShops, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(
                                        o2);
                            }
                        });
                        data += setShops;
                    }
                });
    }
}
