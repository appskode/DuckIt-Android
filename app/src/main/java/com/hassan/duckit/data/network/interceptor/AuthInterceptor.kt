package com.hassan.duckit.data.network.interceptor

import com.hassan.duckit.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // TODO: we can also use qualifiers to avoid adding token to sign in and sign up requests
        // Right Now the UpVote and DownVote requests are also not accepting a token, adding the token fails them for some reason
        if (originalRequest.url.encodedPath.contains("/signin") ||
            originalRequest.url.encodedPath.contains("/signup") ||
            originalRequest.url.encodedPath.contains("/upvote") ||
            originalRequest.url.encodedPath.contains("/downvote")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenManager.getToken() }

        val modifiedRequest = token?.let { authToken ->
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
        } ?: originalRequest

        return chain.proceed(modifiedRequest)
    }
}
