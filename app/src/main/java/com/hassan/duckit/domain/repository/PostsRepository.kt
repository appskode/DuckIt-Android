package com.hassan.duckit.domain.repository

import com.hassan.duckit.presentation.post.model.DuckPost

interface PostsRepository {
    suspend fun getPosts(): Result<List<DuckPost>>
    suspend fun upVotePost(postId: String): Result<Unit>
    suspend fun downVotePost(postId: String): Result<Unit>
}
