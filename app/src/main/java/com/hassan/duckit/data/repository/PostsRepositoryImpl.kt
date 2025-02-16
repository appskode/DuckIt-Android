package com.hassan.duckit.data.repository

import com.hassan.duckit.data.api.DuckItService
import com.hassan.duckit.domain.repository.PostsRepository
import com.hassan.duckit.presentation.post.model.DuckPost
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val api: DuckItService
) : PostsRepository {
    override suspend fun getPosts(): Result<List<DuckPost>> {
        return try {
            val response = api.getPosts()
            if (response.isSuccessful) {
                response.body()?.let { postsResponse ->
                    Result.success(
                        postsResponse.posts.map { post ->
                            DuckPost(
                                id = post.id,
                                headline = post.headline,
                                imageUrl = post.image,
                                upVotes = post.upVotes
                            )
                        }
                    )
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to fetch posts: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun upVotePost(postId: String): Result<Unit> {
        return try {
            // TODO: Implement actual API call for upVoting
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downVotePost(postId: String): Result<Unit> {
        return try {
            // TODO: Implement actual API call for downVoting
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}