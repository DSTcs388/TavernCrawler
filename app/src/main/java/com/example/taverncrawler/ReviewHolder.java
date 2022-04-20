package com.example.taverncrawler;

import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewHolder extends RecyclerView.ViewHolder{
    TextView tvReviewTitle;
    TextView tvReviewBody;
    RatingBar reviewRatingBar;
    RelativeLayout container;

    public ReviewHolder(@NonNull View itemView)
    {
        super(itemView);
        tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
        tvReviewBody = itemView.findViewById(R.id.tvReviewBody);
        reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
        container = itemView.findViewById(R.id.item_review);
    }
}
