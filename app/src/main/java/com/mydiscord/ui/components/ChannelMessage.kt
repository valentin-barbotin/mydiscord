package com.mydiscord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.kord.common.entity.DiscordMessage
import dev.kord.core.cache.data.MessageData
import kotlinx.datetime.Instant
import java.lang.String.format
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GuildChannelMessage(message: MessageData, avatar: String) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        val locale = Locale.FRENCH
        val date = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(
            Date(message.timestamp.epochSeconds * 1000)
        )
        val time = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(
            Date(message.timestamp.epochSeconds * 1000)
        )

        val messageDate = format("%s %s", date, time);

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            println(message.author)
            AsyncImage(
                model = avatar,
                contentDescription = "photo",
                Modifier
                    .size(60.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 10.dp)
                        .fillMaxWidth(),
                ) {
                    Text(message.author.globalName.value ?: message.author.username)
                    Text(messageDate)
                }
                Text(message.content)
            }
        }
    }
}