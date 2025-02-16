package com.hassan.duckit.presentation.post.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hassan.duckit.presentation.post.model.DuckPost
import com.hassan.duckit.ui.theme.DuckItTheme

@Composable
fun PostsScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    val isAuthenticated = false // TODO: This will come from ViewModel later

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DuckIt") },
                actions = {
                    if (!isAuthenticated) {
                        IconButton(onClick = onNavigateToAuth) {
                            Icon(Icons.Default.Person, "Login")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (isAuthenticated) {
                FloatingActionButton(
                    onClick = onNavigateToCreatePost,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Post",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(10) { index ->
                PostItem(
                    post = DuckPost(
                        id = index.toString(),
                        headline = "Sample Duck Post $index",
                        imageUrl = "some url",
                        upVotes = index * 10
                    ),
                    isAuthenticated = isAuthenticated,
                    onUpVote = {},
                    onDownVote = {}
                )
            }
        }
    }
}

@Preview(showBackground = true,)
@Composable
fun PostsScreenPreview() {
    DuckItTheme {
        PostsScreen(
            onNavigateToAuth = {},
            onNavigateToCreatePost = {}
        )
    }
}
