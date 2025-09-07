package com.example.codechallenge.features.user.di

import android.content.Context
import com.example.codechallenge.features.user.data.local.db.AppDatabase
import com.example.codechallenge.features.user.data.repository.UserRepositoryImpl
import com.example.codechallenge.features.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        db: AppDatabase
    ): UserRepository {
        return UserRepositoryImpl(db.userDao())
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}