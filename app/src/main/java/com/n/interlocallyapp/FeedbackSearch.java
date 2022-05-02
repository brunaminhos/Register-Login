package com.n.interlocallyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedbackSearch extends AppCompatActivity {

    private RatingBar ratingBar2;
    private EditText feedbackRating;
    private Button feedbackBtn;
    private TextView tvFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_search);

        ratingBar2 = findViewById(R.id.ratingBar2);
        feedbackRating = findViewById(R.id.feedbackRating);
        feedbackBtn = findViewById(R.id.feedbackBtn);
        tvFeedback = findViewById(R.id.tvFeedback);

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
}
