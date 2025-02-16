package com.hassan.duckit.data.api.service

import com.hassan.duckit.data.api.models.AuthRequest
import com.hassan.duckit.data.api.models.AuthResponse
import com.hassan.duckit.data.api.models.CreatePostRequest
import com.hassan.duckit.data.api.models.PostsResponse
import com.hassan.duckit.data.api.models.VoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// all can be separated into different services for each feature
interface DuckItService {
    @POST("signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @GET("posts")
    suspend fun getPosts(): Response<PostsResponse>

    @POST("posts/{postId}/upvote")
    suspend fun upVotePost(
        @Path("postId") postId: String
    ): Response<VoteResponse>

    @POST("posts/{postId}/downvote")
    suspend fun downVotePost(
        @Path("postId") postId: String
    ): Response<VoteResponse>

    @POST("posts")
    suspend fun createPost(
        @Body request: CreatePostRequest
    ): Response<Unit>
}
