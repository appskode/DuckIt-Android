package com.hassan.duckit

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.hassan.duckit.presentation.navigation.DuckItNavigation
import com.hassan.duckit.ui.theme.DuckItTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        setContent {
            DuckItTheme {
                DuckItNavigation(
                    onBackPress = { finish() },
                )
            }
        }
    }
}
