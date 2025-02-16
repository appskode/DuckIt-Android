package com.hassan.duckit.presentation.create_post.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hassan.duckit.presentation.create_post.CreatePostScreenAction
import com.hassan.duckit.presentation.create_post.CreatePostScreenEvent
import com.hassan.duckit.presentation.create_post.CreatePostScreenState
import com.hassan.duckit.presentation.create_post.CreatePostViewModel
import com.hassan.duckit.ui.theme.DuckItTheme

@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    onPostCreated: () -> Unit,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.createPostEvent.collect { event ->
            when (event) {
                CreatePostScreenEvent.PostCreated -> onPostCreated()
            }
        }
    }

    CreatePostContent(
        state = uiState,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun CreatePostContent(
    state: CreatePostScreenState,
    onAction: (CreatePostScreenAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.headline,
                onValueChange = { onAction(CreatePostScreenAction.UpdateHeadline(it)) },
                label = { Text("Headline") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.imageUrl,
                onValueChange = { onAction(CreatePostScreenAction.UpdateImageUrl(it)) },
                label = { Text("Image URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = !state.isLoading
            )

            if (state.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = state.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = { onAction(CreatePostScreenAction.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = !state.isLoading &&
                        state.headline.isNotEmpty() &&
                        state.imageUrl.isNotEmpty()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Post")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    DuckItTheme {
        CreatePostContent(
            onNavigateBack = {},
            state = CreatePostScreenState(),
            onAction = {},
        )
    }
}
