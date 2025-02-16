package com.hassan.duckit.data.repository

import com.hassan.duckit.base.MainDispatcherRule
import com.hassan.duckit.data.api.models.CreatePostRequest
import com.hassan.duckit.data.api.service.DuckItService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertTrue

class CreatePostRepositoryImplTest {

    private lateinit var api: DuckItService
    private lateinit var createPostRepository: CreatePostRepositoryImpl

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        api = mock(DuckItService::class.java)
        createPostRepository = CreatePostRepositoryImpl(api)
    }

    @Test
    fun `createPost success should return Result success`() = runTest {
        // Given
        val headline = "New Post"
        val imageUrl = "https://example.com/image.jpg"
        val mockResponse = Response.success<Unit>(Unit)

        `when`(api.createPost(CreatePostRequest(headline, imageUrl))).thenReturn(mockResponse)

        // When
        val result = createPostRepository.createPost(headline, imageUrl)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `createPost unauthorized should return error`() = runTest {
        // Given
        val headline = "New Post"
        val imageUrl = "https://example.com/image.jpg"
        val mockResponse = Response.error<Unit>(401, "".toResponseBody())

        `when`(api.createPost(CreatePostRequest(headline, imageUrl))).thenReturn(mockResponse)

        // When
        val result = createPostRepository.createPost(headline, imageUrl)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `createPost forbidden should return error`() = runTest {
        // Given
        val headline = "New Post"
        val imageUrl = "https://example.com/image.jpg"
        val mockResponse = Response.error<Unit>(403, "".toResponseBody())

        `when`(api.createPost(CreatePostRequest(headline, imageUrl))).thenReturn(mockResponse)

        // When
        val result = createPostRepository.createPost(headline, imageUrl)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `createPost failure should return generic error`() = runTest {
        // Given
        val headline = "New Post"
        val imageUrl = "https://example.com/image.jpg"
        val mockResponse = Response.error<Unit>(500, "".toResponseBody())

        `when`(api.createPost(CreatePostRequest(headline, imageUrl))).thenReturn(mockResponse)

        // When
        val result = createPostRepository.createPost(headline, imageUrl)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `createPost throws exception should return failure`() = runTest {
        // Given
        val headline = "New Post"
        val imageUrl = "https://example.com/image.jpg"
        val exception = RuntimeException("Network error")

        `when`(api.createPost(CreatePostRequest(headline, imageUrl))).thenThrow(exception)

        // When
        val result = createPostRepository.createPost(headline, imageUrl)

        // Then
        assertTrue(result.isFailure)
    }
}
