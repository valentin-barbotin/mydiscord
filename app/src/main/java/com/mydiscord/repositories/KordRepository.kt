package com.mydiscord.repositories

import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap
import com.mydiscord.network.services.KordService
import dev.kord.core.Kord
import dev.kord.core.entity.Asset
import kotlinx.coroutines.runBlocking

class KordRepository(
    private val kordService: KordService
) {

    fun getUsername(): String {
        if (!kordService.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return runBlocking {
            val username = kordService.getUsername()
            username
        }
    }

    fun getAvatar(): String {
        if (!kordService.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return runBlocking {

            when (val avatar = kordService.getAvatar()) {
                null -> return@runBlocking ""
                else -> return@runBlocking avatar.cdnUrl.toUrl()
            }

        }
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        return this.kordService.loginWithToken(token)
    }
}