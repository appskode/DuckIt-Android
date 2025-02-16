package com.hassan.duckit.presentation.post.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.duckit.presentation.post.PostsScreenAction
import com.hassan.duckit.presentation.post.PostsScreenState

@Composable
fun PostsScreenContent(
    uiState: PostsScreenState,
    onAction: (PostsScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DuckIt") },
                actions = {
                    val description = if (!uiState.isAuthenticated) {
                        "Login"
                    } else {
                        "Account"
                    }
                    IconButton(onClick = { onAction(PostsScreenAction.NavigateToAuth) }) {
                        Icon(Icons.Default.Person, description)
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.isAuthenticated) {
                FloatingActionButton(
                    onClick = { onAction(PostsScreenAction.NavigateToCreatePost) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Post",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorContent(
                        errorMessage = uiState.error,
                        onRetry = { onAction(PostsScreenAction.Refresh) }
                    )
                }
                uiState.posts.isEmpty() -> {
                    EmptyPostsContent(
                        isAuthenticated = uiState.isAuthenticated,
                    )
                }
                else -> {
                    PostsList(
                        posts = uiState.posts,
                        isAuthenticated = uiState.isAuthenticated,
                        onUpVote = { postId -> onAction(PostsScreenAction.UpVote(postId)) },
                        onDownVote = { postId -> onAction(PostsScreenAction.DownVote(postId)) }
                    )
                }
            }
        }
    }
}
