package com.hassan.duckit.domain.repository

interface CreatePostRepository {
    suspend fun createPost(headline: String, imageUrl: String): Result<Unit>
}
