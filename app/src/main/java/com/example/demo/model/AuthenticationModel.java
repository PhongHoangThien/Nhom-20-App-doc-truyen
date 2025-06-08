package com.example.demo.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.demo.AuthActivity;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.utils.SharedPreferencesManager;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationModel {
    private static final String TAG = "AuthenticationModel";
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
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("role")
    private String role;

    @SerializedName("token")
    private String token;

    private final Context context;
    private final ApiService apiService;
    private final SharedPreferencesManager prefsManager;

    public AuthenticationModel(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.prefsManager = SharedPreferencesManager.getInstance(context);
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
        prefsManager.clearUserData();
        
        // Navigate to login screen
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return prefsManager.isLoggedIn();
    }

    public String getToken() {
        return prefsManager.getToken();
    }

    public int getUserId() {
        return prefsManager.getUserId();
    }

    public String getUsername() {
        return prefsManager.getUsername();
    }

    public String getEmail() {
        return prefsManager.getEmail();
    }

    public String getPhone() {
        return prefsManager.getPhone();
    }

    public String getRole() {
        return prefsManager.getRole();
    }

    public String getAvatar() {
        return prefsManager.getAvatar();
    }

    public void saveUserData(LoginResponse response) {
        // Save token and user to SharedPreferencesManager
        prefsManager.saveToken(response.getToken());
        prefsManager.saveUser(response.getUser());
    }

    public void saveUserData(RegisterResponse response) {
        // Save token and user to SharedPreferencesManager
        prefsManager.saveToken(response.getToken());
        prefsManager.saveUser(response.getUser());
    }

    public interface LoginCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onError(String message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void clearLoginData() {
        Log.d(TAG, "Clearing login data");
        prefsManager.clearUserData();
    }
}
