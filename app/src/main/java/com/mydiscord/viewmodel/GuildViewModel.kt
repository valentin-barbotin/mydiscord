package com.mydiscord.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydiscord.repositories.KordRepository
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.MessageData
import dev.kord.core.cache.data.toData
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.on
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.file.Path

class GuildViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    private var selectedGuild: MutableState<Guild?> = mutableStateOf(null)

    var selectedChannel = mutableStateOf<TopGuildChannel?>(null)
    var channelMessages = mutableStateListOf<MessageData>()
    private var switchChannelJob = mutableStateOf<Job?>(null)

    init {
        this.kordRepo.kordService.kord.on<MessageCreateEvent> {
            val message = this.message
            if (message.channelId == this@GuildViewModel.selectedChannel.value?.id) {
                this@GuildViewModel.channelMessages.add(message.data)
            }
        }

        this.kordRepo.kordService.kord.on<MessageDeleteEvent> {
            if (this.channelId == this@GuildViewModel.selectedChannel.value?.id) {
                this@GuildViewModel.channelMessages.removeIf { it.id == this.messageId }
            }
        }
    }

    fun switchChannel(channel: TopGuildChannel): Boolean {
        when (channel.type) {
            is ChannelType.GuildText -> {
                this.switchChannelJob.value.run { this?.cancel() }

                this.switchChannelJob.value = viewModelScope.launch {

                    this@GuildViewModel.refreshChannel(channel.id)

                    this@GuildViewModel.selectedChannel.value = channel

                }

                return true
            }

            else -> {
                return false
            }
        }
    }

    private fun refreshChannel(id: Snowflake) {
        viewModelScope.launch {
            this@GuildViewModel.channelMessages.clear()
            this@GuildViewModel.kordRepo.getChannelMessages(id)
                .map {
                    it.toData()
                }
                .toCollection(this@GuildViewModel.channelMessages)
        }
    }

    /*private suspend fun updateChannels() {
        val guild = this.getGuild().value

        if (guild != null) {
            channels.clear()
            channels.addAll(guild.channels.toList())
        }
    }*/

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val channel = this@GuildViewModel.selectedChannel.value as? TextChannel
            if (channel != null) {
                val message = this@GuildViewModel.kordRepo.sendMessageInChannel(message, channel)
            }
        }
    }

    fun sendFileMessage(file: Path, filename: String) {
        viewModelScope.launch {
            val channel = this@GuildViewModel.selectedChannel.value as? TextChannel
            if (channel != null) {
                val message = this@GuildViewModel.kordRepo.sendFileMessage(channel, file, filename)
            }
            file.toFile().delete()
        }
    }
}