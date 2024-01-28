package com.mydiscord.model

import kotlinx.datetime.Instant
import java.util.Date

data class Message(
    val content: String = "",
    val date: Instant = Instant.fromEpochMilliseconds(0),
    val author: String = "",
    val avatar: String? = null
)