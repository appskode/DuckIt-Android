package com.hassan.duckit.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
            PostsScreen(
                onNavigateToAuth = { navController.navigate(Screen.Auth) },
                onNavigateToCreatePost = { navController.navigate(Screen.CreatePost) }
            )
        }

        composable<Screen.Auth> {
            AuthScreen(
                onAuthSuccess = { navController.navigateUp() },
                onNavigateBack = { navController.navigateUp() },
                onLogoutSuccess = { navController.navigateUp() },
            )
        }

        composable<Screen.CreatePost> {
            CreatePostScreen(
                onPostCreated = { navController.navigateUp() },
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
