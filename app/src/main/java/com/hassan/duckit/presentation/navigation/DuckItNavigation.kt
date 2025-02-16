package com.hassan.duckit.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hassan.duckit.core.Constants.SHOULD_CHECK_AUTH_KEY_STRING
import com.hassan.duckit.core.Constants.SHOULD_REFRESH_POSTS_KEY_STRING
import com.hassan.duckit.presentation.auth.AuthViewModel
import com.hassan.duckit.presentation.auth.ui.AuthScreen
import com.hassan.duckit.presentation.create_post.ui.CreatePostScreen
import com.hassan.duckit.presentation.post.ui.PostsScreen

@Composable
fun DuckItNavigation(
    navController: NavHostController = rememberNavController(),
    onBackPress: () -> Unit
) {
    val currentBackStack by navController.currentBackStackEntryAsState()

    BackHandler {
        val isAtRoot = currentBackStack?.destination?.hasRoute<Screen.Posts>() ?: true
        if (isAtRoot) {
            onBackPress()
        } else {
            navController.navigateUp()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Posts
    ) {
        composable<Screen.Posts> {
            val shouldRefreshPosts = it.savedStateHandle.get<Boolean>(SHOULD_REFRESH_POSTS_KEY_STRING) ?: false
            val shouldCheckAuth = it.savedStateHandle.get<Boolean>(SHOULD_CHECK_AUTH_KEY_STRING) ?: false

            PostsScreen(
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth)
                },
                onNavigateToCreatePost = {
                    navController.navigate(Screen.CreatePost)
                },
                shouldRefreshPosts = shouldRefreshPosts,
                shouldCheckAuth = shouldCheckAuth
            )

            LaunchedEffect(shouldRefreshPosts, shouldCheckAuth) {
                if (shouldRefreshPosts) {
                    it.savedStateHandle[SHOULD_REFRESH_POSTS_KEY_STRING] = false
                }
                if (shouldCheckAuth) {
                    it.savedStateHandle[SHOULD_CHECK_AUTH_KEY_STRING] = false
                }
            }
        }

        composable<Screen.Auth> {
            AuthScreen(
                onAuthSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(SHOULD_CHECK_AUTH_KEY_STRING, true)
                    navController.navigateUp()
                },
                onNavigateBack = { navController.navigateUp() },
                onLogoutSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(SHOULD_CHECK_AUTH_KEY_STRING, true)
                    navController.navigateUp()
                },
            )
        }

        composable<Screen.CreatePost> {
            CreatePostScreen(
                onPostCreated = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(SHOULD_REFRESH_POSTS_KEY_STRING, true)
                    navController.navigateUp()
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
