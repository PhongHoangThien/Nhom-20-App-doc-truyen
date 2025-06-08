package com.example.demo.network;

import com.example.demo.model.User;
import com.example.demo.model.Chapter;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/auth/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @GET("api/users/profile")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @PUT("api/users/change-password")
    Call<Void> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest request);

    @PUT("api/users/change-email")
    Call<Void> changeEmail(@Header("Authorization") String token, @Body ChangeEmailRequest request);

    @PUT("api/users/profile")
    Call<User> updateProfile(@Header("Authorization") String token, @Body UpdateProfileRequest request);

    @POST("api/auth/forgot-password")
    Call<Void> forgotPassword(@Body String email);

    @GET("chapters/{id}")
    Call<Chapter> getChapter(@Path("id") Long chapterId);
} 