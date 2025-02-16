package com.hassan.duckit.data.repository

import com.hassan.duckit.base.MainDispatcherRule
import com.hassan.duckit.data.api.models.AuthRequest
import com.hassan.duckit.data.api.models.AuthResponse
import com.hassan.duckit.data.api.service.DuckItService
import com.hassan.duckit.data.local.TokenManager
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private lateinit var api: DuckItService
    private lateinit var tokenManager: TokenManager
    private lateinit var authRepository: AuthRepositoryImpl

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        api = mock(DuckItService::class.java)
        tokenManager = mock(TokenManager::class.java)
        authRepository = AuthRepositoryImpl(api, tokenManager)
    }

    @Test
    fun `signIn success should save token`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val fakeToken = "fake_token"
        val mockResponse = Response.success(AuthResponse(fakeToken))

        `when`(api.signIn(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signIn(email, password)

        // Then
        assertTrue(result.isSuccess)
        verify(tokenManager).saveToken(fakeToken)
    }

    @Test
    fun `signIn incorrect password should return error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrong_password"
        val mockResponse = Response.error<AuthResponse>(403, "".toResponseBody())

        `when`(api.signIn(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signIn(email, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `signIn account not found should return error`() = runTest {
        // Given
        val email = "notfound@example.com"
        val password = "password"
        val mockResponse = Response.error<AuthResponse>(404, "".toResponseBody())

        `when`(api.signIn(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signIn(email, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `signIn failure should return generic error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val mockResponse = Response.error<AuthResponse>(500, "".toResponseBody())

        `when`(api.signIn(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signIn(email, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `signUp success should save token`() = runTest {
        // Given
        val email = "newuser@example.com"
        val password = "password"
        val fakeToken = "fake_token"
        val mockResponse = Response.success(AuthResponse(fakeToken))

        `when`(api.signUp(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signUp(email, password)

        // Then
        assertTrue(result.isSuccess)
        verify(tokenManager).saveToken(fakeToken)
    }

    @Test
    fun `signUp account already exists should return error`() = runTest {
        // Given
        val email = "existing@example.com"
        val password = "password"
        val mockResponse = Response.error<AuthResponse>(409, "".toResponseBody())

        `when`(api.signUp(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signUp(email, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `signUp failure should return generic error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val mockResponse = Response.error<AuthResponse>(500, "".toResponseBody())

        `when`(api.signUp(AuthRequest(email, password))).thenReturn(mockResponse)

        // When
        val result = authRepository.signUp(email, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getStoredToken should return token`() = runTest {
        // Given
        val fakeToken = "stored_token"
        `when`(tokenManager.getToken()).thenReturn(fakeToken)

        // When
        val result = authRepository.getStoredToken()

        // Then
        assertEquals(result, fakeToken)
    }

    @Test
    fun `clearToken should call tokenManager`() = runTest {
        // When
        authRepository.clearToken()

        // Then
        verify(tokenManager).clearToken()
    }
}
