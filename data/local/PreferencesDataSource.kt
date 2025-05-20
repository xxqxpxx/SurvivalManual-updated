package data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import domain.model.UserPreferences
import domain.error.PreferencesSaveException
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesDataSource(private val context: Context) {

    private object PreferencesKeys {
        val NIGHT_MODE = booleanPreferencesKey("night_mode")
    }

    suspend fun getUserPreferences(): UserPreferences {
        return try {
            val preferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
            preferences.let {
                UserPreferences(
                    isNightModeEnabled = it.getBoolean(PreferencesKeys.NIGHT_MODE.name, false)
                )
            }
        } catch (e: Exception) {
            throw domain.error.UnknownErrorException("Failed to get user preferences: ${e.message}")
        }
    }

    suspend fun saveUserPreferences(preferences: UserPreferences) {
        try {
            context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit().putBoolean(PreferencesKeys.NIGHT_MODE.name, preferences.isNightModeEnabled).apply()
        } catch (e: Exception) {
            throw PreferencesSaveException("Failed to save user preferences: ${e.message}")
        }
    }
}