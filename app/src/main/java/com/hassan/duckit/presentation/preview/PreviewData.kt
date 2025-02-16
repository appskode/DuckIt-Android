package com.hassan.duckit.presentation.preview

import com.hassan.duckit.domain.model.DuckPost

object PreviewData {
    val samplePosts = listOf(
        DuckPost(
            id = "1",
            headline = "A Cute Duck Swimming in the Pond",
            imageUrl = "some url",
            upVotes = 42
        ),
        DuckPost(
            id = "2",
            headline = "Majestic Mallard in Morning Light",
            imageUrl = "some url",
            upVotes = 128,
            isUpVoted = true
        ),
        DuckPost(
            id = "3",
            headline = "Baby Ducks Following Their Mother",
            imageUrl = "some url",
            upVotes = 85,
            isDownVoted = true
        )
    )
}
