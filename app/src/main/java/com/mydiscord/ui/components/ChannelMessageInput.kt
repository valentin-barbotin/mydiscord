package com.mydiscord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

@Composable
fun GuildChannelMessageInput(
    lifecycleScope: CoroutineScope,
    modifier: Modifier = Modifier,
    selectedChannel: TopGuildChannel,
    sendMessage: KSuspendFunction2<String, TextChannel, Unit> //edit channel type
) {
    Row(
        modifier = modifier
            //.defaultMinSize(minHeight = 50.dp)
            //.height(50.dp)
            .fillMaxWidth()
            .heightIn(max = 50.dp)
    ) {
        IconButton(onClick = {
            // more
        }) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "more",
                modifier = Modifier
                    .size(20.dp)
            )
        }

        val givenToken = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = givenToken.value,
            onValueChange = {
                givenToken.value = it
            },
            label = { Text("Enter your message") }
        )
        if (givenToken.value.text.isNotEmpty())
            IconButton(onClick = {
                lifecycleScope.launch {
                    if (selectedChannel is TextChannel) {
                        sendMessage(givenToken.value.text, selectedChannel)
                        givenToken.value = TextFieldValue()
                    }
                }
            }) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "send",
                    modifier = Modifier
                        .size(20.dp)
                )
            } else {
            IconButton(onClick = {
                // more
            }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "more",
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    }
}

