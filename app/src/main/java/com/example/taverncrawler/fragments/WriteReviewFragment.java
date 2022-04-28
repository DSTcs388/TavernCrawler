package com.example.taverncrawler.fragments;


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
import androidx.fragment.app.Fragment;

import com.example.taverncrawler.R;
import com.example.taverncrawler.models.Review;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class WriteReviewFragment extends Fragment {

    private EditText etReviewTitle;
    private EditText etReviewBody;
    private RatingBar ratingBar;
    private Button buttonSubmit;
    public static final String TAG = "WriteReviewFragment";

    public WriteReviewFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_write_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etReviewTitle = (EditText) view.findViewById(R.id.etReviewTitle);
        etReviewBody = (EditText) view.findViewById(R.id.etReviewBody);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);

        Bundle bundle = getArguments();
        String name = bundle.getString("name");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etReviewTitle.getText().toString();
                String body = etReviewBody.getText().toString();
                double rating = ratingBar.getRating();
                if (title.isEmpty() || body.isEmpty())
                {
                    Toast.makeText(getContext(), "Please write and title your review first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rating == 0) {
                    Toast.makeText(getContext(), "You need to add a rating!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveReview(title, body, rating, name);
                etReviewBody.setText("");
                etReviewTitle.setText("");
                ratingBar.setRating(0);
            }
        });
    }

    private void saveReview(String title, String body, Double rating, String barName)
    {
        Review review = new Review();
        review.setTitle(title);
        review.setBody(body);
        review.setRating(rating);
        review.setBar(barName);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("WriteReviewActivity", "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Review saved successfully!", Toast.LENGTH_SHORT).show();
                    ReviewsFragment fragment = new ReviewsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", barName);
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });
    }
}
