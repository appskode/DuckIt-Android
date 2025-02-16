package com.hassan.duckit.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsResponse(
    @SerialName("Posts") val posts: List<Post>,
)
