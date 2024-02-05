package com.mydiscord.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydiscord.repositories.KordRepository
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.MessageData
import dev.kord.core.cache.data.toData
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class GuildViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    private var selectedGuild: MutableState<Guild?> = mutableStateOf(null)

    var selectedChannel = mutableStateOf<TopGuildChannel?>(null)
    var channelMessages = mutableStateListOf<MessageData>()
    private var switchChannelJob = mutableStateOf<Job?>(null)

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

    suspend fun sendMessage(message: String, channel: TextChannel) {
        val message = this.kordRepo.sendMessageInChannel(message, channel)
        this@GuildViewModel.channelMessages.add(message.data)
        // TODO: addMessage => refresh ? push to list ?
        //refreshChannel(channel.id)
    }

    /*fun addMessage(message: DiscordMessage) {
        this.channelMessages.add(message)
    }*/

    fun getGuild() = this.selectedGuild
}