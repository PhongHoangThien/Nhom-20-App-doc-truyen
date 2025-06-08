package com.example.demo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demo.AuthActivity;
import com.example.demo.R;
import com.example.demo.ChangeEmailActivity;
import com.example.demo.ChangePasswordActivity;
import com.example.demo.PersonalSettingsActivity;
import com.example.demo.utils.SharedPreferencesManager;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {
    private ShapeableImageView profileImage;
    private TextView usernameText;
    private TextView emailText;
    private TextView editProfileButton;
    private TextView changePasswordButton;
    private TextView changeEmailButton;
    private Button logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImage = root.findViewById(R.id.profileImage);
        usernameText = root.findViewById(R.id.usernameText);
        emailText = root.findViewById(R.id.emailText);
        editProfileButton = root.findViewById(R.id.editProfileButton);
        changePasswordButton = root.findViewById(R.id.changePasswordButton);
        changeEmailButton = root.findViewById(R.id.changeEmailButton);
        logoutButton = root.findViewById(R.id.logoutButton);

        // Set up click listeners
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalSettingsActivity.class);
            startActivity(intent);
        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        changeEmailButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeEmailActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferencesManager.getInstance(requireContext()).clearUserData();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Load user data
        loadUserData();

        return root;
    }

    private void loadUserData() {
        SharedPreferencesManager prefs = SharedPreferencesManager.getInstance(requireContext());
        usernameText.setText(prefs.getUsername());
        emailText.setText(prefs.getEmail());
        // TODO: Load profile image using Glide or Picasso
    }
} 