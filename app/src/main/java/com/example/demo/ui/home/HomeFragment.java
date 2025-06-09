package com.example.demo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo.R;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.ui.book.BookAdapter;
import com.example.demo.ui.detail.BookDetailActivity;
import com.example.demo.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener, BookAdapter.OnBookClickListener {
    private static final String TAG = "HomeFragment";
    private static final int PAGE_SIZE = 10;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView allBooksRecyclerView;
    private RecyclerView popularBooksRecyclerView;
    private CategoryAdapter categoryAdapter;
    private BookAdapter allBooksAdapter;
    private BookAdapter popularBooksAdapter;
    private ApiService apiService;
    private SharedPreferencesManager preferenceManager;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMorePages = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = RetrofitClient.getInstance().getApiService();
        preferenceManager = SharedPreferencesManager.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize RecyclerViews
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        allBooksRecyclerView = view.findViewById(R.id.allBooksRecyclerView);
        popularBooksRecyclerView = view.findViewById(R.id.popularBooksRecyclerView);
        
        // Setup RecyclerViews
        setupRecyclerViews();
        
        // Load data
        loadCategories();
        loadAllBooks();
        loadPopularBooks();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupRecyclerViews() {
        // Setup categories
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryAdapter.setOnCategoryClickListener(this);
        categoriesRecyclerView.setAdapter(categoryAdapter);
        
        // Setup all books
        allBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allBooksAdapter = new BookAdapter(new ArrayList<>());
        allBooksAdapter.setOnBookClickListener(this);
        allBooksRecyclerView.setAdapter(allBooksAdapter);
        
        // Setup popular books
        popularBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularBooksAdapter = new BookAdapter(new ArrayList<>());
        popularBooksAdapter.setOnBookClickListener(this);
        popularBooksRecyclerView.setAdapter(popularBooksAdapter);
    }

    private void loadCategories() {
        Log.d(TAG, "Loading categories");
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    Log.d(TAG, "Loaded " + categories.size() + " categories");
                    categoryAdapter.updateCategories(categories);
                } else {
                    Log.e(TAG, "Failed to load categories: " + response.message());
                    Toast.makeText(getContext(), "Failed to load categories. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e(TAG, "Error loading categories", t);
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllBooks() {
        Log.d(TAG, "Loading all books");
        apiService.getBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    Log.d(TAG, "Loaded " + books.size() + " books");
                    allBooksAdapter.updateBooks(books);
                } else {
                    Log.e(TAG, "Failed to load books: " + response.message());
                    Toast.makeText(getContext(), "Failed to load books. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG, "Error loading books", t);
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPopularBooks() {
        if (isLoading || !hasMorePages) {
            return;
        }

        isLoading = true;
        Log.d(TAG, "Loading popular books page " + currentPage);
        
        apiService.getPopularBooks(currentPage, PAGE_SIZE).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                isLoading = false;
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    Log.d(TAG, "Loaded " + books.size() + " popular books for page " + currentPage);
                    
                    if (books.isEmpty()) {
                        hasMorePages = false;
                        return;
                    }
                    
                    // Update adapter with new books
                    List<Book> currentBooks = new ArrayList<>(popularBooksAdapter.getBooks());
                    currentBooks.addAll(books);
                    popularBooksAdapter.updateBooks(currentBooks);
                    
                    // Increment page for next load
                    currentPage++;
                } else {
                    Log.e(TAG, "Failed to load popular books: " + response.message());
                    Toast.makeText(getContext(), "Failed to load popular books. Please try again later.", Toast.LENGTH_SHORT).show();
                    hasMorePages = false;
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                isLoading = false;
                Log.e(TAG, "Error loading popular books", t);
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                hasMorePages = false;
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        // Handle category click
        Toast.makeText(getContext(), "Selected category: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookClick(Book book) {
        // Handle book click
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("book_id", book.getId());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize your views and setup here
    }
} 