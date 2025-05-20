package org.ligi.survivalmanual.refactor.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesDataSource(private val context: Context) {

    private object PreferencesKeys {
        val NIGHT_MODE = booleanPreferencesKey("night_mode")
    }

    suspend fun getUserPreferences(): UserPreferences {
        return try {
            context.dataStore.data.collect { preferences ->
                UserPreferences(
                    isNightModeEnabled = preferences[PreferencesKeys.NIGHT_MODE] ?: false
                )
            }
            // This part won't be reached as collect is a suspending function that doesn't return
            // You might need a different pattern if you just want the current value, e.g., first()
        } catch (e: Exception) {
            throw DomainException.UnknownErrorException("Failed to get user preferences: ${e.message}")
        }
    }

    suspend fun saveUserPreferences(preferences: UserPreferences) {
        try {
            context.dataStore.edit { mutablePreferences ->
                mutablePreferences[PreferencesKeys.NIGHT_MODE] = preferences.isNightModeEnabled
            }
        } catch (e: Exception) {
            throw DomainException.PreferencesSaveException("Failed to save user preferences: ${e.message}")
        }
    }
}