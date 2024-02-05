package com.mydiscord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydiscord.repositories.KordRepository
import dev.kord.core.Kord
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val kordRepo: KordRepository
): ViewModel() {
    var token = ""

    suspend fun loginWithToken(token: String): Result<Kord> {
        return viewModelScope.run {
            kordRepo.loginWithToken(token)
        }
    }
}