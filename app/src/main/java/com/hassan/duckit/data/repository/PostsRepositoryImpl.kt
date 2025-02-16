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

    override suspend fun upVotePost(postId: String): Result<Int> {
        return try {
            val response = api.upVotePost(postId)
            if (response.isSuccessful) {
                response.body()?.let { voteResponse ->
                    Result.success(voteResponse.upVotes)
                } ?: Result.failure(Exception("Empty upVote response"))
            } else {
                Result.failure(Exception("Failed to upVote post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downVotePost(postId: String): Result<Int> {
        return try {
            val response = api.downVotePost(postId)
            if (response.isSuccessful) {
                response.body()?.let { voteResponse ->
                    Result.success(voteResponse.upVotes)
                } ?: Result.failure(Exception("Empty downVote response"))
            } else {
                Result.failure(Exception("Failed to downVote post: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
