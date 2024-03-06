package com.example.mydiscordxml

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiscordxml.di.injectModuleDependencies
import com.example.mydiscordxml.viewmodel.ChannelViewModel
import com.example.mydiscordxml.viewmodel.GuildsViewModel
import com.example.mydiscordxml.views.adapters.ChannelsListAdapter
import com.example.mydiscordxml.views.adapters.GuildsListAdapter
import com.example.mydiscordxml.views.adapters.OnChannelClicked
import com.example.mydiscordxml.views.adapters.OnGuildClicked
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TopGuildChannel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity(), OnGuildClicked, OnChannelClicked {

    private val guildsViewModel: GuildsViewModel by viewModel()
    private val channelsViewModel: ChannelViewModel by viewModel()

    private lateinit var guildsRv: RecyclerView
    private lateinit var channelsRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectModuleDependencies(this@MainActivity)

        this.guildsRv = findViewById(R.id.guilds_rv)
        this.channelsRv = findViewById(R.id.channels_rv)

        this.lifecycleScope.launch {
            this@MainActivity.guildsViewModel.login {

                this@MainActivity.guildsViewModel.guilds.observe(this@MainActivity) {
                    this@MainActivity.setupGuildsList(it)
                }

                this@MainActivity.guildsViewModel.channels.observe(this@MainActivity) {
                    this@MainActivity.setupChannelsList(it)
                }

                this@MainActivity.guildsViewModel.getGuilds()
            }

        }
    }

    private fun setupGuildsList(guilds: List<Guild>) {
        val adapter = GuildsListAdapter(guilds, this)
        guildsRv.layoutManager = LinearLayoutManager( this)
        guildsRv.adapter = adapter
    }

    private fun setupChannelsList(channels: List<TopGuildChannel>) {
        val adapter = ChannelsListAdapter(channels, this)
        channelsRv.layoutManager = LinearLayoutManager( this)
        channelsRv.adapter = adapter
    }

    override fun channelClicked(channel: TopGuildChannel) {
        this.channelsViewModel.channel.postValue(channel)
        displayChannel()
    }

    private fun displayChannel() {
        Intent(
            this,
            ChannelActivity::class.java
        ).also {
            startActivity(it)
        }
    }

    override fun displayGuild(guild: Guild) {
        this.guildsViewModel.switchGuild(guild)
    }

    /*override fun displayConversation(guild: Guild) {
        Intent(
            this,
            ConversationDetailActivity::class.java
        ).also {
            this.usersViewModel.currentUserId = userDataDto.id
            startActivity(it)
        }
    }*/
}
