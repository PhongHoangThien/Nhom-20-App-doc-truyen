package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.model.User;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY = 2000; // 2 seconds
    private boolean isNavigating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for splash screen
        new Handler().postDelayed(this::checkLoginStatus, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        if (isFinishing()) return;
        
        SharedPreferencesManager prefsManager = SharedPreferencesManager.getInstance(this);
        if (prefsManager.isLoggedIn()) {
            Log.d(TAG, "User is logged in, going to main screen");
            navigateToMain();
        } else {
            Log.d(TAG, "User not logged in, going to login screen");
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        if (isFinishing()) return;
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        if (isFinishing()) return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 