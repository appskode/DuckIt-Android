package com.hassan.duckit.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
