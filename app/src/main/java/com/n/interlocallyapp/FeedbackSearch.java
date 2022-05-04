package com.n.interlocallyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private Button feedbackBtn;
    private TextView tvFeedback;
    private ArrayAdapter<String> adapterShops;
    private List<String> setShops = new ArrayList<>();
    private AutoCompleteTextView autoCompleteShopsDropDown;
    private List<String> duplicatesShops= new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference shopReference = db.collection("Shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_search);

        shopsFinder();

        ratingBar2 = findViewById(R.id.ratingBar2);
        feedbackRating = findViewById(R.id.feedbackRating);
        feedbackBtn = findViewById(R.id.feedbackBtn);
        tvFeedback = findViewById(R.id.tvFeedback);
        autoCompleteShopsDropDown = findViewById(R.id.auto_complete_shops_dropdown);
        adapterShops = new ArrayAdapter<>(this, R.layout.dropdown_item, setShops);
        autoCompleteShopsDropDown.setAdapter(adapterShops);

        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    tvFeedback.setText("Very Dissatisfied");
                } else if (rating == 1) {
                    tvFeedback.setText("Dissatisfied");
                } else if (rating == 2 || rating == 3) {
                    tvFeedback.setText("It is alright");
                } else if (rating == 4) {
                    tvFeedback.setText("Satisfied");
                } else if (rating == 5) {
                    tvFeedback.setText("Very Satisfied");
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
