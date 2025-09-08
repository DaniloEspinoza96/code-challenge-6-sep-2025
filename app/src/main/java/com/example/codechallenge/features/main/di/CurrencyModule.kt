package com.example.codechallenge.features.main.di

import com.example.codechallenge.features.main.data.remote.service.CurrencyService
import com.example.codechallenge.features.main.data.repository.CurrencyRepositoryImpl
import com.example.codechallenge.features.main.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CurrencyModule {
    @Singleton
    @Provides
    fun provideCurrencyService(): CurrencyService {
        return CurrencyService.create()
    }

    @Singleton
    @Provides
    fun provideCurrencyRepository(currencyService: CurrencyService): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyService)
    }
}