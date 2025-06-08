package com.example.demo.ui.library;

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
import com.example.demo.ui.detail.BookDetailActivity;
import com.example.demo.ui.home.BookAdapter;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CompletedFragment extends Fragment implements BookAdapter.OnBookClickListener {
    private static final String TAG = "CompletedFragment";

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ApiService apiService;
    private SharedPreferencesManager preferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = RetrofitClient.getInstance().getApiService();
        preferenceManager = new SharedPreferencesManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        adapter = new BookAdapter(new ArrayList<>());
        adapter.setOnBookClickListener(this);
        recyclerView.setAdapter(adapter);

        // Load completed books
        loadCompletedBooks();

        return view;
    }

    private void loadCompletedBooks() {
        Log.d(TAG, "Loading completed books");
        // TODO: Implement loading completed books from API
        // For now, show a message
        Toast.makeText(getContext(), "Completed books feature coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookClick(Book book) {
        Log.d(TAG, "Book clicked: " + book.getTitle());
        startActivity(BookDetailActivity.newIntent(getContext(), book.getId()));
    }
} 