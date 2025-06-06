package com.example.demo.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.demo.AuthActivity;
import com.example.demo.MainActivity;
import com.example.demo.network.LoginRequest;
import com.example.demo.network.LoginResponse;
import com.example.demo.network.RegisterRequest;
import com.example.demo.network.RegisterResponse;
import com.example.demo.network.ApiService;
import com.example.demo.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationModel {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private final Context context;
    private final SharedPreferences preferences;
    private final ApiService apiService;

    public AuthenticationModel(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);
    }

    public void login(String email, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    saveUserData(loginResponse);
                    callback.onSuccess();
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void register(String username, String email, String password, String phone, RegisterCallback callback) {
        RegisterRequest request = new RegisterRequest(username, email, password, phone);
        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    saveUserData(registerResponse);
                    callback.onSuccess();
                } else {
                    callback.onError("Registration failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void logout() {
        // Clear saved data
        preferences.edit().clear().apply();
        
        // Navigate to login screen
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return preferences.contains(KEY_TOKEN);
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, null);
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
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

    private void saveUserData(LoginResponse response) {
        preferences.edit()
                .putString(KEY_TOKEN, response.getToken())
                .putString(KEY_USER_ID, response.getUser().getId())
                .putString(KEY_USERNAME, response.getUser().getUsername())
                .putString(KEY_EMAIL, response.getUser().getEmail())
                .putString(KEY_PHONE, response.getUser().getPhone())
                .putString(KEY_ROLE, response.getUser().getRole())
                .putString(KEY_AVATAR, response.getUser().getAvatar())
                .putString(KEY_CREATED_AT, response.getUser().getCreatedAt())
                .putString(KEY_UPDATED_AT, response.getUser().getUpdatedAt())
                .apply();
    }

    private void saveUserData(RegisterResponse response) {
        preferences.edit()
                .putString(KEY_TOKEN, response.getToken())
                .putString(KEY_USER_ID, response.getUser().getId())
                .putString(KEY_USERNAME, response.getUser().getUsername())
                .putString(KEY_EMAIL, response.getUser().getEmail())
                .putString(KEY_PHONE, response.getUser().getPhone())
                .putString(KEY_ROLE, response.getUser().getRole())
                .putString(KEY_AVATAR, response.getUser().getAvatar())
                .putString(KEY_CREATED_AT, response.getUser().getCreatedAt())
                .putString(KEY_UPDATED_AT, response.getUser().getUpdatedAt())
                .apply();
    }

    public interface LoginCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onError(String message);
    }
}
