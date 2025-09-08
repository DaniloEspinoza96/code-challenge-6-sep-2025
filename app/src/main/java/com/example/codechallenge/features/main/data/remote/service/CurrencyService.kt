package com.example.codechallenge.features.main.data.remote.service

import com.example.codechallenge.BuildConfig
import com.example.codechallenge.core.network.createClient
import com.example.codechallenge.features.main.data.remote.adapter.CurrencyResponseAdapter
import com.example.codechallenge.features.main.data.remote.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyService {
    @GET("{currency}/periodo/{startDate}/{endDate}?apikey=${BuildConfig.API_KEY}&formato=json")
    suspend fun getCurrencyHistory(
        @Path("currency") currency: String,
        @Path("startDate", encoded = true) startDate: String,
        @Path("endDate", encoded = true) endDate: String
    ): Response<CurrencyResponse>

    companion object {
        fun create(): CurrencyService = createClient(
            baseUrl = BuildConfig.BASE_URL,
            service = CurrencyService::class.java,
            moshiAdapters = listOf(CurrencyResponseAdapter())
        )
    }
}