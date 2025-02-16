package com.hassan.duckit.data.network.interceptor

import com.hassan.duckit.data.local.TokenManager
import com.hassan.duckit.di.qualifier.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // qualifiers can be used for providing authorized and unauthorized retrofit instances
        // for some reason voting has issues with the authorization header
        if (originalRequest.url.encodedPath.contains("/signin") ||
            originalRequest.url.encodedPath.contains("/signup") ||
            originalRequest.url.encodedPath.contains("/upvote") ||
            originalRequest.url.encodedPath.contains("/downvote")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking(dispatcher) { tokenManager.getToken() }

        val modifiedRequest = token?.let { authToken ->
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
        } ?: originalRequest

        return chain.proceed(modifiedRequest)
    }
}
