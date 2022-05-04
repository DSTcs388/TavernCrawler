package com.example.taverncrawler.models;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.math.BigDecimal;
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

    public Bar(JSONObject jsonObject, double latitude, double longitude) throws JSONException {
        this.name = jsonObject.getString("name");
        this.vicinity = jsonObject.getString("vicinity");
        this.latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        this.longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        this.rating = jsonObject.getDouble("rating");
        this.user_ratings_total = jsonObject.getInt("user_ratings_total");
        Location loc1 = new Location(""); loc1.setLatitude(this.latitude); loc1.setLongitude(this.longitude);
        Location loc2 = new Location(""); loc2.setLatitude(latitude); loc2.setLongitude(longitude);
        this.distance = distanceInMiles(loc1, loc2);

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
        DecimalFormat df = new DecimalFormat("#.##");
        double d = Double.parseDouble(df.format(distance));
        Log.i("Bar", String.valueOf(d));
        return d;
    }
    private double distanceInMiles(Location loc1, Location loc2) {
        return (loc1.distanceTo(loc2))/1609.344;
    }
}