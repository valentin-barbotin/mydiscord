package com.mydiscord

import org.junit.Assert
import org.junit.Test

class TestLogin {
    @Test
    suspend fun test_login() {
        val client = Client();

        client.login()
    }
}