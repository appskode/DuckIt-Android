package com.hassan.duckit.data.network.interceptor

import com.hassan.duckit.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // TODO: we can also use qualifiers to avoid adding token to sign in and sign up requests
        if (originalRequest.url.encodedPath.contains("/signin") ||
            originalRequest.url.encodedPath.contains("/signup")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { authRepository.getStoredToken() }

        val modifiedRequest = token?.let { authToken ->
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
        } ?: originalRequest

        return chain.proceed(modifiedRequest)
    }
}
