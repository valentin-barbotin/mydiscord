package com.mydiscord.viewmodel

import androidx.lifecycle.ViewModel
import com.mydiscord.repositories.KordRepository

class ProfileViewModel(
    private val kordRepo: KordRepository
): ViewModel() {

    fun getUsername(): String {
        return this.kordRepo.getUsername()
    }

    fun getAvatar(): String {
        return this.kordRepo.getAvatar()
    }
}