package com.example.taverncrawler.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taverncrawler.R;
import com.example.taverncrawler.RestClient;
import com.example.taverncrawler.adapters.FeedAdapter;
import com.example.taverncrawler.viewmodels.RouteViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * The LiveData list does not need to be observed in this fragment or its adapter, for now. This may change in the future.
*/
public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";
    private RecyclerView rvFeed;
    private final RestClient client = new RestClient();
    public double latitude, longitude;
    private HashMap<String, List<Double>> bars = new HashMap<>();
    private RouteViewModel routeViewModel;

    public FeedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeViewModel = new ViewModelProvider(requireActivity()).get(RouteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        Context context = view.getContext();

        //Get longitude and latitude
        Bundle bundle = getArguments();
        longitude = bundle.getDouble("longi");
        latitude = bundle.getDouble("lat");
        Toast.makeText(context, "Longitude: " + String.valueOf(longitude) + "\n Latitude: " + String.valueOf(latitude), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Longitude: " + String.valueOf(longitude) + "\n Latitude: " + String.valueOf(latitude));

        //LiveData to be observed in adapter
        LifecycleOwner owner = getViewLifecycleOwner();
        routeViewModel.getSelectedBars().observeForever(new Observer<HashMap<String, List<Double>>>() {
            @Override
            public void onChanged(HashMap<String, List<Double>> stringListHashMap) {
                Log.i(TAG, stringListHashMap.toString());
            }
        });


        //Initialize RecyclerView and Adapter
        rvFeed = (RecyclerView) view.findViewById(R.id.rvFeed);
        FeedAdapter feedAdapter = new FeedAdapter(getContext(), bars, routeViewModel, owner);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(context));

        //Initialize SDK
        Places.initialize(getContext(), getString(R.string.places_api_key));
        PlacesClient placesClient = Places.createClient(getContext());
        getNearbyBars(feedAdapter);

        //Return view
        return view;
    }

    private void getNearbyBars(FeedAdapter adapter){
        //Create search parameters
        RequestParams params = new RequestParams();
        params.put("location", String.valueOf(latitude) + "," + String.valueOf(longitude));
        params.put("radius", "2000");
        params.put("type", "bar");
        params.put("key", getString(R.string.places_api_key));
        /**
         * @param url is the base URL for the Google Places API
         * @param AsyncHttpResponseHandler is necessary in order to execute the task asynchronously. Adapter must be notified when data is retrieved
         */
        client.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "" + statusCode);
                Log.i(TAG, new String(responseBody));
                try {
                    //Add bars to list
                    JSONObject response = new JSONObject(new String(responseBody));
                    JSONArray data = response.getJSONArray("results");
                    for(int i = 0; i < data.length(); i++) {
                        List<Double> list = new ArrayList<>();
                        list.add(data.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        list.add(data.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        bars.put(data.getJSONObject(i).getString("name"), list);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e) {
                    Log.i(TAG, "Fail response:" + statusCode + ":" + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, ""+statusCode+":"+ error);
            }
        });
    }
}