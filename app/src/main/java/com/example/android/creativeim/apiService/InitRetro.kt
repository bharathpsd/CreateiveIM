package com.example.android.creativeim.apiService

import com.example.android.creativeim.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class InitRetro {

    private val loggingInterceptor by lazy { HttpLoggingInterceptor() }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
            .build()

    val api = retrofit.create(NotificationMessage::class.java)


}

