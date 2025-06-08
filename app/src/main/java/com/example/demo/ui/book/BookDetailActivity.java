package com.example.demo.ui.book;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.demo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BookDetailActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fabRead = findViewById(R.id.fabRead);

        // Set up ViewPager with adapter
        BookDetailPagerAdapter pagerAdapter = new BookDetailPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Details");
                            break;
                        case 1:
                            tab.setText("Chapters");
                            break;
                        case 2:
                            tab.setText("Reviews");
                            break;
                    }
                }).attach();

        // Set up FAB click listener
        fabRead.setOnClickListener(v -> {
            // TODO: Start ReaderActivity
        });

        // TODO: Load book details
    }
} 