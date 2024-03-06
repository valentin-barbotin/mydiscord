package com.example.mydiscordxml.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.mydiscordxml.repositories.KordRepository
import com.google.android.material.search.SearchView.Behavior
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
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

class GuildsViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    /*val channels: BehaviorSubject<List<TopGuildChannel>> = BehaviorSubject.createDefault(
        listOf()
    )*/

    /*val guilds: BehaviorSubject<List<Guild>> = BehaviorSubject.createDefault(
        listOf()
    )*/

    private val avatars: BehaviorSubject<Map<Snowflake, String>> = BehaviorSubject.createDefault(
        mapOf()
    )

    val guilds: MutableLiveData<List<Guild>> = MutableLiveData(emptyList())
    val channels: MutableLiveData<List<TopGuildChannel>> = MutableLiveData(emptyList())

    var selectedGuild = BehaviorSubject.createDefault(Snowflake(0))

    suspend fun login(token: String = "MTE5NzI4NTg2NDc3OTMwNTA2Mg.GH2OyU.F4T9LBUf0a6bo-5v7iOrQJU_SeFLN7AnGWyS1I", callBack: () -> Unit = {}) {
        val auth = this.kordRepo.loginWithToken(token)
        auth.onFailure {
            println("Failed to login")
            println(it.message)
        }

        auth.onSuccess {
            println("Logged in")
            callBack()
        }
    }

    fun getGuilds(callBack: () -> Unit = {}) {
        this.viewModelScope.launch {
            val guilds = this@GuildsViewModel.kordRepo.getGuilds().toList()
            this@GuildsViewModel.guilds.postValue(guilds)
            if (guilds.isNotEmpty()) {
                this@GuildsViewModel.selectedGuild.onNext(guilds.first().id)
                this@GuildsViewModel.channels.postValue(guilds.first().channels.toList())
            }

            callBack()
        }
    }

    fun switchGuild(guild: Guild, callBack: () -> Unit = {}) {
        this.viewModelScope.launch {
            this@GuildsViewModel.selectedGuild.onNext(guild.id)
            this@GuildsViewModel.channels.postValue(guild.channels.toList())
            callBack()
        }
    }
}
