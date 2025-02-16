package com.hassan.duckit.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun getStoredToken(): String?
    suspend fun clearToken()
}
