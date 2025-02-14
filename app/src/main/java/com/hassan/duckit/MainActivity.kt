package com.hassan.duckit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.hassan.duckit.presentation.navigation.DuckItNavigation
import com.hassan.duckit.ui.theme.DuckItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DuckItTheme {
                DuckItNavigation(navController)
            }
        }
    }
}
