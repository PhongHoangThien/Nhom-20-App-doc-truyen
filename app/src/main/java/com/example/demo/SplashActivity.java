package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivitySplashBinding;
import com.example.demo.network.RetrofitClient;
import com.example.demo.network.ApiService;
import com.example.demo.network.User;
import com.example.demo.utils.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check login status after delay
        new Handler().postDelayed(this::checkLoginStatus, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            // No token found, go to login screen
            startActivity(new Intent(this, AuthActivity.class));
        } else {
            // Token exists, verify with server
            ApiService apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);
            apiService.getCurrentUser("Bearer " + token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Valid token, go to main screen
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        // Invalid token, go to login screen
                        SharedPreferencesManager.getInstance(SplashActivity.this).clearUserData();
                        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Network error, go to login screen
                    SharedPreferencesManager.getInstance(SplashActivity.this).clearUserData();
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    finish();
                }
            });
        }
    }
} 