package org.ligi.survivalmanual.refactor.presentation.ui.image_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.use_case.GetImageUseCase
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ImageScreenState())
    val state: StateFlow<ImageScreenState> = _state.asStateFlow()

    fun loadImage(imageId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getImageUseCase(imageId)
                .onSuccess { imageData ->
                    _state.value = _state.value.copy(isLoading = false, imageData = imageData)
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "An error occurred"
                    )
                }
        }
    }
}

data class ImageScreenState(
    val isLoading: Boolean = false,
    val imageData: ByteArray? = null,
    val error: String? = null
)