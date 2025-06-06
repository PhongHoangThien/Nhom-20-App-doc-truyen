package com.example.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo.databinding.ActivityPersonalSettingsBinding;
import com.example.demo.model.AuthenticationModel;
import com.example.demo.network.ApiService;
import com.example.demo.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalSettingsActivity extends AppCompatActivity {
    private ActivityPersonalSettingsBinding binding;
    private AuthenticationModel authModel;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Personal Settings");

        // Initialize AuthenticationModel and ApiService
        authModel = new AuthenticationModel(this);
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);

        // Load current user data
        loadUserData();

        // Set up click listeners
        setupClickListeners();
    }

    private void loadUserData() {
        // Display current user data
        binding.editTextUsername.setText(authModel.getUsername());
        binding.editTextEmail.setText(authModel.getEmail());
        binding.editTextPhone.setText(authModel.getPhone());
    }

    private void setupClickListeners() {
        binding.buttonSave.setOnClickListener(v -> updateUserProfile());
    }

    private void updateUserProfile() {
        String username = binding.editTextUsername.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();

        // TODO: Implement update profile with Spring Boot API
        Toast.makeText(this, "Profile update will be implemented with Spring Boot API", Toast.LENGTH_SHORT).show();
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