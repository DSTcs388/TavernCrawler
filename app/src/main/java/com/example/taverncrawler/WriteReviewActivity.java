package com.example.taverncrawler;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.taverncrawler.databinding.ActivityWriteReviewBinding;
import com.example.taverncrawler.models.Review;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class WriteReviewActivity extends AppCompatActivity {
    private EditText etReviewTitle;
    private EditText etReviewBody;
    private RatingBar ratingBar;
    private Button buttonSubmit;

    private ActivityWriteReviewBinding binding;
/*
    public WriteReviewActivity()
    {
        // Required empty public constructor
    }
*/
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_review, container, false);
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_review);
        etReviewTitle = binding.etReviewTitle;
        etReviewBody = binding.etReviewBody;
        ratingBar = binding.ratingBar;
        buttonSubmit = binding.buttonSubmit;

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etReviewTitle.getText().toString();
                String body = etReviewBody.getText().toString();
                double rating = ratingBar.getRating();
                if (title.isEmpty() || body.isEmpty())
                {
                    Toast.makeText(WriteReviewActivity.this, "Please write and title your review first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rating == 0) {
                    Toast.makeText(WriteReviewActivity.this, "You need to add a rating!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveReview(title, body, rating);
                etReviewBody.setText("");
                etReviewTitle.setText("");
                ratingBar.setRating(0);
            }
        });
    }

    private void saveReview(String title, String body, Double rating)
    {
        Review review = new Review();
        review.setTitle(title);
        review.setBody(body);
        review.setRating(rating);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("WriteReviewActivity", "Error while saving", e);
                    Toast.makeText(WriteReviewActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
