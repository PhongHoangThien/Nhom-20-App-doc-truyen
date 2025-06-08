package com.example.demo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo.R;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView categoriesRecyclerView;
    private RecyclerView newBooksRecyclerView;
    private RecyclerView popularBooksRecyclerView;
    private CategoryAdapter categoryAdapter;
    private BookAdapter newBooksAdapter;
    private BookAdapter popularBooksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerViews
        categoriesRecyclerView = root.findViewById(R.id.categoriesRecyclerView);
        newBooksRecyclerView = root.findViewById(R.id.newBooksRecyclerView);
        popularBooksRecyclerView = root.findViewById(R.id.popularBooksRecyclerView);

        // Set up layouts
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize adapters
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        newBooksAdapter = new BookAdapter(new ArrayList<>());
        popularBooksAdapter = new BookAdapter(new ArrayList<>());

        // Set adapters
        categoriesRecyclerView.setAdapter(categoryAdapter);
        newBooksRecyclerView.setAdapter(newBooksAdapter);
        popularBooksRecyclerView.setAdapter(popularBooksAdapter);

        // Load data
        loadCategories();
        loadNewBooks();
        loadPopularBooks();

        return root;
    }

    private void loadCategories() {
        // TODO: Load categories from API
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Fiction"));
        categories.add(new Category(2L, "Non-Fiction"));
        categories.add(new Category(3L, "Science"));
        categories.add(new Category(4L, "History"));
        categoryAdapter.updateCategories(categories);
    }

    private void loadNewBooks() {
        // TODO: Load new books from API
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "Author 1", "Description 1", "cover1.jpg"));
        books.add(new Book(2L, "Book 2", "Author 2", "Description 2", "cover2.jpg"));
        newBooksAdapter.updateBooks(books);
    }

    private void loadPopularBooks() {
        // TODO: Load popular books from API
        List<Book> books = new ArrayList<>();
        books.add(new Book(3L, "Book 3", "Author 3", "Description 3", "cover3.jpg"));
        books.add(new Book(4L, "Book 4", "Author 4", "Description 4", "cover4.jpg"));
        popularBooksAdapter.updateBooks(books);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize your views and setup here
    }
} 