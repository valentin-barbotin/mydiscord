package com.mydiscord.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mydiscord.ui.theme.DiscordDark
import com.mydiscord.ui.theme.DiscordLight
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.MessageData

@Composable
@Preview(showBackground = true)
fun GuildChannelMessages(
    modifier: Modifier = Modifier,
    messages: SnapshotStateList<MessageData> = mutableStateListOf(),
    avatars: HashMap<Snowflake, String> = hashMapOf(),
    openAttachment: (String) -> Unit,
    context: Context = LocalContext.current
) {
    val verticalGap = 12.dp
    val size = 680.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(vertical = verticalGap),
        userScrollEnabled = true,
        reverseLayout = true,
        modifier = modifier
            .background(DiscordDark)
            .fillMaxWidth()
            .heightIn(max = size, min = size)

    ) {
        val paddingValues = PaddingValues(
            horizontal = 16.dp,
            vertical = 2.dp
        )

        if (messages.isEmpty()) {
            item {
                Text(
                    text = "No messages",
                    Modifier.padding(paddingValues)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height((-verticalGap.value).dp))
        }

        items(messages.sortedBy {
            it.timestamp.epochSeconds
        }.reversed()) { message ->
            GuildChannelMessage(
                message = message,
                avatar = avatars.getOrDefault(message.author.id, "https://cdn.discordapp.com/embed/avatars/0.png"),
                contentPadding = paddingValues,
                openAttachment = openAttachment
            )
        }
    }
}