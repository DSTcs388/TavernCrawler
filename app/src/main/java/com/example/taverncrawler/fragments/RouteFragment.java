package com.example.taverncrawler.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taverncrawler.R;
import com.example.taverncrawler.RestClient;
import com.example.taverncrawler.viewmodels.RouteViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * An {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = "RouteFragment";
    private double longitude, latitude;
    private final RestClient client = new RestClient();
    private HashMap<String, List<Double>> selectedBars = new HashMap<>();
    private RouteViewModel routeViewModel;
    private GoogleMap map;
    private List<String> directions = new ArrayList<>();
    private List<String> barNames = new ArrayList<>();
    private List<List<LatLng>> directionsDecoded = new ArrayList<>();

    //Required empty public constructor
    public RouteFragment() {}

    /**
     * An {@link FunctionalInterface} that is used to enforce a small amount of synchronization
     * If this interface is not used, the {@param directions} list will not be populated with a list of encoded Polylines, and the routes will not be drawn.
     */
    public interface DirectionsCallbackInterface {
        void onListPopulated();
    }

    /**
     * {@param routeViewModel} must be initialized here, as there is a low chance that an error will occur if it is not.
     * The reason why this occurs is best left to minds greater than mine.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeViewModel = new ViewModelProvider(requireActivity()).get(RouteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Observes the list of selected bars for as long as the MainActivity remains active. On change, updates list of bars
        routeViewModel.getSelectedBars().observeForever(new Observer<HashMap<String, List<Double>>>() {
            @Override
            public void onChanged(HashMap<String, List<Double>> stringListHashMap) {
                selectedBars = stringListHashMap;
                addNamesToList(selectedBars);
                printMap(selectedBars);
            }
        });

        //Longitude and Latitude to be used for current location
        Bundle bundle = getArguments();
        latitude = bundle.getDouble("lat");
        longitude = bundle.getDouble("longi");
        Log.i(TAG, "Current Latitude: " + String.valueOf(latitude) + "Current Longitude: " + String.valueOf(longitude));

        //Initializes map fragment for display
        SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    //Prints out HashMap. Utility function used to check that the list of selected bars is being accurately updated
    private void printMap(HashMap<String, List<Double>> bars) {
        Iterator<String> iterator = bars.keySet().iterator();
        while(iterator.hasNext()) {
            Log.i(TAG, iterator.next());
        }
        Iterator<List<Double>> iterator2 = bars.values().iterator();
        while(iterator2.hasNext()) {
            Log.i(TAG, iterator2.next().toString());
        }
    }

    //Utility function to get a list of the names of each bar
    private void addNamesToList(HashMap<String, List<Double>> bars) {
        Iterator<String> iterator = bars.keySet().iterator();
        barNames = new ArrayList<>();
        while(iterator.hasNext()) {
            barNames.add(iterator.next());
        }
    }

    //Creates the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng currentPosition = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(currentPosition).title("Current Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
        updateRoute(map, selectedBars);
    }

    //Creates a series of Asynchronous GET requests in order to fetch the directions for each bar in the list of bars
    private void updateRoute(GoogleMap googleMap, HashMap<String, List<Double>> selectedBars) {
        Iterator<List<Double>> iterator = selectedBars.values().iterator();
        List<Double> list = new ArrayList<>();
        while(iterator.hasNext()) {
            list = iterator.next();
            Log.i(TAG, "Current Iteration: " + list.toString());
            //DirectionsCallbackInterface is used here in order to force decodeDirections to execute if and only if directions is populated with data
            getDirections(list.get(0), list.get(1), new DirectionsCallbackInterface() {
                @Override
                public void onListPopulated() {
                    decodeDirections(googleMap);
                }
            });
        }
    }

    /**
     * Performs the GET request for fetching the encoded String of Polylines to be drawn on the map
     * @param c1 This is the first coordinate (In our case, latitude)
     * @param c2 This is the second coordinate (In our case, longitude)
     * @param directionsCallbackInterface The interface's onListPopulated() method is only called when the asynchronous request is fully finished
     * In order to prevent decodeDirections() from being called multiple times, a check must be performed to make sure that it is only called after the all bars in selectedBars have been parsed into the directions list
     */
    private void getDirections(Double c1, Double c2, DirectionsCallbackInterface directionsCallbackInterface) {
        RequestParams params = new RequestParams();
        params.put("origin", String.valueOf(latitude) + "," + String.valueOf(longitude));
        params.put("destination", String.valueOf(c1) + "," + String.valueOf(c2));
        params.put("key", getString(R.string.places_api_key));
        client.get("https://maps.googleapis.com/maps/api/directions/json?", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "" + statusCode);
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    JSONArray data = response.getJSONArray("routes");
                    for(int i = 0; i < data.length(); i++) {
                        String encodedPolyline = data.getJSONObject(i).getJSONObject("overview_polyline").getString("points");
                        directions.add(encodedPolyline);
                    }
                }
                catch (JSONException e) {
                    Log.e(TAG, "JSONException when parsing JSON object", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, ""+statusCode+":"+ error);
            }

            @Override
            public void onFinish() {
                if(directions.size() >= selectedBars.size()) {
                    directionsCallbackInterface.onListPopulated();
                }
            }
        });
    }
    //Decodes the directions into a list of LatLng coordinates that constitute the route from the current location to the bar
    private void decodeDirections(GoogleMap googleMap) {
        for(int i = 0; i < directions.size(); i++) {
            String encodedString = directions.get(i);
            List<LatLng> coords = PolyUtil.decode(encodedString);
            directionsDecoded.add(coords);
        }
        Log.i(TAG, directionsDecoded.toString());
        drawPolylines(googleMap);
    }
    //Adds polyline to map
    private void drawPolylines(GoogleMap googleMap) {
        for(int i  = 0; i < directionsDecoded.size(); i++) {
            googleMap
            .addMarker(new MarkerOptions()
                    .position(directionsDecoded
                            .get(i)
                            .get(directionsDecoded
                                    .get(i)
                                    .size()-1))
                    .title(barNames.get(i)));
            googleMap.addPolyline(new PolylineOptions().addAll(directionsDecoded.get(i)));
        }
    }
}