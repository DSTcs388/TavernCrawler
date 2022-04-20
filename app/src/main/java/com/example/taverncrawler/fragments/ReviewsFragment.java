package com.example.taverncrawler.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taverncrawler.R;
import com.example.taverncrawler.ReviewAdapter;
import com.example.taverncrawler.WriteReviewActivity;
import com.example.taverncrawler.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private Button buttonWrite;
    private RecyclerView rvReviews;
    protected ReviewAdapter adapter;
    protected List<Review> allReviews;

    public ReviewsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonWrite = view.findViewById(R.id.buttonWriteReview);
        rvReviews = view.findViewById(R.id.rvReviews);
        allReviews = new ArrayList<>();
        adapter = new ReviewAdapter(getContext(), allReviews);
        Log.i("ReviewsFragment", adapter.toString());
        Log.i("ReviewsFragment", rvReviews.toString());
        rvReviews.setAdapter(adapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), WriteReviewActivity.class);
                startActivity(intent);
            }
        });
        queryReviews();
    }

    protected void queryReviews()
    {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.setLimit(10);
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e != null)
                {
                    Log.e("ReviewsFragment","Issue with getting posts", e);
                    return;
                }
                for (Review review : reviews)
                {
                    Log.i("ReviewsFragment", "Review: " + review.getTitle() + " " + review.getBody() + " " + review.getRating());
                }
                allReviews.addAll(reviews);
                adapter.notifyDataSetChanged();
            }
        });
    }
}


