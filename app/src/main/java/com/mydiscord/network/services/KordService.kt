package com.mydiscord.network.services

import android.os.SystemClock.sleep
import dev.kord.core.Kord
import dev.kord.core.entity.Asset
import kotlinx.coroutines.runBlocking

class KordService {

    private lateinit var kord: Kord

    fun isInitialized(): Boolean {
        return ::kord.isInitialized
    }

    suspend fun getUsername(): String {
        return this.kord.getSelf().username
    }

    suspend fun getAvatar(): Asset? {
        return this.kord.getSelf().avatar
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        this.kord = Kord(token)

        return Result.success(this.kord)
    }
}