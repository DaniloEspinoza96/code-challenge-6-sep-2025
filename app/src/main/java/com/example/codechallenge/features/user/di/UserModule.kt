package com.example.codechallenge.features.user.di

import android.content.Context
import com.example.codechallenge.features.user.data.local.datastore.UserPreferences
import com.example.codechallenge.features.user.data.local.db.AppDatabase
import com.example.codechallenge.features.user.data.repository.UserRepositoryImpl
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.utis.TinkManager
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserModule {
    private val KEYSET_NAME = "master_keyset"
    private val PREF_FILE = "master_key_preference"
    private val MASTER_KEY_URI = "android-keystore://master_key"

    @Singleton
    @Provides
    fun provideAead(@ApplicationContext context: Context): Aead {
        AeadConfig.register()

        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE)
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(Aead::class.java)
    }

    @Singleton
    @Provides
    fun provideUserPreference(
        @ApplicationContext context: Context,
        tinkManager: TinkManager
    ): UserPreferences {
        return UserPreferences(
            context,
            tinkManager
        )
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        db: AppDatabase,
        userPreferences: UserPreferences
    ): UserRepository {
        return UserRepositoryImpl(db.userDao(), userPreferences)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}