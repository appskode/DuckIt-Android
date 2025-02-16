package com.hassan.duckit.domain.model

data class DuckPost(
    val id: String,
    val headline: String,
    val imageUrl: String,
    val upVotes: Int,
    val isUpVoted: Boolean = false,
    val isDownVoted: Boolean = false
)
