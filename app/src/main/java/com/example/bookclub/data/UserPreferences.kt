package com.example.bookclub.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

data class UserData(val isLoggedIn: Boolean, val username: String)

class UserPreferences(private val context: Context) {
    companion object{
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USERNAME = stringPreferencesKey("username")
    }

    val userFlow: Flow<UserData> = context.dataStore.data.map { preferences ->
        UserData(
            isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
            username = preferences[USERNAME] ?: ""
        )
    }

    suspend fun saveUser(username: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USERNAME] = username
        }
    }

    suspend fun logoutUser() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USERNAME] = ""
        }
    }
}