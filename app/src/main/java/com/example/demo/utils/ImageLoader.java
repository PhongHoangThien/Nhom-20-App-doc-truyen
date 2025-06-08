package com.example.demo.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.demo.R;
import com.example.demo.api.RetrofitClient;

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    public static void loadBookCover(Context context, String imagePath, ImageView imageView) {
        if (imagePath == null || imagePath.isEmpty()) {
            // Load default image if no image path is provided
            imageView.setImageResource(R.drawable.default_book_cover);
            return;
        }

        // Configure Glide options
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.default_book_cover)
                .error(R.drawable.default_book_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        // Check if the image path is a drawable resource name
        if (!imagePath.startsWith("http") && !imagePath.startsWith("/")) {
            try {
                // Try to load from drawable resources
                int resourceId = context.getResources().getIdentifier(
                    imagePath.replace(".png", "").replace(".jpg", "").replace(".jpeg", ""),
                    "drawable",
                    context.getPackageName()
                );
                if (resourceId != 0) {
                    Log.d(TAG, "Loading image from drawable: " + imagePath);
                    Glide.with(context)
                            .load(resourceId)
                            .apply(requestOptions)
                            .into(imageView);
                    return;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading drawable resource: " + e.getMessage());
            }
        }

        // If not a drawable or drawable loading failed, try loading from URL
        String fullImagePath;
        if (imagePath.startsWith("http")) {
            // If it's already a full URL, use it as is
            fullImagePath = imagePath;
        } else if (imagePath.startsWith("/")) {
            // If it's a relative path starting with '/', prepend the base URL
            fullImagePath = RetrofitClient.getBaseUrl().replace("/api/", "") + imagePath;
        } else {
            // If it's a relative path without '/', add it
            fullImagePath = RetrofitClient.getBaseUrl().replace("/api/", "") + "/" + imagePath;
        }

        Log.d(TAG, "Loading image from URL: " + fullImagePath);

        // Load the image using Glide
        Glide.with(context)
                .load(fullImagePath)
                .apply(requestOptions)
                .into(imageView);
    }
} 