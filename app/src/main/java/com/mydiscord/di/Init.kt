package com.mydiscord.di

import android.content.Context
import com.mydiscord.di.modules.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.error.ApplicationAlreadyStartedException

private val modules = mutableListOf(coreModules)

fun injectModuleDependencies(context: Context) {
    try {
        startKoin {
            androidContext(context)
            modules(modules)
        }
    } catch (alreadyStart: ApplicationAlreadyStartedException) {
        loadKoinModules(modules)
    }
}

