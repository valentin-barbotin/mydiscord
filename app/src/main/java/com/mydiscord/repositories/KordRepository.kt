package com.mydiscord.repositories

import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap
import com.mydiscord.network.services.KordService
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Asset
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class KordRepository(
    private val kordService: KordService
) {

    fun getUsername(): String {
        return runBlocking {
            val username = kordService.getUsername()
            username
        }
    }

    fun getAvatar(): String {
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

    fun getGuilds(): Flow<Guild> {
        return this.kordService.getGuilds()
    }

    suspend fun getChannelMessages(id: Snowflake): List<DiscordMessage> {
        return this.kordService.getChannelMessages(id)
    }

    suspend fun sendMessageInChannel(message: String, channel: TextChannel) {
        this.kordService.sendMessageInChannel(message, channel)
    }
}