package com.example.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.demo.model.User;
import com.google.gson.Gson;
import java.util.Map;

public class SharedPreferencesManager {
    private static final String TAG = "SharedPreferencesManager";
    private static final String PREF_NAME = "DemoPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_AVATAR = "avatar";

    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    public SharedPreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        debugPrintAllPreferences();
    }

    private void debugPrintAllPreferences() {
        Map<String, ?> allPrefs = preferences.getAll();
        Log.d(TAG, "=== Current SharedPreferences State ===");
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            Log.d(TAG, entry.getKey() + ": " + entry.getValue());
        }
        Log.d(TAG, "=====================================");
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveToken(String token) {
        Log.d(TAG, "Saving token: " + (token != null ? "not null" : "null"));
        preferences.edit()
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_IS_LOGGED_IN, token != null && !token.isEmpty())
                .apply();
        debugPrintAllPreferences();
    }

    public String getToken() {
        Log.d(TAG, "Getting token: " + (preferences.getString(KEY_TOKEN, null) != null ? "not null" : "null"));
        return preferences.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        Log.d(TAG, "Saving user: " + user.getEmail());
        preferences.edit()
                .putString(KEY_USER, gson.toJson(user))
                .putString(KEY_EMAIL, user.getEmail())
                .putString(KEY_USERNAME, user.getUsername())
                .putInt(KEY_USER_ID, user.getId())
                .putString(KEY_ROLE, user.getRole())
                .apply();
        debugPrintAllPreferences();
    }

    public User getUser() {
        String userJson = preferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        String token = getToken();
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);
        
        Log.d(TAG, "Checking login state - Token exists: " + (token != null && !token.isEmpty()) + 
              ", isLoggedIn flag: " + isLoggedIn);
        
        // If either token is missing or isLoggedIn flag is false, user is not logged in
        if (token == null || token.isEmpty() || !isLoggedIn) {
            Log.d(TAG, "User is not logged in - Token missing or isLoggedIn flag is false");
            return false;
        }
        
        Log.d(TAG, "User is logged in - Valid token and isLoggedIn flag present");
        return true;
    }

    public void clearUserData() {
        Log.d(TAG, "Clearing all user data");
        preferences.edit().clear().apply();
        debugPrintAllPreferences();
    }

    public void saveUsername(String username) {
        Log.d(TAG, "Saving username: " + username);
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public void saveEmail(String email) {
        Log.d(TAG, "Saving email: " + email);
        preferences.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }

    public String getPhone() {
        return preferences.getString(KEY_PHONE, null);
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, null);
    }

    public String getAvatar() {
        return preferences.getString(KEY_AVATAR, null);
    }
}