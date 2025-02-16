package com.hassan.duckit.data.api

import com.hassan.duckit.data.api.models.AuthRequest
import com.hassan.duckit.data.api.models.AuthResponse
import com.hassan.duckit.data.api.models.PostsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DuckItApi {
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
}
