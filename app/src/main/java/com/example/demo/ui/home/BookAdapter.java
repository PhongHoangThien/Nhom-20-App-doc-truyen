package com.example.demo.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo.R;
import com.example.demo.model.Book;
import com.example.demo.utils.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private static final String TAG = "BookAdapter";
    private List<Book> books;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(List<Book> books) {
        this.books = books != null ? books : new ArrayList<>();
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if (books != null && position < books.size()) {
            Book book = books.get(position);
            holder.bind(book);
        }
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public void updateBooks(List<Book> newBooks) {
        Log.d(TAG, "Updating books list with " + (newBooks != null ? newBooks.size() : 0) + " items");
        this.books = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Book> getBooks() {
        return books;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView bookCover;
        private final TextView bookTitle;
        private final TextView bookAuthor;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && books != null && position < books.size()) {
                    Book book = books.get(position);
                    if (listener != null) {
                        listener.onBookClick(book);
                    }
                }
            });
        }

        void bind(Book book) {
            Log.d(TAG, "Binding book: " + book.toString());
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            ImageLoader.loadBookCover(itemView.getContext(), book.getCoverImage(), bookCover);
        }
    }
} 