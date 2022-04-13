package com.example.taverncrawler.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Review")
public class Review extends ParseObject{
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_RATING = "rating";
    //public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getTitle() {
        return getString(KEY_TITLE);
    }
    public void setTitle(String title)
    {
        put(KEY_TITLE, title);
    }

    public String getBody() {
        return getString(KEY_BODY);
    }
    public void setBody(String body)
    {
        put(KEY_BODY, body);
    }

    public double getRating() {
        return getDouble(KEY_RATING);
    }
    public void setRating(double rating)
    {
        put(KEY_RATING, rating);
    }

    /*public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }*/

}
