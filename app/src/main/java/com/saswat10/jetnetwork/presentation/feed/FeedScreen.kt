package com.saswat10.jetnetwork.presentation.feed

import android.content.res.Configuration
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.ui.theme.Pink
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID
import com.saswat10.jetnetwork.utils.formattedTime

fun FeedScreen() {

}


@Composable
fun FeedItem(
    feed: Post,
    addComment: () -> Unit, // TODO
    toggleLike: () -> Unit, // TODO
    toggleBookMark: () -> Unit // TODO
) {
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
                                Icons.Default.Favorite,
                                "Like",
                                tint = Pink,
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .clickable {}
                                    .padding(4.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("24", style = MaterialTheme.typography.labelLarge, color = Pink)
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
                            Text("24", style = MaterialTheme.typography.labelLarge)
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
    FeedItem(DEFAULT_POST, {}, {}, {})
}

@Preview(
    apiLevel = 34, showSystemUi = false, showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun FeedItemDarkPreview() {
    FeedItem(DEFAULT_POST, {}, {}, {})
}

val DEFAULT_POST =
    Post(DEFAULT_POST_ID, "Default Post Title", "Default Post Content", false, "-1", "Saswat", "")