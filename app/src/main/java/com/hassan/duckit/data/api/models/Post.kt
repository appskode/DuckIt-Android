package com.hassan.duckit.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("author") val author: String,
    @SerialName("headline") val headline: String,
    @SerialName("id") val id: String,
    @SerialName("image") val image: String,
    @SerialName("upvotes") val upVotes: Int,
)
