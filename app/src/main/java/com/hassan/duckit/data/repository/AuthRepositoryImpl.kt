package com.hassan.duckit.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hassan.duckit.core.Constants.AUTH_TOKEN_KEY_STRING
import com.hassan.duckit.data.api.DuckItApi
import com.hassan.duckit.data.api.models.AuthRequest
import com.hassan.duckit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: DuckItApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey(AUTH_TOKEN_KEY_STRING)
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            val response = api.signIn(AuthRequest(email, password))
            when {
                response.isSuccessful -> {
                    response.body()?.token?.let { token ->
                        saveToken(token)
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
            val response = api.signUp(AuthRequest(email, password))
            when {
                response.isSuccessful -> {
                    response.body()?.token?.let { token ->
                        saveToken(token)
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
        return dataStore.data.first()[AUTH_TOKEN]
    }

    private suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }
}
