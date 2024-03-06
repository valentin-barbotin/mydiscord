package com.example.mydiscordxml.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.mydiscordxml.repositories.KordRepository
import com.google.android.material.search.SearchView.Behavior
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.MessageData
import dev.kord.core.cache.data.toData
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.TopGuildChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.MessageDeleteEvent
import dev.kord.core.on
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths

class ChannelViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    val messages: MutableLiveData<List<MessageData>> = MutableLiveData(emptyList())
    val channel = MutableLiveData<TopGuildChannel>()

    init {
        this.kordRepo.kordService.kord.on<MessageCreateEvent> {
            val message = this.message
            if (message.channelId == this@ChannelViewModel.channel.value?.id) {
                this@ChannelViewModel.messages.postValue(
                    this@ChannelViewModel.messages.value?.plus(message.data)
                )
            }
        }

        this.kordRepo.kordService.kord.on<MessageDeleteEvent> {
            val message = this.message
            if (message != null) {
                if (message.channelId == this@ChannelViewModel.channel.value?.id) {
                    this@ChannelViewModel.messages.postValue(
                        this@ChannelViewModel.messages.value?.filter { it.id != message.id } ?: emptyList()
                    )
                }
            } else {
                this@ChannelViewModel.refreshChannel()
            }
        }
    }

    fun refreshChannel() {
        val id = channel.value?.id ?: return

        viewModelScope.launch {
            this@ChannelViewModel.messages.postValue(
                this@ChannelViewModel.kordRepo.getChannelMessages(id)
                    .map {
                        println(it)
                        it.toData()
                    }
                    .reversed()
            )
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val channel = this@ChannelViewModel.channel.value as TextChannel
            if (channel != null) {
                val message = this@ChannelViewModel.kordRepo.sendMessageInChannel(message, channel)
            }
        }
    }

    fun sendFileMessage(file: Path, filename: String) {
        viewModelScope.launch {
            val channel = this@ChannelViewModel.channel.value as TextChannel
            if (channel != null) {
                val message = this@ChannelViewModel.kordRepo.sendFileMessage(channel, file, filename)
            }
            file.toFile().delete()
        }
    }
}
