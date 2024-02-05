package com.mydiscord.network.services

import android.os.SystemClock.sleep
import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Asset
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class KordService {

    private lateinit var kord: Kord

    private fun isInitialized(): Boolean {
        return ::kord.isInitialized
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        this.kord = Kord(token)

        return Result.success(this.kord)
    }

    suspend fun getUser(): User {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.getSelf()
    }

    suspend fun getUser(id: Snowflake): User? {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.getUser(id)
    }

    fun getGuilds(): Flow<Guild> {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.guilds
    }

    suspend fun getChannelMessages(id: Snowflake): List<DiscordMessage> {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.rest.channel.getMessages(id)
    }

    suspend fun sendMessageInChannel(message: String, channel: TextChannel): Message {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return channel.createMessage(message)
    }

    suspend fun getGuildMembers(id: Snowflake): List<DiscordGuildMember> {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.rest.guild.getGuildMembers(id)
    }
}