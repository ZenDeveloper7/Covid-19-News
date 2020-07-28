package com.zen.covid_19news.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_1 = "https://covid-19-data.p.rapidapi.com/"
    private const val BASE_URL_2 = "https://api.covid19api.com/"

    private val retrofitOne by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                    .addHeader(
                        "x-rapidapi-key",
                        "9f0891d519msh20c86bc5798ca36p1a5aadjsn5f54babe3562"
                    )
                    .method(original.method, original.body)
                it.proceed(requestBuilder.build())
            }.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    private val retrofitTwo by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
                it.proceed(requestBuilder.build())
            }.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: Api by lazy {
        retrofitOne.create(Api::class.java)
    }

    val api2: Api by lazy {
        retrofitTwo.create(Api::class.java)
    }
}