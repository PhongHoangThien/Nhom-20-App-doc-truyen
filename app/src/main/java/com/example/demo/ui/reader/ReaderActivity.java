package com.example.demo.ui.reader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import com.example.demo.R;
import com.example.demo.model.ReadingProgress;
import com.example.demo.model.ReadingSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.demo.model.Chapter;
import com.example.demo.api.RetrofitClient;
import com.example.demo.api.ApiService;

public class ReaderActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "ReaderPrefs";
    private static final String KEY_READING_SETTINGS = "reading_settings";
    private static final String KEY_READING_PROGRESS = "reading_progress_";

    private Toolbar toolbar;
    private TextView chapterTitleText;
    private TextView textViewContent;
    private BottomNavigationView bottomNavigation;
    private NestedScrollView scrollView;

    private ReadingSettings readingSettings;
    private ReadingProgress readingProgress;
    private Long bookId;
    private Long chapterId;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        chapterTitleText = findViewById(R.id.chapterTitleText);
        textViewContent = findViewById(R.id.textViewContent);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        scrollView = findViewById(R.id.scrollView);

        // Set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get book and chapter IDs from intent
        bookId = getIntent().getLongExtra("bookId", -1);
        chapterId = getIntent().getLongExtra("chapterId", -1);
        userId = getIntent().getLongExtra("userId", -1);

        if (bookId == -1 || chapterId == -1 || userId == -1) {
            Toast.makeText(this, "Invalid book or chapter ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadReadingProgress();
        loadChapter();

        // Load reading settings
        loadReadingSettings();

        // Apply reading settings
        applyReadingSettings();

        // Set up bottom navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_previous) {
                loadPreviousChapter();
                return true;
            } else if (itemId == R.id.navigation_font) {
                showFontSettingsDialog();
                return true;
            } else if (itemId == R.id.navigation_theme) {
                toggleTheme();
                return true;
            } else if (itemId == R.id.navigation_next) {
                loadNextChapter();
                return true;
            }
            return false;
        });

        // Set up scroll listener to save progress
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            saveReadingProgress(scrollY);
        });
    }

    private void loadReadingSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = prefs.getString(KEY_READING_SETTINGS, null);
        if (settingsJson != null) {
            readingSettings = new Gson().fromJson(settingsJson, ReadingSettings.class);
        } else {
            readingSettings = new ReadingSettings();
        }
    }

    private void saveReadingSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = new Gson().toJson(readingSettings);
        prefs.edit().putString(KEY_READING_SETTINGS, settingsJson).apply();
    }

    private void loadReadingProgress() {
        String progressJson = getSharedPreferences("reading_progress", MODE_PRIVATE)
                .getString(bookId.toString(), null);
        if (progressJson != null) {
            readingProgress = new Gson().fromJson(progressJson, ReadingProgress.class);
            if (readingProgress.getChapterOrder() == chapterId.intValue()) {
                scrollView.post(() -> scrollView.scrollTo(0, readingProgress.getScrollPosition()));
            }
        } else {
            readingProgress = new ReadingProgress(bookId.intValue(), chapterId.intValue(), 0);
        }
    }

    private void saveReadingProgress(int scrollPosition) {
        readingProgress.setChapterOrder(chapterId.intValue());
        readingProgress.setScrollPosition(scrollPosition);
        readingProgress.setLastReadTimestamp(System.currentTimeMillis());
        
        String progressJson = new Gson().toJson(readingProgress);
        getSharedPreferences("reading_progress", MODE_PRIVATE)
                .edit()
                .putString(bookId.toString(), progressJson)
                .apply();
    }

    private void applyReadingSettings() {
        // Apply font settings
        textViewContent.setTextSize(readingSettings.getFontSize());
        textViewContent.setTypeface(android.graphics.Typeface.create(readingSettings.getFontFamily(), android.graphics.Typeface.NORMAL));
        textViewContent.setLineSpacing(0, readingSettings.getLineSpacing());

        // Apply theme
        if (readingSettings.isDarkMode()) {
            textViewContent.setTextColor(getResources().getColor(android.R.color.white));
            textViewContent.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            textViewContent.setTextColor(getResources().getColor(android.R.color.black));
            textViewContent.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        // Apply margins
        textViewContent.setPadding(
            readingSettings.getMarginHorizontal(),
            readingSettings.getMarginVertical(),
            readingSettings.getMarginHorizontal(),
            readingSettings.getMarginVertical()
        );
    }

    private void loadPreviousChapter() {
        if (chapterId > 0) {
            chapterId--;
            loadChapter();
        }
    }

    private void loadNextChapter() {
        // TODO: Check if there are more chapters
        chapterId++;
        loadChapter();
    }

    private void showFontSettingsDialog() {
        // TODO: Show font settings dialog
    }

    private void toggleTheme() {
        readingSettings.setDarkMode(!readingSettings.isDarkMode());
        applyReadingSettings();
        saveReadingSettings();
    }

    private void loadChapter() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getChapter(chapterId)
                .enqueue(new Callback<Chapter>() {
                    @Override
                    public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Chapter chapter = response.body();
                            chapterTitleText.setText(chapter.getTitle());
                            textViewContent.setText(chapter.getContent());
                            
                            // Create reading progress with integer IDs
                            readingProgress = new ReadingProgress(
                                bookId.intValue(), 
                                chapterId.intValue(), 
                                0
                            );
                            
                            // Update chapter index with integer ID
                            readingProgress.setChapterOrder(chapterId.intValue());
                        } else {
                            Toast.makeText(ReaderActivity.this, "Failed to load chapter", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Chapter> call, Throwable t) {
                        Toast.makeText(ReaderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save progress when leaving the activity
        saveReadingProgress(scrollView.getScrollY());
    }
} 