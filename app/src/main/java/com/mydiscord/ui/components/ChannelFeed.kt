package com.mydiscord.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.kord.core.cache.data.MessageData
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KSuspendFunction2

@Composable
fun GuildChannelFeed(
    channelOpened: MutableState<Boolean> = mutableStateOf(false),
    channelMessages: SnapshotStateList<MessageData> = mutableStateListOf(),
    lifecycleScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    selectedChannel: TopGuildChannel,
    openAttachment: (String) -> Unit,
    context: Context = LocalContext.current,
    onClickAdd: () -> Unit = {},
    onClickSendMessage: (String) -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        val (header, messages, input, topDiv, bottomDiv) = createRefs()

        GuildChannelHeader(modifier =
        Modifier.constrainAs(header)
        {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(topDiv.top)
        }, selectedChannel.name, onClick = {
            channelOpened.value = false
        }, onClickBack = {
            channelOpened.value = false
        }, onClickSearch = {
            // todo
        })

        Divider(
            modifier = Modifier
                .constrainAs(topDiv) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Gray))
        )

        GuildChannelMessages(modifier = Modifier.constrainAs(messages) {
            top.linkTo(topDiv.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(bottomDiv.top)
        }, messages = channelMessages, openAttachment = openAttachment, context = context)

        Divider(
            modifier = Modifier
                .constrainAs(bottomDiv) {
                    top.linkTo(messages.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    //bottom.linkTo(input.top)
                }
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Gray))
        )

        GuildChannelMessageInput(
            lifecycleScope,
            Modifier.constrainAs(input) {
            top.linkTo(bottomDiv.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            },
            onClickSendMessage,
            onClickAdd
        )
    }
}