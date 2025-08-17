package com.saswat10.jetnetwork.presentation.feed

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.utils.formatName
import com.saswat10.jetnetwork.utils.formattedTime

@Composable
fun FeedItem(
    postWithLikes: PostWithLikes,
    getComments: () -> Unit,
    toggleLike: () -> Unit,
    modifier: Modifier,
    currentUserId: String,
    openEditPage: () -> Unit
) {

    val feed = postWithLikes.post
    val isLiked = postWithLikes.isLiked
    var isExpanded by remember { mutableStateOf(false) }
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 6.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
                if(feed.photoUrl == "null"){
                    Icon(Icons.Default.AccountCircle, null, Modifier.size(44.dp))
                }else {
                    AsyncImage(
                        model = feed.photoUrl,
                        contentDescription = feed.username,
                        Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    Modifier
                        .padding(top = 4.dp)
                        .weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(formatName(feed.username), style = MaterialTheme.typography.labelLarge)
                        Text(
                            formattedTime(feed.createdAt),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                if (currentUserId == postWithLikes.post.userId) {
                    Icon(Icons.Default.Edit, null, Modifier
                        .clip(CircleShape)
                        .clickable{
                            openEditPage()
                        }
                        .size(26.dp)
                        .padding(4.dp)
                    )

                }
            }


            Column(modifier = Modifier.padding(10.dp, 16.dp, 10.dp, 8.dp)) {
                Text(
                    feed.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    feed.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.animateContentSize()
                )
                if (feed.content.trim().length > 60) {
                    Text(
                        text = if (isExpanded) "Show Less" else "Read More",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.End)
                            .clickable { isExpanded = !isExpanded }
                    )
                }

            }

            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { toggleLike() },
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

                    TextButton(onClick = { getComments() }) {
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
            }
        }
    }
}