package com.example.fitranker.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.tokenDataStore by preferencesDataStore(name = "auth_token")

object TokenKeys {
    val ID_TOKEN = stringPreferencesKey("id_token")
}

class TokenStorage @Inject constructor( @ApplicationContext private val context: Context) {

    fun getToken(): Flow<String?> =
        context.tokenDataStore.data.map { prefs ->
            prefs[TokenKeys.ID_TOKEN]
        }

    suspend fun saveToken(token: String?) {
        context.tokenDataStore.edit { prefs ->
            if (token == null) {
                prefs.remove(TokenKeys.ID_TOKEN)
            } else {
                prefs[TokenKeys.ID_TOKEN] = token
            }
        }
    }

    suspend fun clearToken() {
        saveToken(null)
    }
}