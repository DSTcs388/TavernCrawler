package com.example.taverncrawler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taverncrawler.R;
import com.example.taverncrawler.models.Bar;

import org.parceler.Parcel;
import org.parceler.Parcels;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    TextView tvBarName;
    TextView tvRatingsTotal;
    TextView tvRating;
    TextView tvVicinity;
    Button btnViewReviews;
    private Bar bar;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBarName = (TextView) view.findViewById(R.id.tvBarName);
        tvRatingsTotal = (TextView) view.findViewById(R.id.tvRatingsTotal);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvVicinity = (TextView) view.findViewById(R.id.tvVicinity);
        btnViewReviews = (Button) view.findViewById(R.id.btnViewReviews);
        Bundle bundle = getArguments();
        if (bundle != null) {
            bar = (Bar) Parcels.unwrap(bundle.getParcelable("bar"));
        }
        tvBarName.setText(bar.getName());
        tvRatingsTotal.setText("Total Number of Ratings: " + String.valueOf(bar.getUserRatingsTotal()));
        tvRating.setText("User Rating: " + String.valueOf(bar.getRating()));
        tvVicinity.setText(String.valueOf(bar.getVicinity()));
        btnViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsFragment fragment = new ReviewsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", bar.getName());
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });


    }
}