package com.example.demo.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.demo.R;

public class BookDetailsFragment extends Fragment {
    private TextView titleText;
    private TextView authorText;
    private TextView descriptionText;
    private TextView priceText;
    private TextView ratingText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_details, container, false);

        // Initialize views
        titleText = root.findViewById(R.id.titleText);
        authorText = root.findViewById(R.id.authorText);
        descriptionText = root.findViewById(R.id.descriptionText);
        priceText = root.findViewById(R.id.priceText);
        ratingText = root.findViewById(R.id.ratingText);

        // TODO: Load book details

        return root;
    }
} 