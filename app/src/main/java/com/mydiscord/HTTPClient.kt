package com.mydiscord

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*

class HTTPClient {
    val httpClient: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }
}