package com.hassan.duckit.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    @SerialName("headline") val headline: String,
    @SerialName("image") val image: String
)
