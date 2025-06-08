package com.example.demo.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements BookAdapter.OnBookClickListener {
    private TextInputEditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> books = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);

        // Set up RecyclerView
        bookAdapter = new BookAdapter(books, this);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsRecyclerView.setAdapter(bookAdapter);

        // Set up search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    searchBooks(s.toString());
                }
            }
        });

        loadBooks();
        
        return view;
    }

    @Override
    public void onBookClick(Book book) {
        // TODO: Navigate to book details
        Toast.makeText(getContext(), "Selected: " + book.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void loadBooks() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getBooks()
                .enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            books.clear();
                            books.addAll(response.body());
                            bookAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchBooks(String query) {
        RetrofitClient.getInstance()
                .getApiService()
                .searchBooks(query)
                .enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            books.clear();
                            books.addAll(response.body());
                            bookAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}