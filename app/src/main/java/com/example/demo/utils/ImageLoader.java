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
                int resourceId = context.getResources().getIdentifier(
                    imagePath.replace(".png", "").replace(".jpg", "").replace(".jpeg", ""),
                    "drawable",
                    context.getPackageName()
                );
                if (resourceId != 0) {
                    Log.d(TAG, "Loading image from drawable: " + imagePath);
                    Glide.with(context).clear(imageView);
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
            fullImagePath = imagePath;
        } else if (imagePath.startsWith("/")) {
            fullImagePath = RetrofitClient.getBaseUrl().replace("/api/", "") + imagePath;
        } else {
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