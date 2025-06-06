package com.example.demo.api

import com.example.demo.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Authentication endpoints
    @POST("api/auth/register")
    suspend fun register(@Body user: User): Response<User>

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body email: String): Response<Unit>

    // User endpoints
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: User): Response<User>

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Unit>

    // Profile endpoints
    @GET("api/users/profile")
    suspend fun getProfile(): Response<User>

    @PUT("api/users/profile")
    suspend fun updateProfile(@Body user: User): Response<User>

    @PUT("api/users/change-password")
    suspend fun changePassword(@Body passwordRequest: ChangePasswordRequest): Response<Unit>
}

// Data classes for requests and responses
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
) 