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
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class GuildsViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    private var selectedGuild: MutableState<Guild?> = mutableStateOf(null)

    var guilds = mutableStateListOf<Guild>()
    var channels = mutableStateListOf<TopGuildChannel>()
    val avatars = hashMapOf<Snowflake, String>()
    private var switchChannelJob = mutableStateOf<Job?>(null)

    suspend fun switchGuild(guild: Guild) {
        this.switchChannelJob.value.run { this?.cancel() }
        this.selectedGuild.value = guild
        this.updateChannels()
        //this.getAvatars()
    }

    private fun getAvatars() {
        val guild = this.getGuild().value

        if (guild != null) {
            viewModelScope.launch {
                //val members = kordRepo.getGuildMembers(guild.id)

                //members.onEach {
                    //val id = it.user.value?.id
                    //val avatar = it.user.value?.avatar

                    //if (id != null && avatar != null) {
                        //this@GuildsViewModel.avatars[id] = avatar
                    //}
                //}
            }
        }
    }

    private suspend fun updateChannels() {
        val guild = this.getGuild().value

        if (guild != null) {
            channels.clear()
            channels.addAll(guild.channels.toList())
        }
    }

    fun updateGuilds() {
        viewModelScope.launch {
            this@GuildsViewModel.guilds.clear()

            this@GuildsViewModel.guilds.addAll(
                this@GuildsViewModel.kordRepo.getGuilds().toList()
            )
        }
    }

    fun getGuild() = this.selectedGuild
}