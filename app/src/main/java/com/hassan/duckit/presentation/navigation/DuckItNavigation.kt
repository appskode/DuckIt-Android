package com.hassan.duckit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hassan.duckit.presentation.auth.ui.AuthScreen
import com.hassan.duckit.presentation.create_post.ui.CreatePostScreen
import com.hassan.duckit.presentation.post.ui.PostsScreen

@Composable
fun DuckItNavigation(
    navController: NavHostController = rememberNavController()
) {
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
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.CreatePost> {
            CreatePostScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
