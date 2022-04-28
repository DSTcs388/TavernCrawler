package com.example.taverncrawler.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String Image = "image";
    public static final String User = "username";
    public static final String CreatedAt = "createdAt";
    public static final String Email = "email";
    public static final String EmailVerified = "emailVerified";

    public ParseFile getImage() {
        return getParseFile(Image);
    }

    public void setImage(ParseFile parseFile) {
        put(Image, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(User);
    }

    public void setUser(ParseUser parseUser) {
        put(User, parseUser);
    }

    public boolean getEmailVerified() {
        return getBoolean(EmailVerified);
    }
    public void setEmailVerified(boolean verified) {
        put(EmailVerified, verified);
    }

    @Override
    public String getEmail() {
        return getString(Email);
    }

    @Override
    public void setEmail(String email) {
        put(Email, email);
    }
}
