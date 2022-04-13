package com.example.taverncrawler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taverncrawler.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews)
    {
       this.context = context;
       this.reviews = reviews;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        ReviewHolder reviewHolder = (ReviewHolder) holder;
        reviewHolder.tvReviewTitle.setText(review.getTitle());
        reviewHolder.tvReviewBody.setText(review.getBody());
        reviewHolder.reviewRatingBar.setRating((float)review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addAll(List<Review> reviews) {
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }
}
