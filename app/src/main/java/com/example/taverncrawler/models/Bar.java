package com.example.taverncrawler.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Bar {
    String name, vicinity;
    double latitude, longitude, rating;
    int user_ratings_total;

    public Bar() {
        //Empty Constructor to be used by Parcel
    }

    public Bar(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        vicinity = jsonObject.getString("vicinity");
        latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        rating = jsonObject.getDouble("rating");
        user_ratings_total = jsonObject.getInt("user_ratings_total");

    }

    public static List<Bar> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Bar> bars = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            bars.add(new Bar(jsonArray.getJSONObject(i)));
        }
        return bars;
    }

    public String getName() { return name; }

    public String getVicinity() { return vicinity; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public double getRating() { return rating; }

    public int getUserRatingsTotal() { return user_ratings_total; }

}
