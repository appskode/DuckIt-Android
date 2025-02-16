package com.hassan.duckit.data.repository

import com.hassan.duckit.data.api.service.DuckItService
import com.hassan.duckit.data.api.models.AuthRequest
import com.hassan.duckit.data.local.TokenManager
import com.hassan.duckit.domain.repository.AuthRepository
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: DuckItService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            val response = api.signIn(AuthRequest(email, hashInput(password)))
            when {
                response.isSuccessful -> {
                    response.body()?.token?.let { token ->
                        tokenManager.saveToken(token)
                        Result.success(Unit)
                    } ?: Result.failure(Exception("Invalid response"))
                }
                response.code() == 403 -> Result.failure(Exception("Incorrect password"))
                response.code() == 404 -> Result.failure(Exception("Account not found"))
                else -> Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            val response = api.signUp(AuthRequest(email, hashInput(password)))
            when {
                response.isSuccessful -> {
                    response.body()?.token?.let { token ->
                        tokenManager.saveToken(token)
                        Result.success(Unit)
                    } ?: Result.failure(Exception("Invalid response"))
                }
                response.code() == 409 -> Result.failure(Exception("Account already exists"))
                else -> Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStoredToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun clearToken() {
        tokenManager.clearToken()
    }

    private fun hashInput(input: String): String {
        // this is just some basic hashing for now
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
