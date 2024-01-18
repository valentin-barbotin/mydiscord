package com.mydiscord.network.services

import dev.kord.core.Kord
import kotlinx.coroutines.runBlocking

class KordService {

    private lateinit var kord: Kord

    suspend fun getUsername(): String {
        if (!::kord.isInitialized) {
            return "Unknown"
        }

        return this.kord.getSelf().username
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        this.kord = Kord(token)
        return Result.success(this.kord)
    }
}