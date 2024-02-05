package com.mydiscord.repositories

import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap
import com.mydiscord.network.services.KordService
import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Asset
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class KordRepository(
    private val kordService: KordService
) {

    fun getUser(): User {
        return runBlocking {
            val user = kordService.getUser()
            user
        }
    }

    suspend fun getUser(id: Snowflake): User? {
        return this.kordService.getUser(id)
    }

    suspend fun getGuildMembers(id: Snowflake): List<DiscordGuildMember> {
        return this.kordService.getGuildMembers(id)
    }

    fun getAvatar(id: Snowflake): String {
        return runBlocking {
            val user = kordService.getUser(id)

            when (val avatar = user?.avatar) {
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

    suspend fun sendMessageInChannel(message: String, channel: TextChannel): Message {
        return this.kordService.sendMessageInChannel(message, channel)
    }
}