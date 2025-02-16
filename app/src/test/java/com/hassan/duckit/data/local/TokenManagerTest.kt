package com.hassan.duckit.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TokenManagerTest {
    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tempFolder.newFile("test_datastore.preferences_pb") }
        )
        tokenManager = TokenManager(testDataStore)
    }

    @Test
    fun `saveToken should store token in DataStore`() = testScope.runTest {
        // Given
        val testToken = "test_token"

        // When
        tokenManager.saveToken(testToken)

        // Then
        val savedToken = testDataStore.data.first()[TokenManager.AUTH_TOKEN]
        assertEquals(testToken, savedToken)
    }

    @Test
    fun `getToken should return null when no token is stored`() = testScope.runTest {
        // When
        val result = tokenManager.getToken()

        // Then
        assertNull(result)
    }

    @Test
    fun `getToken should return stored token`() = testScope.runTest {
        // Given
        val testToken = "test_token"
        testDataStore.edit { preferences ->
            preferences[TokenManager.AUTH_TOKEN] = testToken
        }

        // When
        val result = tokenManager.getToken()

        // Then
        assertEquals(testToken, result)
    }

    @Test
    fun `clearToken should remove token from DataStore`() = testScope.runTest {
        // Given
        val testToken = "test_token"
        testDataStore.edit { preferences ->
            preferences[TokenManager.AUTH_TOKEN] = testToken
        }

        // When
        tokenManager.clearToken()

        // Then
        val savedToken = testDataStore.data.first()[TokenManager.AUTH_TOKEN]
        assertNull(savedToken)
    }

    @Test
    fun `saveToken should overwrite existing token`() = testScope.runTest {
        // Given
        val initialToken = "initial_token"
        val newToken = "new_token"
        testDataStore.edit { preferences ->
            preferences[TokenManager.AUTH_TOKEN] = initialToken
        }

        // When
        tokenManager.saveToken(newToken)

        // Then
        val savedToken = testDataStore.data.first()[TokenManager.AUTH_TOKEN]
        assertEquals(newToken, savedToken)
    }
}
