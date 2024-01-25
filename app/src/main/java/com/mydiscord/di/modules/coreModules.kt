package com.mydiscord.di.modules

import com.mydiscord.network.services.KordService
import com.mydiscord.repositories.KordRepository
import com.mydiscord.viewmodel.MainViewModel
import com.mydiscord.viewmodel.ProfileViewModel
import org.koin.dsl.module

internal val coreModules = module {
    // Singletons
    single { KordRepository(get()) }

    single { KordService() }

    single { MainViewModel(get()) }
    single { ProfileViewModel(get()) }
}
