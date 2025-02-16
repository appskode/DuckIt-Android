package com.hassan.duckit.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.duckit.domain.repository.AuthRepository
import com.hassan.duckit.domain.repository.PostsRepository
import com.hassan.duckit.presentation.post.model.DuckPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsScreenState())
    val uiState = _uiState.asStateFlow()

    private val _postsScreenEvent = Channel<PostsScreenEvent>()
    val postsScreenEvent = _postsScreenEvent.receiveAsFlow()

    init {
        checkAuthStatus()
        loadPosts()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            val token = authRepository.getStoredToken()
            _uiState.update {
                it.copy(isAuthenticated = token != null)
            }
        }
    }

    fun loadPosts(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh || _uiState.value.posts.isEmpty()) {
                _uiState.update { it.copy(isLoading = true) }

                postsRepository.getPosts()
                    .onSuccess { posts ->
                        _uiState.update {
                            it.copy(
                                posts = posts,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Failed to load posts"
                            )
                        }
                    }
            }
        }
    }

    private fun onUpVote(postId: String) {
        viewModelScope.launch {
            postsRepository.upVotePost(postId)
                .onSuccess { updatedUpVotes ->
                    _uiState.update { currentState ->
                        val updatedPosts = currentState.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(
                                    upVotes = updatedUpVotes,
                                    isUpVoted = true,
                                    isDownVoted = false
                                )
                            } else post
                        }
                        currentState.copy(posts = updatedPosts)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = error.message ?: "Failed to upVote post")
                    }
                }
        }
    }

    private fun onDownVote(postId: String) {
        viewModelScope.launch {
            postsRepository.downVotePost(postId)
                .onSuccess { updatedUpVotes ->
                    _uiState.update { currentState ->
                        val updatedPosts = currentState.posts.map { post ->
                            if (post.id == postId) {
                                post.copy(
                                    upVotes = updatedUpVotes,
                                    isDownVoted = true,
                                    isUpVoted = false
                                )
                            } else post
                        }
                        currentState.copy(posts = updatedPosts)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = error.message ?: "Failed to downVote post")
                    }
                }
        }
    }

    fun onAction(action: PostsScreenAction) {
        when (action) {
            PostsScreenAction.Refresh -> loadPosts(isRefresh = true)
            PostsScreenAction.NavigateToAuth -> {
                viewModelScope.launch {
                    _postsScreenEvent.send(PostsScreenEvent.NavigateToAuth)
                }
            }

            PostsScreenAction.NavigateToCreatePost -> {
                viewModelScope.launch {
                    _postsScreenEvent.send(PostsScreenEvent.NavigateToCreatePost)
                }
            }

            is PostsScreenAction.UpVote -> onUpVote(action.postId)
            is PostsScreenAction.DownVote -> onDownVote(action.postId)
        }
    }
}

data class PostsScreenState(
    val posts: List<DuckPost> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

sealed interface PostsScreenEvent {
    data object NavigateToAuth : PostsScreenEvent
    data object NavigateToCreatePost : PostsScreenEvent
}

sealed interface PostsScreenAction {
    data object Refresh : PostsScreenAction
    data object NavigateToAuth : PostsScreenAction
    data object NavigateToCreatePost : PostsScreenAction
    data class UpVote(val postId: String) : PostsScreenAction
    data class DownVote(val postId: String) : PostsScreenAction
}
