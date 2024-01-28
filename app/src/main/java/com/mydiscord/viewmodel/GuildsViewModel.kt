package com.mydiscord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydiscord.model.Message
import com.mydiscord.repositories.KordRepository
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordMessage
import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class GuildsViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    private var selectedGuild: Guild? = null

    val channels = MutableStateFlow(emptyList<TopGuildChannel>())
    val guilds = MutableStateFlow(emptyList<Guild>())
    val selectedChannel = MutableStateFlow<TopGuildChannel?>(null)
    val channelMessages = MutableStateFlow(emptyList<Message>())
    private var switchChannelJob = MutableStateFlow<Job?>(null)

    fun switchInProgress() = this.switchChannelJob.value?.isActive ?: false

    suspend fun updateGuilds() {
        guilds.value = this.kordRepo.getGuilds().toList().sorted()
    }

    private suspend fun updateChannels() {
        val guild = this.getGuild()
        
        if (guild != null) {
            channels.value = guild.channels.toList()
        }
    }

    suspend fun switchGuild(guild: Guild) {
        this.selectedGuild = guild
        this.updateChannels()
    }

    fun switchChannel(channel: TopGuildChannel): Boolean {
        when (channel.type) {
            is ChannelType.GuildText -> {
                this.switchChannelJob.value.run { this?.cancel() }

                this.switchChannelJob.value = viewModelScope.launch {
                    this@GuildsViewModel.channelMessages.value = emptyList()
                    this@GuildsViewModel.channelMessages.value = this@GuildsViewModel.kordRepo.getChannelMessages(channel.id).map {
                        println("Message: ${it.timestamp}")
                        Message(
                            it.content,
                            it.timestamp,
                            it.author.username,
                            it.author.avatar
                        )
                    }.sortedWith(compareBy { it.date }).reversed()

                    this@GuildsViewModel.selectedChannel.value = channel
                }

                return true
            }

            else -> {
                return false
            }
        }
    }

    suspend fun sendMessage(message: String, channel: TextChannel) {
        this.kordRepo.sendMessageInChannel(message, channel)
    }

    fun getGuild() = this.selectedGuild
}