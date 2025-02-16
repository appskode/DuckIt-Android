package com.hassan.duckit.presentation.post

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hassan.duckit.presentation.auth.ui.AccountContent
import com.hassan.duckit.ui.theme.DuckItTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLogoutButton_click_logoutSuccess() {
        val onLogoutSuccessCalled = mutableStateOf(false)

        composeTestRule.setContent {
            DuckItTheme {
                AccountContent(
                    onAction = { onLogoutSuccessCalled.value = true },
                    onNavigateBack = {}
                )
            }
        }

        // Click the Logout button
        composeTestRule.onNodeWithText("Logout").performClick()

        // Verify that the logout success callback is triggered
        assert(onLogoutSuccessCalled.value)
    }
}
