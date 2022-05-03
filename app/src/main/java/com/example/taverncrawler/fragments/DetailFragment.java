package com.example.taverncrawler.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taverncrawler.R;
import com.example.taverncrawler.RestClient;
import com.example.taverncrawler.models.Bar;
import com.example.taverncrawler.models.Review;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private TextView tvBarName;
    private TextView tvRatingsTotal;
    private TextView tvGoogleReviews;
    private TextView tvVicinity;
    private TextView tvYelpReviews;
    private TextView tvUserReviews;
    private TextView tvOperatingHours;
    private TextView tvPhone;
    private ImageView ivBarPicture;
    private Button btnViewReviews;

    private Bar bar;
    private final AsyncHttpClient client = RestClient.client;
    public static final String TAG = "DetailFragment";

    private String phoneNumber;
    private String id;
    private String imageUrl;
    private String startHours;
    private String endHours;

    private int yelpReviews;
    private int userReviews;
    private int googleReviews;
    private int count = 0;
    private int totalReviews;

    private double userRating;

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
        tvGoogleReviews = (TextView) view.findViewById(R.id.tvGoogleReviews);
        tvYelpReviews = (TextView) view.findViewById(R.id.tvYelpReviews);
        tvUserReviews = (TextView) view.findViewById(R.id.tvUserReviews);
        tvOperatingHours = (TextView) view.findViewById(R.id.tvOperatingHours);
        tvVicinity = (TextView) view.findViewById(R.id.tvVicinity);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        ivBarPicture = (ImageView) view.findViewById(R.id.ivBarPicture);
        btnViewReviews = (Button) view.findViewById(R.id.btnViewReviews);

        ProgressDialog loading = new ProgressDialog(requireActivity());
        loading.setMessage("Loading information...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            bar = (Bar) Parcels.unwrap(bundle.getParcelable("bar"));
        }

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
        RequestParams params = new RequestParams();
        params.add("latitude",  String.valueOf(bar.getLatitude()));
        params.add("longitude", String.valueOf(bar.getLongitude()));
        params.add("term", bar.getName());
        params.add("categories", "bars");
        client.addHeader("Authorization", "Bearer " + getString(R.string.yelp_api_key));
        client.get("https://api.yelp.com/v3/businesses/search?", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "Successful");
                Log.i(TAG, "" + statusCode);
                Log.i(TAG, "" + new String(responseBody));
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    JSONObject data = response.getJSONArray("businesses").getJSONObject(0);
                    id = data.getString("id");
                    Log.i(TAG, id);
                    getDetails(id, client, loading);
                }
                catch (JSONException e) {
                    Log.e(TAG, "JSONExceptions::onViewCreated::", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "GET Failure::onViewCreated::", error);
            }
        });
    }

    private void getDetails(String idNumber, AsyncHttpClient client, ProgressDialog dialog) {
        client.get("https://api.yelp.com/v3/businesses/" + idNumber, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    phoneNumber = response.getString("display_phone");
                    imageUrl = response.getString("image_url");
                    String hours1 = response.getJSONArray("hours").getJSONObject(0).getJSONArray("open").getJSONObject(0).getString("start");
                    String hours2 = response.getJSONArray("hours").getJSONObject(0).getJSONArray("open").getJSONObject(0).getString("end");
                    startHours = timeConversion(hours1);
                    endHours = timeConversion(hours2);
                    yelpReviews = response.getInt("rating");
                    getTotalUserReviews(dialog);
                }
                catch (JSONException e) {
                    Log.e(TAG, "GET Failure::getDetails::", e);
                }
                catch (java.text.ParseException e) {
                    Log.e(TAG, "ParseException::getDetails::", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "GET Failure::getDetails::", error);
            }
        });
    }

    private void displayDetails(ProgressDialog dialog) {
        Glide.with(requireActivity()).load(imageUrl).override(400,400).placeholder(R.drawable.ic_launcher_background).into(ivBarPicture);
        tvBarName.setText(bar.getName());
        tvVicinity.setText(bar.getVicinity());
        tvGoogleReviews.setText("Google Rating: " + String.valueOf(bar.getRating()));
        tvUserReviews.setText("TavernCrawler Rating: " + String.valueOf(userRating));
        tvYelpReviews.setText("Yelp Rating: " + String.valueOf(yelpReviews));
        tvOperatingHours.setText(startHours + " - " + endHours);
        tvPhone.setText(phoneNumber);
        tvRatingsTotal.setText("Total Reviews: " + String.valueOf(totalReviews));
        dialog.dismiss();
    }

    private void combineTotalUserReviews(ProgressDialog dialog) {
        googleReviews = bar.getUserRatingsTotal();
        userReviews = count;
        totalReviews = googleReviews + yelpReviews + userReviews;
        displayDetails(dialog);
    }

    private void getTotalUserReviews(ProgressDialog dialog) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.whereEqualTo("bar", bar.getName());
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ParseException::getTotalUserReviews::", e);
                }
                else {
                    for(Review review : reviews) {
                        userRating = review.getRating();
                        count++;
                    }
                    combineTotalUserReviews(dialog);
                }
            }
        });
    }

    //Converts military time to standard time
    private String timeConversion(String hours) throws java.text.ParseException {
        Date time = new SimpleDateFormat("hhmm").parse(hours);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(time);
    }
}