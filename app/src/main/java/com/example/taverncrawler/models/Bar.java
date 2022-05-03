package com.example.taverncrawler.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Bar {
    String name, vicinity;
    double latitude, longitude, rating, distance;
    int user_ratings_total;

    public Bar() {
        //Empty Constructor to be used by Parcel
    }

    public Bar(JSONObject jsonObject, double longitude, double latitude) throws JSONException {
        this.name = jsonObject.getString("name");
        this.vicinity = jsonObject.getString("vicinity");
        this.latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        this.longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        this.rating = jsonObject.getDouble("rating");
        this.user_ratings_total = jsonObject.getInt("user_ratings_total");
        this.distance = distance(latitude, longitude, this.latitude, this.longitude, 0, 0);

    }

    public static List<Bar> fromJSONArray(JSONArray jsonArray, double latitude, double longitude) throws JSONException {
        List<Bar> bars = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            bars.add(new Bar(jsonArray.getJSONObject(i), latitude, longitude));
        }
        return bars;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public int getUserRatingsTotal() {
        return user_ratings_total;
    }

    public double getDistance() {
        Log.i("Bar", String.valueOf(distance));
        DecimalFormat df = new DecimalFormat("#.##");
        double d = Double.parseDouble(df.format(distance));
        Log.i("Bar", String.valueOf(d));
        return d;
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}