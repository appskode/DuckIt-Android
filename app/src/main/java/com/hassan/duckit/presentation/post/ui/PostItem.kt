package com.hassan.duckit.presentation.post.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hassan.duckit.domain.model.DuckPost
import com.hassan.duckit.presentation.preview.PreviewData

@Composable
fun PostItem(
    post: DuckPost,
    isAuthenticated: Boolean,
    onUpVote: () -> Unit,
    onDownVote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = post.headline,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = post.headline,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${post.upVotes}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (isAuthenticated) {
                        IconButton(onClick = onUpVote) {
                            Icon(Icons.Default.ThumbUp, "Upvote")
                        }
                        IconButton(onClick = onDownVote) {
                            Icon(Icons.Default.ThumbDown, "Downvote")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    PostItem(
        post = PreviewData.samplePosts[0],
        isAuthenticated = true,
        onUpVote = {},
        onDownVote = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PostItemUnauthenticatedPreview() {
    PostItem(
        post = PreviewData.samplePosts[0],
        isAuthenticated = false,
        onUpVote = {},
        onDownVote = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PostItemUpVotedPreview() {
    PostItem(
        post = PreviewData.samplePosts[1],
        isAuthenticated = true,
        onUpVote = {},
        onDownVote = {},
    )
}
