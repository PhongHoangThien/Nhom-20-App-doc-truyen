package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.model.User;
import com.example.demo.network.ApiService;
import com.example.demo.network.RetrofitClient;
import com.example.demo.ui.home.HomeFragment;
import com.example.demo.ui.library.LibraryFragment;
import com.example.demo.ui.search.SearchFragment;
import com.example.demo.ui.profile.ProfileFragment;
import com.example.demo.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private ApiService apiService;
    private User currentUser;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;
    private boolean isNavigatingToLogin = false;
    private SharedPreferencesManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        preferenceManager = SharedPreferencesManager.getInstance(this);
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);
        
        // Check login status
        if (!preferenceManager.isLoggedIn()) {
            Log.d(TAG, "User not logged in, redirecting to login screen");
            navigateToLogin();
            return;
        }
        
        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        
        // Set up bottom navigation and ViewPager2
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Don't check login status here to prevent loops
    }

    private void navigateToLogin() {
        if (isFinishing()) return;
        Log.d(TAG, "Navigating to login screen");
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                binding.viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                binding.viewPager.setCurrentItem(1);
                return true;
            }
            return false;
        });

        // Set up ViewPager2
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ProfileFragment());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, fragments);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setUserInputEnabled(false); // Disable swipe

        // Sync ViewPager2 with BottomNavigationView
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.bottomNavigation.setSelectedItemId(
                    position == 0 ? R.id.navigation_home : R.id.navigation_profile
                );
            }
        });
    }

    private void loadHomeFragment() {
        binding.viewPager.setCurrentItem(0);
    }

    private void loadProfileFragment() {
        binding.viewPager.setCurrentItem(1);
    }

    private void loadUserData() {
        String token = preferenceManager.getToken();
        if (token != null && !token.isEmpty()) {
            apiService.getCurrentUser("Bearer " + token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUser = response.body();
                        updateUI();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUI() {
        if (currentUser != null) {
            binding.textViewUserInfo.setText(String.format("Welcome, %s!", currentUser.getFullName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_account) {
            startActivity(new Intent(this, AccountActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        String token = preferenceManager.getToken();
        if (token != null && !token.isEmpty()) {
            apiService.logout("Bearer " + token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // Clear local data and go to login screen regardless of server response
                    preferenceManager.clearUserData();
                    navigateToLogin();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Clear local data and go to login screen even if network fails
                    preferenceManager.clearUserData();
                    navigateToLogin();
                }
            });
        } else {
            navigateToLogin();
        }
    }
}
