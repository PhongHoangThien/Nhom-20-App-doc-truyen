package com.example.demo.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.R;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.model.Book;
import com.example.demo.utils.ImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailActivity";
    private static final String EXTRA_BOOK_ID = "extra_book_id";

    private ImageView bookCoverImage;
    private TextView bookTitleText;
    private TextView bookAuthorText;
    private TextView bookDescriptionText;
    private TextView bookPriceText;
    private TextView bookCategoryText;
    private TextView bookRatingText;
    private FloatingActionButton fabRead;
    private ApiService apiService;

    public static Intent newIntent(Context context, Long bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Initialize views
        bookCoverImage = findViewById(R.id.bookCoverImage);
        bookTitleText = findViewById(R.id.bookTitleText);
        bookAuthorText = findViewById(R.id.bookAuthorText);
        bookDescriptionText = findViewById(R.id.bookDescriptionText);
        bookPriceText = findViewById(R.id.bookPriceText);
        bookCategoryText = findViewById(R.id.bookCategoryText);
        bookRatingText = findViewById(R.id.bookRatingText);
        fabRead = findViewById(R.id.fabRead);

        // Initialize API service
        apiService = RetrofitClient.getInstance().getApiService();

        // Get book ID from intent
        Long bookId = getIntent().getLongExtra(EXTRA_BOOK_ID, -1);
        if (bookId != -1) {
            loadBookDetails(bookId);
        } else {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadBookDetails(Long bookId) {
        Log.d(TAG, "Loading book details for ID: " + bookId);
        apiService.getBook(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    displayBookDetails(book);
                } else {
                    Log.e(TAG, "Failed to load book details: " + response.message());
                    Toast.makeText(BookDetailActivity.this, "Failed to load book details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e(TAG, "Error loading book details", t);
                Toast.makeText(BookDetailActivity.this, "Error loading book details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBookDetails(Book book) {
        Log.d(TAG, "Displaying book details: " + book.getTitle());
        bookTitleText.setText(book.getTitle());
        bookAuthorText.setText(book.getAuthor());
        bookDescriptionText.setText(book.getDescription());
        bookPriceText.setText(String.format("$%.2f", book.getPrice()));
        if (book.getCategory() != null) {
            bookCategoryText.setText(book.getCategory().getName());
        }
        bookRatingText.setText(String.format("%.1f", book.getRating()));

        // Load book cover image
        ImageLoader.loadBookCover(this, book.getCoverImage(), bookCoverImage);

        // Set up read button
        fabRead.setOnClickListener(v -> {
            // TODO: Implement read functionality
            Toast.makeText(this, "Read functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }
} 