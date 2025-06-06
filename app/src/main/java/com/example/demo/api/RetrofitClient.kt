package com.example.demo.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Thay đổi BASE_URL tùy theo môi trường
    private const val BASE_URL = "http://10.0.2.2:8080/" // Cho Android Emulator
    // private const val BASE_URL = "http://192.168.1.xxx:8080/" // Cho thiết bị thật, thay xxx bằng IP của máy tính

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method, original.body)
            
            // Thêm token nếu có
            val token = getAuthToken() // Implement phương thức này để lấy token từ SharedPreferences
            if (!token.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
            
            chain.proceed(requestBuilder.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    // Phương thức để lấy token từ SharedPreferences
    private fun getAuthToken(): String? {
        // TODO: Implement phương thức này để lấy token từ SharedPreferences
        return null
    }
} 