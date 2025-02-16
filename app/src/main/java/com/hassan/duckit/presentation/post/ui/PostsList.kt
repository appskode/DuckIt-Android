package com.hassan.duckit.presentation.post.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.duckit.presentation.post.model.DuckPost

@Composable
fun PostsList(
    posts: List<DuckPost>,
    isAuthenticated: Boolean,
    onUpVote: (String) -> Unit,
    onDownVote: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                isAuthenticated = isAuthenticated,
                onUpVote = { onUpVote(post.id) },
                onDownVote = { onDownVote(post.id) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }
    }
}
