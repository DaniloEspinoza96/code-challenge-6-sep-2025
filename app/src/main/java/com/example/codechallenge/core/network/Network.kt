package com.example.codechallenge.core.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun <T> createClient(
    baseUrl: String,
    service: Class<T>,
    readTimeout: Long = 20,
    connectTimeout: Long = 20,
    moshiAdapters: List<Any> = emptyList()
): T {

    val clientBuilder = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .build()

    val moshi = Moshi.Builder()

    moshiAdapters.forEach { adapter ->
        moshi.add(adapter)
    }

    moshi.add(KotlinJsonAdapterFactory())

    val moshiBuilder = moshi.build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(clientBuilder)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder).asLenient())
        .build()
        .create(service)
}