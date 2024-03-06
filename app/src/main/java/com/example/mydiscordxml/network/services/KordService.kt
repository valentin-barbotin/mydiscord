package com.example.mydiscordxml.network.services

import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.addFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.concurrent.thread

class KordService {

    lateinit var kord: Kord

    private fun isInitialized(): Boolean {
        return ::kord.isInitialized
    }

    @OptIn(PrivilegedIntent::class)
    suspend fun loginWithToken(token: String): Result<Kord> {
        println("le start")
        this.kord = Kord(token)

        thread {
            runBlocking {
                this@KordService.kord.login {
                    presence { playing("bot presence") }

                    intents = Intents.ALL

                    /*
                    kord.on<MessageCreateEvent> {
                        println("message received")
                    }
                     */

                    this@KordService.kord
                }
            }
        }

        return Result.success(this@KordService.kord)
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

    suspend fun sendFileMessage(channel: TextChannel, file: Path, filename: String): Message {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return channel.createMessage {
            addFile(file) {
                this.filename = filename
            }
        }
    }

    suspend fun getGuildMembers(id: Snowflake): List<DiscordGuildMember> {
        if (!this.isInitialized()) {
            throw Exception("Kord is not initialized")
        }

        return this.kord.rest.guild.getGuildMembers(id)
    }
}