package com.hassan.duckit.data.repository

import com.hassan.duckit.data.api.service.DuckItService
import com.hassan.duckit.data.api.models.CreatePostRequest
import com.hassan.duckit.domain.repository.CreatePostRepository
import javax.inject.Inject


class CreatePostRepositoryImpl @Inject constructor(
    private val api: DuckItService,
) : CreatePostRepository {
    override suspend fun createPost(headline: String, imageUrl: String): Result<Unit> {
        return try {
            val response = api.createPost(
                request = CreatePostRequest(headline, imageUrl)
            )

            when {
                response.code() == 201 || response.code() == 200 -> Result.success(Unit)
                response.code() == 401 || response.code() == 403 -> Result.failure(Exception("Not authorized"))
                else -> Result.failure(Exception("Failed to create post"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
