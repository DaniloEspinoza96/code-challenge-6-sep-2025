package com.example.codechallenge.features.user.data.local.datastore

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.codechallenge.features.user.domain.model.UserInfo
import com.example.codechallenge.utis.TinkManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.map


val Context.secureDataStore by preferencesDataStore("user_preferences")

class UserPreferences(
    private val context: Context,
    private val tinkManager: TinkManager
) {
    private val USER_KEY = stringPreferencesKey("user")
    private val gson = Gson()

    suspend fun saveUser(user: UserInfo) {
        val serializedUser = gson.toJson(user)

        val encrypted = tinkManager.encrypt(serializedUser)
        val encoded = Base64.encodeToString(encrypted, Base64.DEFAULT)
        context.secureDataStore.edit { prefs ->
            prefs[USER_KEY] = encoded
        }
    }

    suspend fun clearUser() {
        context.secureDataStore.edit { prefs ->
            prefs.remove(USER_KEY)
        }
    }

    val userFlow = context.secureDataStore.data.map { prefs ->
        prefs[USER_KEY]?.let {
            val decoded = Base64.decode(it, Base64.DEFAULT)
            val decrypted = tinkManager.decrypt(decoded)

            val user = gson.fromJson(decrypted, UserInfo::class.java)
            user
        }
    }
}