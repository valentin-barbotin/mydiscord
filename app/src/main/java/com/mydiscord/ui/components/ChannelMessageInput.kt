package com.mydiscord.ui.components

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
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
    onClickSendMessage: (String) -> Unit = {},
    onClickAdd: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 50.dp)
            //.height(50.dp)
            .fillMaxWidth()
            .heightIn(max = 50.dp, min = 50.dp)
    ) {
        IconButton(onClick = onClickAdd) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "more",
                modifier = Modifier
                    .size(20.dp)
            )
        }

        val message = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = message.value,
            onValueChange = {
                message.value = it
            },
            label = { Text("Enter your message") }
        )
        if (message.value.text.isNotEmpty())
            IconButton(onClick = {
                onClickSendMessage(message.value.text)
                message.value = TextFieldValue()
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

