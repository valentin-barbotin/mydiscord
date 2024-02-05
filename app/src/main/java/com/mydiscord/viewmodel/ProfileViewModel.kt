package com.mydiscord.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mydiscord.repositories.KordRepository
import dev.kord.core.entity.User

class ProfileViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    private var user: MutableState<User?> = mutableStateOf(null)

    init {
        this.user.value = this.kordRepo.getUser()
    }

    fun getUser(): User? {
        return this.user.value
    }
}