package com.hassan.duckit.presentation.post.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hassan.duckit.presentation.post.PostsScreenEvent
import com.hassan.duckit.presentation.post.PostsScreenState
import com.hassan.duckit.presentation.post.PostsViewModel
import com.hassan.duckit.presentation.preview.PreviewData.samplePosts
import com.hassan.duckit.ui.theme.DuckItTheme

@Composable
fun PostsScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToCreatePost: () -> Unit,
    shouldRefreshPosts: Boolean = false,
    shouldCheckAuth: Boolean = false,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.postsScreenEvent.collect { event ->
            when (event) {
                PostsScreenEvent.NavigateToAuth -> onNavigateToAuth()
                PostsScreenEvent.NavigateToCreatePost -> onNavigateToCreatePost()
            }
        }
    }

    LaunchedEffect(shouldRefreshPosts, shouldCheckAuth) {
        if (shouldCheckAuth) {
            viewModel.checkAuthStatus()
        }
        if (shouldRefreshPosts) {
            viewModel.loadPosts(isRefresh = true)
        }
    }

    PostsScreenContent(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}


@Preview(showBackground = true)
@Composable
fun PostsScreenPreview() {
    DuckItTheme {
        PostsScreenContent(
            uiState = PostsScreenState(
                posts = samplePosts,
                isAuthenticated = true
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyPostsPreview() {
    DuckItTheme {
        PostsScreenContent(
            uiState = PostsScreenState(
                posts = emptyList(),
                isAuthenticated = true
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPostsPreview() {
    DuckItTheme {
        PostsScreenContent(
            uiState = PostsScreenState(
                posts = emptyList(),
                error = "Failed to load posts",
                isAuthenticated = false
            ),
            onAction = {}
        )
    }
}
