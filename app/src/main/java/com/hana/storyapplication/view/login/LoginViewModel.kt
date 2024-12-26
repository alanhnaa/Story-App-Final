package com.hana.storyapplication.view.login

import androidx.lifecycle.ViewModel
import com.hana.storyapplication.data.UserRepository
import com.hana.storyapplication.data.preference.UserModel

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }
}