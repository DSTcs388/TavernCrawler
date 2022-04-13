package com.example.taverncrawler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taverncrawler.R;

public class ReviewFragment extends Fragment{
    private EditText etReview;
    private Button btnSubmit;
    private TextView tvBarname;
    public void ProfileFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.review_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSubmit = view.findViewById(R.id.btnReview);
        etReview = view.findViewById(R.id.etvReview);
        tvBarname = view.findViewById(R.id.tvBarnamereview);

    }
}
