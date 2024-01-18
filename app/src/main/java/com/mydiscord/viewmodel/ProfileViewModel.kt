package com.mydiscord.viewmodel

import androidx.lifecycle.ViewModel
import com.mydiscord.repositories.KordRepository
import dev.kord.core.Kord
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    suspend fun getUsername(): String {
        return this.kordRepo.getUsername()
    }

    suspend fun loginWithToken(token: String): Result<Kord> {
        return this.kordRepo.loginWithToken(token)
    }
}