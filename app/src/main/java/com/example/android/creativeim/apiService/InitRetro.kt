package com.example.android.creativeim.apiService

import com.example.android.creativeim.constants.Constants
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class InitRetro {

    private val loggingInterceptor by lazy { HttpLoggingInterceptor() }

    private val okHttpClient by lazy {
        okhttp3.OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api by lazy {
        retrofit.create(NotificationMessage::class.java)
    }


}