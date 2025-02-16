package com.hassan.duckit.presentation.create_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.duckit.domain.repository.CreatePostRepository
import com.hassan.duckit.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostRepository: CreatePostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostScreenState())
    val uiState = _uiState.asStateFlow()

    private val _createPostEvent = Channel<CreatePostScreenEvent>()
    val createPostEvent = _createPostEvent.receiveAsFlow()

    fun onAction(action: CreatePostScreenAction) {
        when (action) {
            is CreatePostScreenAction.UpdateHeadline -> {
                _uiState.update {
                    it.copy(
                        headline = action.headline,
                        headlineError = when (val result = Validation.validateHeadline(action.headline)) {
                            is Validation.ValidationResult.Error -> result.message
                            is Validation.ValidationResult.Success -> null
                        }
                    )
                }
            }
            is CreatePostScreenAction.UpdateImageUrl -> {
                _uiState.update {
                    it.copy(
                        imageUrl = action.url,
                        imageUrlError = when (val result = Validation.validateImageUrl(action.url)) {
                            is Validation.ValidationResult.Error -> result.message
                            is Validation.ValidationResult.Success -> null
                        }
                    )
                }
            }
            CreatePostScreenAction.Submit -> {
                val state = _uiState.value
                val headlineValidation = Validation.validateHeadline(state.headline)
                val imageUrlValidation = Validation.validateImageUrl(state.imageUrl)

                if (headlineValidation is Validation.ValidationResult.Success &&
                    imageUrlValidation is Validation.ValidationResult.Success) {
                    createPost()
                } else {
                    _uiState.update {
                        it.copy(
                            headlineError = (headlineValidation as? Validation.ValidationResult.Error)?.message,
                            imageUrlError = (imageUrlValidation as? Validation.ValidationResult.Error)?.message
                        )
                    }
                }
            }
        }
    }

    private fun createPost() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val state = _uiState.value
            createPostRepository.createPost(state.headline, state.imageUrl)
                .onSuccess {
                    _createPostEvent.send(CreatePostScreenEvent.PostCreated)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to create post"
                    )}
                }
        }
    }
}

data class CreatePostScreenState(
    val headline: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val headlineError: String? = null,
    val imageUrlError: String? = null
) {
    val isValid: Boolean
        get() = headline.isNotBlank() &&
                imageUrl.isNotBlank() &&
                headlineError == null &&
                imageUrlError == null
}

sealed interface CreatePostScreenEvent {
    data object PostCreated : CreatePostScreenEvent
}

sealed interface CreatePostScreenAction {
    data class UpdateHeadline(val headline: String) : CreatePostScreenAction
    data class UpdateImageUrl(val url: String) : CreatePostScreenAction
    data object Submit : CreatePostScreenAction
}
