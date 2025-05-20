package org.ligi.survivalmanual.refactor.presentation.ui.preferences_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.error.DomainException
import domain.use_case.GetUserPreferencesUseCase
import domain.use_case.SaveUserPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ligi.survivalmanual.refactor.domain.UserPreferences
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val saveUserPreferencesUseCase: SaveUserPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    init {
        handleIntent(Intent.LoadPreferences)
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            Intent.LoadPreferences -> loadPreferences()
            is Intent.SavePreferences -> savePreferences(intent.preferences)
            // Handle other preference change intents here
            else -> {}
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val preferences = getUserPreferencesUseCase()
                _state.value = State.Success(preferences)
            } catch (e: DomainException) {
                // Handle error state
                _state.value =
                    State.Error(e.message ?: "An unknown error occurred while loading preferences")
            }
        }
    }

    private fun savePreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is State.Success) {
                try {
                    saveUserPreferencesUseCase(preferences)
                    _state.value = State.Success(preferences)
                } catch (e: DomainException) {
                    // Handle error state
                    _state.value = State.Error("Failed to save preferences") // Example error state
                }
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val userPreferences: UserPreferences) : State()
        data class Error(val message: String) : State() // Example error state
        data class DisplayPreferences(val preferences: UserPreferences) : State()
    }

    sealed class Intent {
        object LoadPreferences : Intent()
        data class SavePreferences(val preferences: UserPreferences) : Intent()
        data class ToggleNightMode(val checked: Boolean) : Intent()
        // Define other preference change intents here
    }
}