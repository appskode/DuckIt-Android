package com.hassan.duckit.presentation.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.hassan.duckit.presentation.auth.AuthScreenEvent
import com.hassan.duckit.presentation.auth.AuthViewModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authScreenEvent by viewModel.authScreenEvent.collectAsState()

    LaunchedEffect(authScreenEvent) {
        when (authScreenEvent) {
            AuthScreenEvent.LoginSuccess -> onAuthSuccess()
            AuthScreenEvent.LogoutSuccess -> onLogoutSuccess()
            AuthScreenEvent.None -> {}
        }
    }

    if (uiState.isAuthenticated) {
        AccountContent(
            onAction = viewModel::onEvent,
            onNavigateBack = onNavigateBack
        )
    } else {
        AuthContent(
            uiState = uiState,
            onAction = viewModel::onEvent,
            onNavigateBack = onNavigateBack
        )
    }
}
