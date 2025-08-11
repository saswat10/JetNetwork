package com.saswat10.jetnetwork.presentation.feed

import android.widget.Button
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.ui.theme.Pink
import com.saswat10.jetnetwork.utils.formatName
import com.saswat10.jetnetwork.utils.formattedTime

@Composable
fun FeedItem(
    postWithLikes: PostWithLikes,
    getComments: () -> Unit,
    toggleLike: () -> Unit,
    toggleBookMark: () -> Unit, // TODO
    modifier: Modifier
) {

    val feed = postWithLikes.post
    val isLiked = postWithLikes.isLiked
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 4.dp)
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, null, Modifier.size(36.dp))
            Column(Modifier.padding(top = 4.dp)) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(formatName(feed.username), style = MaterialTheme.typography.labelLarge)
                    Text(
                        formattedTime(feed.createdAt),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }


                Column(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 8.dp)) {
                    Text(feed.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(feed.content, style = MaterialTheme.typography.bodyLarge, maxLines = 3, overflow = TextOverflow.Ellipsis)
                }


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        FilledTonalButton(
                            onClick = { toggleLike() },
                            colors = if (isLiked) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors()
                        ) {
                            Icon(
                                if (!isLiked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                                "Like",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                feed.likes.toString(),
                            )
                        }

                        FilledTonalButton(onClick = { getComments() }) {
                            Icon(
                                painter = painterResource(R.drawable.comment_24px),
                                "Comment",
                                Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                feed.comments.toString(),
                            )
                        }

                    }
                    FilledTonalIconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.bookmark_24px),
                            "Bookmark",
                            modifier = Modifier
                                .size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

//@Preview(apiLevel = 34, showSystemUi = false, showBackground = false)
//@Composable
//fun FeedItemPreview() {
//    FeedItem(PostWithLikes(), {}, {}, {})
//}
//
@Preview(apiLevel = 34, showSystemUi = false, showBackground = true)
@Composable
fun FeedItemDarkPreview() {
    FeedItem(PostWithLikes(), {}, {}, {}, Modifier)
}