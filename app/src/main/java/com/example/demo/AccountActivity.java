package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {
    private ActivityAccountBinding binding;
    private TextView tvUsername;
    private Button btnPersonalSettings;
    private Button btnChangePassword;
    private Button btnChangeEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views
        tvUsername = binding.tvUsername;
        btnPersonalSettings = binding.btnPersonalSettings;
        btnChangePassword = binding.btnChangePassword;
        btnChangeEmail = binding.btnChangeEmail;
        btnLogout = binding.btnLogout;

        // Set click listeners
        btnPersonalSettings.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, PersonalSettingsActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnChangeEmail.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ChangeEmailActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // TODO: Implement logout logic with Spring Boot
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // TODO: Load user data from Spring Boot API
        loadUserData();
    }

    private void loadUserData() {
        // TODO: Implement loading user data from Spring Boot API
        // For now, just set a placeholder
        tvUsername.setText("User");
    }
}