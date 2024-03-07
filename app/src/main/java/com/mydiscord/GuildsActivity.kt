package com.mydiscord

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.mydiscord.di.injectModuleDependencies
import com.mydiscord.ui.components.DiscordNavBar
import com.mydiscord.ui.components.GuildChannelFeed
import com.mydiscord.ui.components.GuildChannelMessages
import com.mydiscord.ui.components.NavItem
import com.mydiscord.ui.theme.*
import com.mydiscord.ui.theme.MydiscordTheme
import com.mydiscord.viewmodel.GuildViewModel
import com.mydiscord.viewmodel.GuildsViewModel
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.MessageData
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuildsActivity : ComponentActivity() {
    private val guildsViewModel: GuildsViewModel by viewModel()
    private val guildViewModel: GuildViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectModuleDependencies(this@GuildsActivity)

        setContent {
            MydiscordTheme(true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        guilds = guildsViewModel.guilds,
                        selectedGuild = guildsViewModel.getGuild(),
                        selectedChannel = guildViewModel.selectedChannel,
                        channelMessages = guildViewModel.channelMessages,
                        channels = guildsViewModel.channels
                    )
                }
            }
        }

        this@GuildsActivity.guildsViewModel.updateGuilds()
    }

    fun navigateTo(destination: String) {
        when (destination) {
            NavItem.SERVEURS.name -> {
                val intent = Intent(this, GuildsActivity::class.java)
                startActivity(intent)
            }
            NavItem.MESSAGES.name -> {
                //val intent = Intent(this, FriendsActivity::class.java)
                //startActivity(intent)
            }
            NavItem.TOI.name -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun Content(
        guilds: SnapshotStateList<Guild> = mutableStateListOf(),
        selectedGuild: MutableState<Guild?> = mutableStateOf(null),
        selectedChannel: MutableState<TopGuildChannel?> = mutableStateOf(null),
        channelMessages: SnapshotStateList<MessageData> = mutableStateListOf(),
        channels: SnapshotStateList<TopGuildChannel> = mutableStateListOf()
    ) {
        val channelOpened = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            val activity = (LocalContext.current as? GuildsActivity)
            val (selectedNavItem, setSelectedNavItem) = remember { mutableStateOf(NavItem.SERVEURS) }
            DiscordNavBar(
                selectedNavItem = selectedNavItem,
                onNavItemClicked = { navItem ->
                    if (navItem != selectedNavItem) {
                        setSelectedNavItem(navItem)
                        activity?.navigateTo(navItem.name)
                    } else {
                        // reload la page
                    }
                }
            )
            Row(
                modifier = Modifier.
                    fillMaxWidth()
            ) {
                if (!channelOpened.value) {
                    GuildList(guilds)
                }

                if (channelOpened.value) {
                    val selectedChannel = selectedChannel.value
                    if (selectedChannel != null) {
                        GuildChannelFeed(
                            channelOpened = channelOpened,
                            channelMessages = guildViewModel.channelMessages,
                            selectedChannel = selectedChannel,
                            sendMessage = guildViewModel::sendMessage,
                            lifecycleScope = lifecycleScope
                        )
                    }
                } else {
                    GuildChannels(
                        guild = selectedGuild,
                        channelOpened = channelOpened,
                        selectedChannel = selectedChannel,
                        channels = channels
                    )
                    GuildChannelMessages(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 7.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 10.dp
                                )
                            )
                        ,
                        messages = channelMessages,
                        avatars = guildsViewModel.avatars
                    )
                }
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun GuildList(guilds: MutableList<Guild> = mutableListOf()) {
        val imageSize = 60.dp

        Column(
            modifier = Modifier
                .padding(5.dp)
                .width(imageSize)
                .fillMaxHeight()
            ,
        ) {
            for (guild in guilds) {
                GuildItem(guild)
            }
        }
    }

    @Composable
    private fun GuildItem(guild: Guild) {
        val icon = guild.icon?.cdnUrl?.toUrl() ?: "https://cdn.discordapp.com/icons/897746126772502569/8cc146043482be4eb78d522dcb25545f.webp?size=240"

        Column(
            modifier = Modifier
                .padding(3.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Surface(
                modifier = Modifier
                    .border(
                        BorderStroke(0.dp, Color.Transparent),
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
                            if (guild.id != currentGuild.value?.id) {
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
        guild: MutableState<Guild?> = mutableStateOf(null),
        channelOpened: MutableState<Boolean> =  mutableStateOf(false),
        selectedChannel: MutableState<TopGuildChannel?> = mutableStateOf(null),
        channels: SnapshotStateList<TopGuildChannel> = mutableStateListOf()
    ) {
        val hiddenCategories = remember { mutableStateListOf<Snowflake>() }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topEnd = 10.dp,
                        topStart = 35.dp
                    )
                )
                .width(250.dp)
                .background(DarkColorScheme.secondary)
                .padding(10.dp)
        ) {
            val guild = guild.value ?: return

            GuildHeader(guild.name, onClick = {
                println("Guild settings")
            })

            val categories = HashMap<TopGuildChannel, MutableList<TopGuildChannel>>()

            for (channel in channels) {
                if (channel.type is ChannelType.GuildCategory) {
                    categories[channel] = mutableListOf()
                }
            }

            for (channel in channels) {
                when (channel) {
                    is TextChannel -> {
                        if (channel.categoryId != null) {
                            val category = categories.keys.find { it.id == channel.categoryId }
                            if (category != null) {
                                categories[category]?.add(channel)
                            }
                        }
                    }
                }
            }

            for (category in categories) {
                GuildChannelHeader(category.key.name, onClick = {
                    if (hiddenCategories.contains(category.key.id)) {
                        hiddenCategories.remove(category.key.id)
                        println("removed")
                        println(category.key.id)
                    } else {
                        hiddenCategories.add(category.key.id)
                        println("added")
                        println(category.key.id)
                    }
                }, hidden = hiddenCategories.contains(category.key.id))

                category.value.sortBy {
                    it.rawPosition
                }

                if (hiddenCategories.contains(category.key.id)) {
                    println(category.key.id)
                    println("hidden")
                    continue
                }

                for (channel in category.value) {
                    val onclick = {
                        channelOpened.value = this@GuildsActivity.guildViewModel.switchChannel(channel)
                    }

                    GuildChannelItem(channel.name, onclick)
                }
            }

            if (channels.isEmpty()) {
                println("No channels")
                Text("No channels")
            }
        }
    }

    @Composable
    @Preview(showBackground = false)
    private fun GuildHeader(
        text: String = "My guild",
        onClick: () -> Unit = {}
    ) {
        Row {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = DarkColorScheme.tertiary,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(0.8f)
            )
            IconButton(
                onClick = onClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Guild settings",
                    tint = DarkColorScheme.tertiary
                )
            }
        }
        Divider()
    }

    @Composable
    @Preview(showBackground = false)
    private fun GuildChannelItem(
        text: String = "general",
        onClick: () -> Unit = {},
    ) {
        TextButton(
            onClick = onClick,
            //elevation = null,
            colors = ButtonDefaults.textButtonColors(
                contentColor = DarkColorScheme.tertiary,
            ),
            contentPadding = PaddingValues(
                start = 10.dp,
                end = 10.dp,
            ),
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth()
        ) {
            Text(text = "#") //TODO: add icons
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
            )
        }
    }

    @Composable
    @Preview(showBackground = false)
    private fun GuildChannelHeader(
        text: String = "general",
        onClick: () -> Unit = {},
        hidden: Boolean = false,
    ) {
        TextButton(
            onClick = onClick,
            //elevation = null,
            colors = ButtonDefaults.textButtonColors(
                contentColor = DarkColorScheme.tertiary,
            ),
            contentPadding = PaddingValues(
                end = 10.dp,
            ),
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = if (hidden) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = "Guild settings",
                tint = DarkColorScheme.tertiary,
            )
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
            )
        }
    }
}