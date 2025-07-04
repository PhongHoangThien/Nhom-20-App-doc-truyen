package com.example.demo.ui.book;

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
import com.example.demo.ui.reader.ReaderActivity;
import com.example.demo.utils.ImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailActivity";
    public static final String EXTRA_BOOK_ID = "book_id";
    public static final String EXTRA_BOOK_TITLE = "extra_book_title";
    public static final String EXTRA_BOOK_AUTHOR = "extra_book_author";
    public static final String EXTRA_BOOK_COVER = "extra_book_cover";

    private ImageView bookCoverImage;
    private TextView bookTitleText;
    private TextView bookAuthorText;
    private TextView bookDescriptionText;
    private TextView bookPriceText;
    private TextView bookCategoryText;
    private TextView bookRatingText;
    private FloatingActionButton fabRead;
    private ApiService apiService;

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

        // Set up FAB click listener
        fabRead.setOnClickListener(v -> {
            // TODO: Start ReaderActivity with book ID
            Intent intent = new Intent(this, ReaderActivity.class);
            intent.putExtra("bookId", bookId);
            startActivity(intent);
        });
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
                    Toast.makeText(BookDetailActivity.this, 
                        "Failed to load book details: " + response.message(), 
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e(TAG, "Error loading book details", t);
                Toast.makeText(BookDetailActivity.this, 
                    "Error loading book details: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBookDetails(Book book) {
        Log.d(TAG, "Displaying book details: " + book.toString());
        
        // Load book cover image
        ImageLoader.loadBookCover(this, book.getCoverImage(), bookCoverImage);
        
        // Set text fields
        bookTitleText.setText(book.getTitle());
        bookAuthorText.setText(book.getAuthor());
        bookDescriptionText.setText(book.getDescription());
        bookPriceText.setText(String.format("$%.2f", book.getPrice()));
        bookCategoryText.setText(book.getCategory() != null ? book.getCategory().getName() : "Unknown");
        bookRatingText.setText(String.format("%.1f", book.getRating()));
    }
} 