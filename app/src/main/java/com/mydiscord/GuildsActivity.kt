package com.mydiscord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.model.Message
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.GuildsViewModel
import dev.kord.common.entity.DiscordMessage
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import dev.kord.core.entity.channel.TopGuildMessageChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import okhttp3.internal.format
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class GuildsActivity : ComponentActivity() {
    private val guildsViewModel: GuildsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@GuildsActivity)

        setContent {
            MydiscordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        guilds = guildsViewModel.guilds.collectAsState().value,
                        channels = guildsViewModel.channels.collectAsState().value,
                        selectedGuild = guildsViewModel.getGuild(),
                        selectedChannel = guildsViewModel.selectedChannel,
                        channelMessages = guildsViewModel.channelMessages,
                    )
                }
            }
        }

        lifecycleScope.launch {
            this@GuildsActivity.guildsViewModel.updateGuilds()
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun Content(
        guilds: List<Guild> = listOf(),
        channels: List<TopGuildChannel> = listOf(),
        selectedGuild: Guild? = null,
        selectedChannel: MutableStateFlow<TopGuildChannel?> = MutableStateFlow(null),
        channelMessages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf()),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.
                    fillMaxWidth()
            ) {
                val channelOpened = remember { mutableStateOf(false) }
                if (!channelOpened.value) {
                    GuildList(guilds)
                }

                if (selectedGuild != null) {
                    if (channelOpened.value) {
                        val selectedChannel = selectedChannel.collectAsState().value
                        if (selectedChannel != null) {
                            GuildChannelFeed(
                                channelName = selectedChannel.name,
                                channelOpened = channelOpened,
                                channelMessages = channelMessages,
                            )
                        }
                    } else {
                        GuildChannels(channels, selectedGuild, channelOpened, selectedChannel)
                        GuildChannelMessages(
                            modifier = Modifier.fillMaxHeight(),
                            messages = channelMessages.collectAsState().value
                        )
                    }
                }
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildList(guilds: List<Guild> = listOf()) {
        val imageSize = 60.dp

        Column(
            modifier = Modifier
                .width(imageSize)
                .fillMaxHeight()
                .background(Color.Blue)
            ,
        ) {
            for (guild in guilds) {
                GuildItem(guild)
            }
        }
    }

    @Composable
    private fun GuildItem(guild: Guild) {
        val icon = guild.data.icon ?: "https://cdn.discordapp.com/icons/897746126772502569/8cc146043482be4eb78d522dcb25545f.webp?size=240"

        Column(
            modifier = Modifier
                .padding(3.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Surface(
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, Color.Black),
                        CircleShape
                    ),
                color = Color.Transparent
            ) {
                AsyncImage(
                    model = icon,
                    contentDescription = "photo",
                    Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .clickable {
                            val currentGuild = this@GuildsActivity.guildsViewModel.getGuild()
                            if (guild.id != currentGuild?.id) {
                                lifecycleScope.launch {
                                    this@GuildsActivity.guildsViewModel.switchGuild(guild)
                                }
                            }
                        }
                )
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannels(
        channels: List<TopGuildChannel> = listOf(),
        guild: Guild? = null,
        channelOpened: MutableState<Boolean> = remember { mutableStateOf(false) },
        selectedChannel: MutableStateFlow<TopGuildChannel?> = MutableStateFlow(null),
    ) {
        Column(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxHeight()
                .width(250.dp)
        ) {
            if (guild == null) {
                return
            }

            for (channel in channels) {
                println("Channel: ${channel.name}")
                val onclick = {
                    channelOpened.value = this@GuildsActivity.guildsViewModel.switchChannel(channel)
                }

                GuildChannelItem(channel.name, onclick)
            }

            if (channels.isEmpty()) {
                println("No channels")
                Text("No channels")
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelItem(
        text: String = "general",
        onClick: () -> Unit = {},
    ) {
        TextButton(
            onClick = onClick,
            elevation = null,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Black,
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(22.dp)
        ) {
            Text(text)
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelMessages(
        modifier: Modifier = Modifier,
        modifierConstraint: Modifier = Modifier,
        messages: List<Message> = listOf(),
    ) {
        val verticalGap = 12.dp
        Column(modifier = modifierConstraint) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(28.dp),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = verticalGap),
                userScrollEnabled = true,
                reverseLayout = true,
                modifier = modifier
                    .background(Color.Green)
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                if (messages.isEmpty()) {
                    item {
                        Text("No messages")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height((-verticalGap.value).dp))
                }

                items(messages) { message ->
                    GuildChannelMessage(message)
                }
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelMessage(message: Message = Message(
        "Hello",
        Instant.fromEpochMilliseconds(0),
        "author",
        "https://cdn.discordapp.com/icons/897746126772502569/8cc146043482be4eb78d522dcb25545f.webp?size=240"
    )) {
        Row(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            val locale = Locale.FRENCH
            val date = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(
                Date(message.date.epochSeconds * 1000)
            )
            val time = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(
                Date(message.date.epochSeconds * 1000)
            )

            val messageDate = format("%s %s", date, time);

            //Text(messageDate, modifier = Modifier.offset(10.dp, 0.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = "https://cdn.discordapp.com/icons/897746126772502569/8cc146043482be4eb78d522dcb25545f.webp?size=240",
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
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(message.author)
                        Text(messageDate)
                    }
                    Text(message.content)
                }
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelMessageInput(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .defaultMinSize(minHeight = 50.dp)
                .fillMaxWidth()
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
                        val channel = this@GuildsActivity.guildsViewModel.selectedChannel.value ?: return@launch

                        if (channel is TextChannel) {
                            this@GuildsActivity.guildsViewModel.sendMessage(givenToken.value.text, channel)
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

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelHeader(
        modifier: Modifier = Modifier,
        text: String = "channel",
        onClick: () -> Unit = {},
        onClickBack: () -> Unit = {},
        onClickSearch: () -> Unit = {}
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .defaultMinSize(minHeight = 50.dp)
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, Color.Black),
                    MaterialTheme.shapes.medium
                )
        ) {
            IconButton(onClick = onClickBack) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier
                        .size(20.dp)
                )
            }
            TextButton(
                onClick = onClick,
            ) {
                Text(text)
            }
            IconButton(onClick = onClickSearch) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "search",
                    modifier = Modifier
                        .size(20.dp)
                        // TODO: put search icon at the end

                )
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildChannelFeed(
        channelName: String = "General",
        channelOpened: MutableState<Boolean> = remember { mutableStateOf(false) },
        channelMessages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf(
            Message("hello"),
            Message("world"),
        )),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val (header, messages, input) = createRefs()

            GuildChannelHeader(modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(messages.top)
            }, channelName, onClick = {
                channelOpened.value = false
            }, onClickBack = {
                channelOpened.value = false
            }, onClickSearch = {
                // todo
            })

            GuildChannelMessages(modifierConstraint = Modifier.constrainAs(messages) {
                top.linkTo(header.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, messages = channelMessages.collectAsState().value)

            GuildChannelMessageInput(Modifier.constrainAs(input) {
                //top.linkTo(messages.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}
