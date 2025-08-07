package com.saswat10.jetnetwork.presentation.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.ui.theme.Pink
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID
import com.saswat10.jetnetwork.utils.formattedTime

@Composable
fun FeedItem(
    postWithLikes: PostWithLikes,
    addComment: () -> Unit, // TODO
    toggleLike: () -> Unit, // TODO
    toggleBookMark: () -> Unit // TODO
) {

    val feed = postWithLikes.post
    val isLiked = postWithLikes.isLiked
    val animatedIsLiked: Color by animateColorAsState(if(isLiked) Pink else Color.Gray, label="color", animationSpec = tween(easing = Ease, durationMillis = 200))
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(6.dp, 4.dp).fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, null, Modifier.size(30.dp))
            Column{
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("@" + feed.username, style = MaterialTheme.typography.labelLarge)
                    Text(
                        formattedTime(feed.createdAt),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }


                Column(modifier = Modifier.padding(0.dp, 4.dp)) {
                    Text(feed.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(feed.content, style = MaterialTheme.typography.bodyMedium)
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if(!isLiked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                                    "Like",
                                    tint = animatedIsLiked,
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .clickable{toggleLike()}
                                        .padding(4.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(feed.likes.toString(), style = MaterialTheme.typography.labelLarge, color = animatedIsLiked)

                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.comment_24px),
                                "Comment",
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .clickable {}
                                    .padding(4.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(feed.comments.toString(), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Icon(
                        painter = painterResource(R.drawable.bookmark_24px_fill),
                        "Bookmark",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable {}
                            .padding(4.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        HorizontalDivider()

    }
}

@Preview(apiLevel = 34, showSystemUi = false, showBackground = false)
@Composable
fun FeedItemPreview() {
    FeedItem(PostWithLikes(), {}, {}, {})
}

@Preview(
    apiLevel = 34, showSystemUi = false, showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun FeedItemDarkPreview() {
    FeedItem(PostWithLikes(), {}, {}, {})
}