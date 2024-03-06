package com.example.mydiscordxml.di.modules

import com.example.mydiscordxml.network.services.KordService
import com.example.mydiscordxml.repositories.KordRepository
import com.example.mydiscordxml.viewmodel.ChannelViewModel
import com.example.mydiscordxml.viewmodel.GuildsViewModel

import org.koin.dsl.module

internal val coreModules = module {
    // Singletons
    single { KordRepository(get()) }

    single { KordService() }
    single { GuildsViewModel(get()) }
    single { ChannelViewModel(get()) }

    /*single { MainViewModel(get()) }
    single { ProfileViewModel(get()) }
    single { GuildsViewModel(get()) }
    single { GuildViewModel(get()) }*/
}
