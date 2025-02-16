package com.hassan.duckit.data.network.interceptor

import com.hassan.duckit.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthInterceptorTest {
    private lateinit var tokenManager: TokenManager
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var response: Response
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Given
        tokenManager = mock()
        authInterceptor = AuthInterceptor(tokenManager, testDispatcher)
        chain = mock()
        response = mock()

        whenever(chain.proceed(any())).thenReturn(response)
        runBlocking { whenever(tokenManager.getToken()).thenReturn("test-token") }
    }

    private fun createTestRequest(path: String): Request = Request.Builder()
        .url("https://example.com$path")
        .build()

    private fun mockRequest(path: String) {
        val request = createTestRequest(path)
        whenever(chain.request()).thenReturn(request)
    }

    private fun verifyNoAuthHeader() {
        val requestCaptor = argumentCaptor<Request>()
        verify(chain).proceed(requestCaptor.capture())
        assertNull(requestCaptor.firstValue.header("Authorization"))
    }

    private fun verifyAuthHeader(expectedToken: String = "test-token") {
        val requestCaptor = argumentCaptor<Request>()
        verify(chain).proceed(requestCaptor.capture())
        assertEquals(
            "Bearer $expectedToken",
            requestCaptor.firstValue.header("Authorization")
        )
    }

    @Test
    fun `should not add token for signin request`() {
        // Given
        mockRequest("/signin")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyNoAuthHeader()
    }

    @Test
    fun `should not add token for signup request`() {
        // Given
        mockRequest("/signup")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyNoAuthHeader()
    }

    @Test
    fun `should not add token for upvote request`() {
        // Given
        mockRequest("/posts/123/upvote")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyNoAuthHeader()
    }

    @Test
    fun `should not add token for downvote request`() {
        // Given
        mockRequest("/posts/123/downvote")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyNoAuthHeader()
    }

    @Test
    fun `should add token for other requests when token exists`() {
        // Given
        mockRequest("/posts")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyAuthHeader()
    }

    @Test
    fun `should not add token for other requests when token is null`() {
        // Given
        runBlocking { whenever(tokenManager.getToken()).thenReturn(null) }
        mockRequest("/posts")

        // When
        authInterceptor.intercept(chain)

        // Then
        verifyNoAuthHeader()
    }
}
