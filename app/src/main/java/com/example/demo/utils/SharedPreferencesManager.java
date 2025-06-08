package com.example.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.demo.model.User;
import com.google.gson.Gson;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "DemoAppPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";

    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    private SharedPreferencesManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void saveToken(String token) {
        preferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        String userJson = gson.toJson(user);
        preferences.edit().putString(KEY_USER, userJson).apply();
    }

    public User getUser() {
        String userJson = preferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public void clearUserData() {
        preferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_USER)
                .apply();
    }
} 