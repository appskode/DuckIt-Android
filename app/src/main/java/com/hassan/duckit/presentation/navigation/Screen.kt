package com.hassan.duckit.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Posts : Screen()

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object CreatePost : Screen()
}
