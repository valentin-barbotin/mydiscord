package com.mydiscord.repositories

import com.mydiscord.network.services.KordService
import dev.kord.core.Kord

class KordRepository(
    private val kordService: KordService
) {
    suspend fun getUsername(): String {
        return this.kordService.getUsername()
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        return this.kordService.loginWithToken(token)
    }
}