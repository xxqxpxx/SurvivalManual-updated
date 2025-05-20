package data.local.test

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import domain.model.UserPreferences
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.refactor.data.local.PreferencesDataSource
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PreferencesDataSourceTest {

    private lateinit var preferencesDataSource: PreferencesDataSource
    private lateinit var sharedPreferences: SharedPreferences
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val PREF_NAME = "user_preferences"

    @Before
    fun setup() {
        // Use a unique name for each test to avoid conflicts
        val testPrefName = PREF_NAME + System.currentTimeMillis()
        sharedPreferences = context.getSharedPreferences(testPrefName, Context.MODE_PRIVATE)
        preferencesDataSource = PreferencesDataSource(context)
    }

    @After
    fun teardown() {
        // Clear SharedPreferences after each test
        sharedPreferences.edit().clear().commit()
    }

    @Test
    fun getUserPreferences_returnsDefaultPreferences_whenNoPreferencesSaved() = runBlocking {
        val preferences = preferencesDataSource.getUserPreferences()

        // Assuming default preferences are set in the data source
        assertThat(preferences).isNotNull()
        // Add assertions for default values based on your implementation
        // assertThat(preferences.isNightModeEnabled).isEqualTo(false)
    }

    @Test
    fun saveUserPreferences_savesPreferencesCorrectly() = runBlocking {
        val initialPreferences = UserPreferences(isNightModeEnabled = false) // Example initial
        preferencesDataSource.saveUserPreferences(initialPreferences)

        val savedPreferences = preferencesDataSource.getUserPreferences()

        assertThat(savedPreferences).isEqualTo(initialPreferences)

        val updatedPreferences = UserPreferences(isNightModeEnabled = true) // Example update
        preferencesDataSource.saveUserPreferences(updatedPreferences)

        val updatedSavedPreferences = preferencesDataSource.getUserPreferences()

        assertThat(updatedSavedPreferences).isEqualTo(updatedPreferences)
    }

    @Test
    fun getUserPreferences_returnsSavedPreferences_whenPreferencesSaved() = runBlocking {
        val preferencesToSave = UserPreferences(isNightModeEnabled = true)
        sharedPreferences.edit()
            .putBoolean("isNightModeEnabled", preferencesToSave.isNightModeEnabled)
            .apply()

        val retrievedPreferences = preferencesDataSource.getUserPreferences()

        assertThat(retrievedPreferences).isEqualTo(preferencesToSave)
    }

    // Add tests for error scenarios if applicable to SharedPreferences operations,
    // though SharedPreferences itself is generally robust and exceptions might be rare
    // unless dealing with file system issues or security exceptions which are harder to mock.
}