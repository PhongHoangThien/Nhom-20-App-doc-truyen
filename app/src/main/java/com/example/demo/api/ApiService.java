package com.example.demo.api;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.model.Book;
import com.example.demo.model.Chapter;
import com.example.demo.model.ReadingProgress;
import com.example.demo.model.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Header;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.RegisterResponse;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("users/profile")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") Long id);

    @GET("books")
    Call<List<Book>> getBooks();

    @GET("books/{id}")
    Call<Book> getBook(@Path("id") Long id);

    @GET("chapters/{id}")
    Call<Chapter> getChapter(@Path("id") Long id);

    @POST("reading-progress/{userId}/{bookId}/{chapterId}")
    Call<Void> saveReadingProgress(
        @Path("userId") Long userId,
        @Path("bookId") Long bookId,
        @Path("chapterId") Long chapterId
    );

    @GET("reading-progress/{userId}/{bookId}")
    Call<ReadingProgress> getReadingProgress(
        @Path("userId") Long userId,
        @Path("bookId") Long bookId
    );

    @GET("reading-progress/user/{userId}")
    Call<List<ReadingProgress>> getUserProgress(@Path("userId") Long userId);

    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("query") String query);

    @GET("books/{bookId}/chapters")
    Call<List<Chapter>> getChapters(@Path("bookId") String bookId);

    @GET("books/{bookId}/chapters/{chapterId}")
    Call<Chapter> getChapter(@Path("bookId") String bookId, @Path("chapterId") String chapterId);

    @GET("books/completed")
    Call<List<Book>> getCompletedBooks(@Header("Authorization") String token);

    @GET("books/popular")
    Call<List<Book>> getPopularBooks(@Query("page") int page, @Query("size") int size);

    @GET("books/category/{categoryId}")
    Call<List<Book>> getBooksByCategory(@Path("categoryId") Long categoryId);

    @GET("categories")
    Call<List<Category>> getCategories();
} 