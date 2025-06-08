package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityAccountDetailBinding;
import com.example.demo.network.ApiService;
import com.example.demo.network.RetrofitClient;
import com.example.demo.model.User;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDetailActivity extends AppCompatActivity {
    private ActivityAccountDetailBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Account Details");
        }

        // Initialize API service
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);

        // Set up click listeners
        setupClickListeners();

        // Load user data
        loadUserData();
    }

    private void setupClickListeners() {
        binding.buttonChangeEmail.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeEmailActivity.class);
            startActivity(intent);
        });

        binding.buttonChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        binding.buttonLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        apiService.getCurrentUser("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                binding.progressBar.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    updateUI(user);
                } else {
                    Toast.makeText(AccountDetailActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                binding.progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(AccountDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        binding.textViewEmail.setText(user.getEmail());
        binding.textViewFullName.setText(user.getFullName() != null ? user.getFullName() : "Not set");
        binding.textViewPhone.setText(user.getPhone() != null ? user.getPhone() : "Not set");
        binding.textViewAddress.setText(user.getAddress() != null ? user.getAddress() : "Not set");
        binding.textViewRole.setText(user.getRole());
    }

    private void logout() {
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            clearUserDataAndFinish();
            return;
        }

        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        apiService.logout("Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(android.view.View.GONE);
                clearUserDataAndFinish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(android.view.View.GONE);
                clearUserDataAndFinish();
            }
        });
    }

    private void clearUserDataAndFinish() {
        SharedPreferencesManager.getInstance(this).clearUserData();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 