package com.hassan.duckit.data.repository

import com.hassan.duckit.base.MainDispatcherRule
import com.hassan.duckit.data.api.models.Post
import com.hassan.duckit.data.api.models.PostsResponse
import com.hassan.duckit.data.api.models.VoteResponse
import com.hassan.duckit.data.api.service.DuckItService
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertTrue

class PostsRepositoryImplTest {

    private lateinit var api: DuckItService
    private lateinit var postsRepository: PostsRepositoryImpl

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        api = mock(DuckItService::class.java)
        postsRepository = PostsRepositoryImpl(api)
    }

    @Test
    fun `getPosts success should return list of posts`() = runTest {
        // Given
        val mockResponse = PostsResponse(
            posts = listOf(
                Post("one", "Headline 1", "1", "https://example.com/image1.jpg", 5),
                Post("some", "Headline 2", "2", "https://example.com/image2.jpg", 10)
            )
        )
        val response = Response.success(mockResponse)

        `when`(api.getPosts()).thenReturn(response)

        // When
        val result = postsRepository.getPosts()

        // Then
        assertTrue(result.isSuccess)
        val posts = result.getOrNull()
        assertNotNull(posts)
        assertEquals(2, posts!!.size)
        assertEquals("1", posts[0].id)
        assertEquals("2", posts[1].id)
    }

    @Test
    fun `getPosts failure should return error`() = runTest {
        // Given
        val response = Response.error<PostsResponse>(500, "".toResponseBody())

        `when`(api.getPosts()).thenReturn(response)

        // When
        val result = postsRepository.getPosts()

        // Then
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertTrue(result.exceptionOrNull()?.message?.contains("Failed to fetch posts") == true)
    }

    @Test
    fun `upVotePost success should return updated upVotes count`() = runTest {
        // Given
        val postId = "1"
        val mockResponse = VoteResponse(11)
        val response = Response.success(mockResponse)

        `when`(api.upVotePost(postId)).thenReturn(response)

        // When
        val result = postsRepository.upVotePost(postId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(11, result.getOrNull())
    }

    @Test
    fun `upVotePost failure should return error`() = runTest {
        // Given
        val postId = "1"
        val response = Response.error<VoteResponse>(403, "".toResponseBody())

        `when`(api.upVotePost(postId)).thenReturn(response)

        // When
        val result = postsRepository.upVotePost(postId)

        // Then
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertTrue(result.exceptionOrNull()?.message?.contains("Failed to upVote post") == true)
    }

    @Test
    fun `downVotePost success should return updated upVotes count`() = runTest {
        // Given
        val postId = "1"
        val mockResponse = VoteResponse(9)
        val response = Response.success(mockResponse)

        `when`(api.downVotePost(postId)).thenReturn(response)

        // When
        val result = postsRepository.downVotePost(postId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(9, result.getOrNull())
    }

    @Test
    fun `downVotePost failure should return error`() = runTest {
        // Given
        val postId = "1"
        val response = Response.error<VoteResponse>(500, "".toResponseBody())

        `when`(api.downVotePost(postId)).thenReturn(response)

        // When
        val result = postsRepository.downVotePost(postId)

        // Then
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertTrue(result.exceptionOrNull()?.message?.contains("Failed to downVote post") == true)
    }
}
